package chat.hola.com.app.Database;

/*
 * Created by moda on 07/01/17.
 */


import com.couchbase.lite.BlobKey;
import com.couchbase.lite.BlobStore;
import com.couchbase.lite.Cache;
import com.couchbase.lite.ChangesOptions;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DatabaseOptions;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Misc;
import com.couchbase.lite.Status;
import com.couchbase.lite.TransactionalTask;
import com.couchbase.lite.internal.Body;
import com.couchbase.lite.internal.InterfaceAudience;
import com.couchbase.lite.replicator.ReplicationState;
import com.couchbase.lite.storage.SQLException;
import com.couchbase.lite.store.EncryptableStore;
import com.couchbase.lite.support.Base64;
import com.couchbase.lite.support.FileDirUtils;
import com.couchbase.lite.support.HttpClientFactory;
import com.couchbase.lite.support.action.Action;
import com.couchbase.lite.support.action.ActionBlock;
import com.couchbase.lite.support.action.ActionException;
import com.couchbase.lite.support.security.SymmetricKey;
import com.couchbase.lite.support.security.SymmetricKeyException;
import com.couchbase.lite.util.CollectionUtils;
import com.couchbase.lite.util.CollectionUtils.Functor;
import com.couchbase.lite.util.IOUtils;
import com.couchbase.lite.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A CouchbaseLite Database.
 */
public class Database implements StoreDelegate {


    public static final String TAG = Log.TAG_DATABASE;

    // When this many changes pile up in _changesToNotify, start removing their bodies to save RAM
    private static final int MANY_CHANGES_TO_NOTIFY = 5000;

    // How long to wait after a database opens before expiring docs
    private static final long kHousekeepingDelayAfterOpening = 3;

    private static final String DEFAULT_PBKDF2_KEY_SALT = "Salty McNaCl";
    private static final int DEFAULT_PBKDF2_KEY_ROUNDS = 64000;

    private static final String DEFAULT_STORAGE = Manager.SQLITE_STORAGE;
    // private static final String SQLITE_STORE_CLASS = "com.couchbase.lite.store.SQLiteStore";


    private static final String SQLITE_STORE_CLASS = "chat.hola.com.app.Database.SQLiteStore";

    private static final String FORESTDB_STORE_CLASS = "com.couchbase.lite.store.ForestDBStore";

    private static ReplicationFilterCompiler filterCompiler;

    // Length that constitutes a 'big' attachment
    private static int kBigAttachmentLength = (2 * 1024);

    private static boolean autoCompact = true;

    // Default value for maxRevTreeDepth, the max rev depth to preserve in a prune operation
    private static int DEFAULT_MAX_REVS = 20;

    private Store store = null;
    private final String path;
    private String name;
    private final AtomicBoolean open = new AtomicBoolean(false);

    private Map<String, View> views;
    private Map<String, String> viewDocTypes;
    private Map<String, ReplicationFilter> filters;
    private Map<String, Validator> validations;

    // Note: Why the pending attachment is not removed from the pendingAttachmentsByDigest
    // After a document is saved the items for its attachments could be removed
    // from _pendingAttachmentsByDigest … except in the case
    // where two docs being added have the same attachment.
    // The values in the dictionary are just 20 bytes long, so it’s not going to cause trouble
    // unless there are enormously many of them.
    private Map<String, Object> pendingAttachmentsByDigest;

    private final Set<Replication> activeReplicators;
    private final Set<Replication> allReplicators;

    private BlobStore attachments;
    private final Manager manager;
    private final Set<ChangeListener> changeListeners;
    private final Set<DatabaseListener> databaseListeners;
    private final Cache<String, Document> docCache;
    private final List<DocumentChange> changesToNotify;
    private boolean postingChangeNotifications;
    private final Object lockPostingChangeNotifications = new Object();
    private final long startTime;
    private Timer purgeTimer;
    private final Object lockViews = new Object();

    /**
     * Each database can have an associated PersistentCookieStore,
     * where the persistent cookie store uses the database to store
     * its cookies.
     * <p/>
     * There are two reasons this has been made an instance variable
     * of the Database, rather than of the Replication:
     * <p/>
     * - The PersistentCookieStore needs to span multiple replications.
     * For example, if there is a "push" and a "pull" replication for
     * the same DB, they should share a cookie store.
     * <p/>
     * - PersistentCookieStore lifecycle should be tied to the Database
     * lifecycle, since it needs to cease to exist if the underlying
     * Database ceases to exist.
     * <p/>
     * REF: https://github.com/couchbase/couchbase-lite-android/issues/269
     */
    private PersistentCookieJar persistentCookieStore;

    /**
     * Constructor
     */
    @InterfaceAudience.Private

    @SuppressWarnings("unchecked")
    public Database(String path, String name, Manager manager, boolean readOnly) {
        assert (new File(path).isAbsolute()); //path must be absolute
        this.path = path;
        this.name = name != null ? name : FileDirUtils.getDatabaseNameFromPath(path);
        this.manager = manager;
        this.startTime = System.currentTimeMillis();
        this.changeListeners = new CopyOnWriteArraySet<ChangeListener>();
        this.databaseListeners = Collections.synchronizedSet(new HashSet<DatabaseListener>());
        this.docCache = new Cache<String, Document>();
        this.changesToNotify = Collections.synchronizedList(new ArrayList<DocumentChange>());
        this.activeReplicators = Collections.synchronizedSet(new HashSet());
        this.allReplicators = Collections.synchronizedSet(new HashSet());
        this.postingChangeNotifications = false;
        this.pendingAttachmentsByDigest = new HashMap<String, Object>();
    }

    ///////////////////////////////////////////////////////////////////////////
    // APIs
    // https://github.com/couchbaselabs/couchbase-lite-api/blob/master/gen/md/Database.md
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Constants
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Class Members - Properties
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns the currently registered filter compiler (nil by default).
     */
    @InterfaceAudience.Public
    private static ReplicationFilterCompiler getFilterCompiler() {
        return filterCompiler;
    }

    /**
     * Registers an object that can compile source code into executable filter blocks.
     */
    @InterfaceAudience.Public
    public static void setFilterCompiler(ReplicationFilterCompiler filterCompiler) {
        Database.filterCompiler = filterCompiler;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Instance Members - Properties
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get all the replicators associated with this database.
     */
    @InterfaceAudience.Public
    public List<Replication> getAllReplications() {
        List<Replication> allReplicatorsList = new ArrayList<Replication>();
        synchronized (allReplicators) {
            allReplicatorsList.addAll(allReplicators);
        }
        return allReplicatorsList;
    }

    /**
     * The number of documents in the database.
     */
    @InterfaceAudience.Public
    int getDocumentCount() {
        return store.getDocumentCount();
    }

    /**
     * The latest sequence number used.  Every new revision is assigned a new sequence number,
     * so this property increases monotonically as changes are made to the database. It can be
     * used to check whether the database has changed between two points in time.
     */
    @InterfaceAudience.Public
    long getLastSequenceNumber() {
        return store.getLastSequence();
    }

    /**
     * The database manager that owns this database.
     */
    @InterfaceAudience.Public
    public Manager getManager() {
        return manager;
    }

    /**
     * Get the database's name.
     */
    @InterfaceAudience.Public
    public String getName() {
        return name;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Instance Members - Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Adds a Database change delegate that will be called whenever a Document
     * within the Database changes.
     */
    @InterfaceAudience.Public
    void addChangeListener(Database.ChangeListener listener) {
        changeListeners.add(listener);
    }

    /**
     * Compacts the database file by purging non-current JSON bodies, pruning revisions older than
     * the maxRevTreeDepth, deleting unused attachment files, and vacuuming the SQLite database.
     */
    @InterfaceAudience.Public
    public void compact() throws CouchbaseLiteException {
        store.compact();
        garbageCollectAttachments();
    }

    /**
     * Changes the database's encryption key, or removes encryption if the new key is null.
     * <p/>
     * To use this API, the database storage engine must support encryption, and the
     * ManagerOptions.EnableStorageEncryption property must be set to true.
     *
     * @param newKeyOrPassword The encryption key in the form of an String (a password) or an
     *                         byte[] object exactly 32 bytes in length (a raw AES key.)
     *                         If a string is given, it will be internally converted to a raw key
     *                         using 64,000 rounds of PBKDF2 hashing.
     *                         A null value is legal, and clears a previously-registered key.
     * @throws CouchbaseLiteException
     */
    @InterfaceAudience.Public
    public void changeEncryptionKey(final Object newKeyOrPassword) throws CouchbaseLiteException {
        if (!(store instanceof EncryptableStore))
            throw new CouchbaseLiteException(Status.NOT_IMPLEMENTED);

        SymmetricKey newKey = null;
        if (newKeyOrPassword != null)
            newKey = createSymmetricKey(newKeyOrPassword);

        try {
            Action action = ((EncryptableStore) store).actionToChangeEncryptionKey(newKey);
            action.add(attachments.actionToChangeEncryptionKey(newKey));
            action.add(new ActionBlock() {
                @Override
                public void execute() throws ActionException {
                    manager.registerEncryptionKey(newKeyOrPassword, name);
                }
            }, null, null);
            action.run();
        } catch (ActionException e) {
            throw new CouchbaseLiteException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns a query that matches all documents in the database.
     * This is like querying an imaginary view that emits every document's ID as a key.
     */
    @InterfaceAudience.Public
    public Query createAllDocumentsQuery() {
        return new Query(this, (View) null);
    }

    /**
     * Creates a new Document object with no properties and a new (random) UUID.
     * The document will be saved to the database when you call -createRevision: on it.
     */
    @InterfaceAudience.Public
    Document createDocument() {
        return getDocument(Misc.CreateUUID());
    }

    /**
     * Creates a new Replication that will pull from the source Database at the given url.
     *
     * @param remote the remote URL to pull from
     * @return A new Replication that will pull from the source Database at the given url.
     */
    @InterfaceAudience.Public
    public Replication createPullReplication(URL remote) {
        return new Replication(this, remote, Replication.Direction.PULL, null);

    }

    /**
     * Creates a new Replication that will push to the target Database at the given url.
     *
     * @param remote the remote URL to push to
     * @return A new Replication that will push to the target Database at the given url.
     */
    @InterfaceAudience.Public
    public Replication createPushReplication(URL remote) {
        return new Replication(this, remote, Replication.Direction.PUSH, null);
    }

    /**
     * Deletes the database.
     */
    @InterfaceAudience.Public
    public void delete() throws CouchbaseLiteException {
        if (open.get()) {
            if (!close()) {
                throw new CouchbaseLiteException("The database was open, and could not be closed",
                        Status.INTERNAL_SERVER_ERROR);
            }
        }
        manager.forgetDatabase(this);
        if (!exists()) {
            return;
        }

        File dir = new File(path);
        if (!FileDirUtils.deleteRecursive(dir))
            throw new CouchbaseLiteException("Was not able to delete the database directory",
                    Status.INTERNAL_SERVER_ERROR);

    }

    /**
     * Deletes the local document with the given ID.
     */
    @InterfaceAudience.Public
    boolean deleteLocalDocument(String localDocID) throws CouchbaseLiteException {
        return putLocalDocument(localDocID, null);
    }

    /**
     * Instantiates a Document object with the given ID.
     * Doesn't touch the on-disk sqliteDb; a document with that ID doesn't
     * even need to exist yet. CBLDocuments are cached, so there will
     * never be more than one instance (in this sqliteDb) at a time with
     * the same documentID.
     * NOTE: the caching described above is not implemented yet
     */
    @InterfaceAudience.Public
    public Document getDocument(String documentId) {
        if (documentId == null || documentId.length() == 0) {
            return null;
        }
        Document doc = docCache.get(documentId);
        if (doc == null) {
            doc = new Document(this, documentId);
            if (doc == null) {
                return null;
            }
            docCache.put(documentId, doc);
        }
        return doc;
    }

    /**
     * Gets the Document with the given id, or null if it does not exist.
     */
    @InterfaceAudience.Public
    public Document getExistingDocument(String docID) {
        // TODO: Needs to review this implementation
        if (docID == null || docID.length() == 0) {
            return null;
        }
        RevisionInternal revisionInternal = getDocument(docID, null, true);
        if (revisionInternal == null) {
            return null;
        }
        return getDocument(docID);
    }

    /**
     * Returns the contents of the local document with the given ID, or nil if none exists.
     */
    @InterfaceAudience.Public
    Map<String, Object> getExistingLocalDocument(String documentId) {
        RevisionInternal revInt = getLocalDocument(makeLocalDocumentId(documentId), null);
        if (revInt == null) {
            return null;
        }
        return revInt.getProperties();
    }

    /**
     * Returns the existing View with the given name, or nil if none.
     */
    @InterfaceAudience.Public
    View getExistingView(String name) {
        synchronized (lockViews) {
            View view = views != null ? views.get(name) : null;
            if (view != null)
                return view;

            try {
                return registerView(new View(this, name, false));
            } catch (CouchbaseLiteException e) {
                // View is not exist.
                return null;
            }
        }
    }

    /**
     * Returns the existing filter function (block) registered with the given name.
     * Note that filters are not persistent -- you have to re-register them on every launch.
     */
    @InterfaceAudience.Public
    public ReplicationFilter getFilter(String filterName) {
        ReplicationFilter result = null;
        if (filters != null) {
            result = filters.get(filterName);
        }
        if (result == null) {
            ReplicationFilterCompiler filterCompiler = getFilterCompiler();
            if (filterCompiler == null) {
                return null;
            }

            List<String> outLanguageList = new ArrayList<String>();
            String sourceCode = getDesignDocFunction(filterName, "filters", outLanguageList);
            if (sourceCode == null) {
                return null;
            }
            String language = outLanguageList.get(0);
            ReplicationFilter filter = filterCompiler.compileFilterFunction(sourceCode, language);
            if (filter == null) {
                Log.w(Database.TAG, "Filter %s failed to compile", filterName);
                return null;
            }
            setFilter(filterName, filter);
            return filter;
        }
        return result;
    }

    /**
     * Returns the existing document validation function (block) registered with the given name.
     * Note that validations are not persistent -- you have to re-register them on every launch.
     */
    @InterfaceAudience.Public
    private Validator getValidation(String name) {
        Validator result = null;
        if (validations != null) {
            result = validations.get(name);
        }
        return result;
    }

    /**
     * Returns a View object for the view with the given name.
     * (This succeeds even if the view doesn't already exist, but the view won't be added to
     * the database until the View is assigned a map function.)
     */
    @InterfaceAudience.Public
    public View getView(String name) {
        synchronized (lockViews) {
            View view = null;
            if (views != null) {
                view = views.get(name);
            }
            if (view != null) {
                return view;
            }
            try {
                return registerView(new View(this, name, true));
            } catch (CouchbaseLiteException e) {
                Log.w(TAG, "Error in registerView: error=" + e.getLocalizedMessage(), e);
                return null;
            }
        }
    }

    /**
     * Sets the contents of the local document with the given ID. Unlike CouchDB, no revision-ID
     * checking is done; the put always succeeds. If the properties dictionary is nil, the document
     * will be deleted.
     */
    @InterfaceAudience.Public
    boolean putLocalDocument(String localDocID, Map<String, Object> properties)
            throws CouchbaseLiteException {
        localDocID = makeLocalDocumentId(localDocID);
        RevisionInternal rev = new RevisionInternal(localDocID, null, properties == null);
        if (properties != null)
            rev.setProperties(properties);
        return store.putLocalRevision(rev, null, false) != null;
    }

    /**
     * Removes the specified delegate as a listener for the Database change event.
     */
    @InterfaceAudience.Public
    void removeChangeListener(Database.ChangeListener listener) {
        changeListeners.remove(listener);
    }

    /**
     * Runs the delegate asynchronously.
     */
    @InterfaceAudience.Public
    Future runAsync(final AsyncTask asyncTask) {
        return getManager().runAsync(new Runnable() {
            @Override
            public void run() {
                asyncTask.run(Database.this);
            }
        });
    }

    /**
     * Runs the block within a transaction. If the block returns NO, the transaction is rolled back.
     * Use this when performing bulk write operations like multiple inserts/updates;
     * it saves the overhead of multiple SQLite commits, greatly improving performance.
     * <p/>
     * Does not commit the transaction if the code throws an Exception.
     * <p/>
     * TODO: the iOS version has a retry loop, so there should be one here too
     */
    @InterfaceAudience.Public
    boolean runInTransaction(TransactionalTask task) {
        return store.runInTransaction(task);
    }

    /**
     * Define or clear a named filter function.
     * <p/>
     * Filters are used by push replications to choose which documents to send.
     */
    @InterfaceAudience.Public
    private void setFilter(String filterName, ReplicationFilter filter) {
        if (filters == null) {
            filters = new HashMap<String, ReplicationFilter>();
        }
        if (filter != null) {
            filters.put(filterName, filter);
        } else {
            filters.remove(filterName);
        }
    }

    /**
     * Defines or clears a named document validation function.
     * Before any change to the database, all registered validation functions are called and given
     * a chance to reject it. (This includes incoming changes from a pull replication.)
     */
    @InterfaceAudience.Public
    public void setValidation(String name, Validator validator) {
        if (validations == null) {
            validations = new HashMap<String, Validator>();
        }
        if (validator != null) {
            validations.put(name, validator);
        } else {
            validations.remove(name);
        }
    }

    /**
     * Set the maximum depth of a document's revision tree (or, max length of its revision history.)
     * Revisions older than this limit will be deleted during a -compact: operation.
     * Smaller values save space, at the expense of making document conflicts somewhat more likely.
     */
    @InterfaceAudience.Public
    public void setMaxRevTreeDepth(int maxRevTreeDepth) {
        if (store != null)
            store.setMaxRevTreeDepth(maxRevTreeDepth);
    }

    /**
     * Get the maximum depth of a document's revision tree (or, max length of its revision history.)
     * Revisions older than this limit will be deleted during a -compact: operation.
     * Smaller values save space, at the expense of making document conflicts somewhat more likely.
     */
    @InterfaceAudience.Public
    public int getMaxRevTreeDepth() {
        return store.getMaxRevTreeDepth();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Events
    ///////////////////////////////////////////////////////////////////////////

    /**
     * The type of event raised when a Database changes.
     */
    @InterfaceAudience.Public
    static class ChangeEvent {
        private Database source;
        private boolean isExternal;
        private List<DocumentChange> changes;

        ChangeEvent(Database source, boolean isExternal, List<DocumentChange> changes) {
            this.source = source;
            this.isExternal = isExternal;
            this.changes = changes;
        }

        public Database getSource() {
            return source;
        }

        public boolean isExternal() {
            return isExternal;
        }

        public List<DocumentChange> getChanges() {
            return changes;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Delegates
    ///////////////////////////////////////////////////////////////////////////

    /**
     * A delegate that can be used to listen for Database changes.
     */
    @InterfaceAudience.Public
    interface ChangeListener {
        void changed(Database.ChangeEvent event);
    }

    // ReplicationFilterCompiler -> ReplicationFilterCompiler.java

    // ReplicationFilter -> ReplicationFilter.java

    // AsyncTask -> AsyncTask.java

    // TransactionalTask -> TransactionalTask.java

    // Validator -> Validator.java

    ///////////////////////////////////////////////////////////////////////////
    // End of APIs
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    // Override Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns a string representation of this database.
     */
    @InterfaceAudience.Public
    public String toString() {
        return this.getClass().getName() + "@" + Integer.toHexString(hashCode()) + '[' + path + ']';
    }

    ///////////////////////////////////////////////////////////////////////////
    // Implementation of StorageDelegate
    ///////////////////////////////////////////////////////////////////////////

    /**
     * in CBLDatabase+Internal.m
     * - (void) storageExitedTransaction: (BOOL)committed
     */
    @InterfaceAudience.Private
    public void storageExitedTransaction(boolean committed) {
        if (!committed) {
            // I already told cached CBLDocuments about these new revisions. Back that out:
            synchronized (changesToNotify) {
                for (DocumentChange change : changesToNotify) {
                    Document doc = cachedDocumentWithID(change.getDocumentId());
                    if (doc != null)
                        doc.forgetCurrentRevision();
                }
                changesToNotify.clear();
            }
        }
        postChangeNotifications();
    }

    /**
     * in CBLDatabase+Internal.m
     * - (void) databaseStorageChanged:(CBLDatabaseChange *)change
     */
    @InterfaceAudience.Private
    public void databaseStorageChanged(DocumentChange change) {
        if (change.getRevisionId() != null)
            Log.v(Log.TAG, "---> Added: %s as seq %d",
                    change.getAddedRevision(), change.getAddedRevision().getSequence());
        else
            Log.v(Log.TAG, "---> Purged: docID=%s", change.getDocumentId());

        changesToNotify.add(change);
        if (!postChangeNotifications()) {
            // The notification wasn't posted yet, probably because a transaction is open.
            // But the CBLDocument, if any, needs to know right away so it can update its
            // currentRevision.

            Document doc = cachedDocumentWithID(change.getDocumentId());
            if (doc != null) {
                doc.revisionAdded(change, false);
            }
        }

        // Squish the change objects if too many of them are piling up:
        if (changesToNotify.size() >= MANY_CHANGES_TO_NOTIFY) {
            if (changesToNotify.size() == MANY_CHANGES_TO_NOTIFY) {
                synchronized (changesToNotify) {
                    for (DocumentChange c : changesToNotify)
                        c.reduceMemoryUsage();
                }
            } else {
                change.reduceMemoryUsage();
            }
        }
    }

    /**
     * Generates a revision ID for a new revision.
     *
     * @param json      The canonical JSON of the revision (with metadata properties removed.)
     * @param deleted   YES if this revision is a deletion
     * @param prevRevID The parent's revision ID, or nil if this is a new document.
     */
    @InterfaceAudience.Private
    public String generateRevID(byte[] json, boolean deleted, String prevRevID) {
        return RevisionUtils.generateRevID(json, deleted, prevRevID);
    }

    @InterfaceAudience.Private
    BlobStore getAttachmentStore() {
        return attachments;
    }

    private void validateRevision(RevisionInternal newRev,
                                  RevisionInternal oldRev,
                                  String parentRevID)
            throws CouchbaseLiteException {
        if (validations == null || validations.size() == 0) {
            return;
        }

        SavedRevision publicRev = new SavedRevision(this, newRev, parentRevID);
        publicRev.setParentRevisionID(parentRevID);

        ValidationContextImpl context = new ValidationContextImpl(this, oldRev, newRev);

        for (String validationName : validations.keySet()) {
            Validator validation = getValidation(validationName);
            validation.validate(publicRev, context);
            if (context.getRejectMessage() != null) {
                throw new CouchbaseLiteException(context.getRejectMessage(), Status.FORBIDDEN);
            }
        }
    }

    // #pragma mark - UPDATING _attachments DICTS:

    /* package */ boolean registerAttachmentBodies(final Map<String, Object> attachments,
                                                   RevisionInternal rev,
                                                   final Status outStatus)
            throws CouchbaseLiteException {
        outStatus.setCode(Status.OK);
        rev.mutateAttachments(new Functor<Map<String, Object>, Map<String, Object>>() {
            public Map<String, Object> invoke(Map<String, Object> meta) {
                String name = (String) meta.get("name");
                Object value = attachments.get(name);
                if (value != null) {
                    byte[] data = null;
                    if ((value instanceof byte[])) {
                        data = (byte[]) value;
                    } else {
                        if (value instanceof URL) {
                            URL url = (URL) value;
                            if ("file".equalsIgnoreCase(url.getProtocol())) {
                                try {
                                    data = IOUtils.toByteArray(url);
                                } catch (IOException e) {
                                    Log.w(TAG, "attachments[\"%s\"] is unable to load", e, name);
                                }
                            } else {
                                Log.w(TAG, "attachments[\"%s\"] is neither byte[] nor file URL", name);
                            }
                        }
                        if (data == null) {
                            outStatus.setCode(Status.BAD_ATTACHMENT);
                            return null;
                        }
                    }

                    // Register attachment body with database:
                    BlobStoreWriter writer = getAttachmentWriter();
                    try {
                        writer.appendData(data);
                        writer.finish();
                    } catch (Exception e) {
                        Log.w(TAG, "failed to write attachment data name: %s", e, name);
                        outStatus.setCode(Status.BAD_ATTACHMENT);
                        return null;
                    }

                    // Make attachment mode "follows", indicating the data is registered:
                    Map<String, Object> nuMeta = new HashMap<String, Object>(meta);
                    nuMeta.remove("data");
                    nuMeta.remove("stub");
                    nuMeta.put("follows", true);

                    // Add or verify metadata "digest" property:
                    String digest = (String) meta.get("digest");
                    String sha1Digest = writer.sHA1DigestString();
                    if (digest != null) {
                        if (!digest.equals(sha1Digest) && !digest.equals(writer.mD5DigestString())) {
                            Log.w(TAG,
                                    "Attachment '%s' body digest (%s) doesn't match 'digest' property %s",
                                    name, sha1Digest, digest);
                            outStatus.setCode(Status.BAD_ATTACHMENT);
                            return null;
                        }
                    } else {
                        digest = sha1Digest;
                        nuMeta.put("digest", sha1Digest);
                    }
                    rememberAttachmentWriter(writer, digest);
                    return nuMeta;
                }
                return meta;
            }
        });
        return outStatus.getCode() == Status.OK;
    }

    private static long smallestLength(Map<String, Object> attachment) {
        long length = 0;
        Number explicitLength = (Number) attachment.get("length");
        if (explicitLength != null)
            length = explicitLength.longValue();
        explicitLength = (Number) attachment.get("encoded_length");
        if (explicitLength != null)
            length = explicitLength.longValue();
        return length;
    }

    /**
     * Modifies a CBL_Revision's _attachments dictionary by adding the "data" property to all
     * attachments (and removing "stub" and "follows".) GZip-encoded attachments will be unzipped
     * unless options contains the flag kCBLLeaveAttachmentsEncoded.
     *
     * @param rev               The revision to operate on. Its _attachments property may be altered.
     * @param minRevPos         Attachments with a "revpos" less than this will remain stubs.
     * @param allowFollows      If YES, non-small attachments will get a "follows" key instead of data.
     * @param decodeAttachments If YES, attachments with "encoding" properties will be decoded.
     * @param outStatus         On failure, will be set to the error status.
     * @return YES on success, NO on failure.
     */
    @InterfaceAudience.Private
    boolean expandAttachments(final RevisionInternal rev,
                              final int minRevPos,
                              final boolean allowFollows,
                              final boolean decodeAttachments,
                              final Status outStatus) {
        outStatus.setCode(Status.OK);

        rev.mutateAttachments(new Functor<Map<String, Object>, Map<String, Object>>() {
            public Map<String, Object> invoke(Map<String, Object> attachment) {

                String name = (String) attachment.get("name");

                int revPos = (Integer) attachment.get("revpos");
                if (revPos < minRevPos && revPos != 0) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("stub", true);
                    map.put("revpos", revPos);
                    return map;
                } else {
                    Map<String, Object> expanded = new HashMap<String, Object>();
                    expanded.putAll(attachment);
                    expanded.remove("stub");
                    if (decodeAttachments) {
                        expanded.remove("encoding");
                        expanded.remove("encoded_length");
                    }

                    if (allowFollows && smallestLength(expanded) >= kBigAttachmentLength) {
                        // Data will follow (multipart):
                        expanded.put("follows", true);
                        expanded.remove("data");
                    } else {
                        // Put data inline:
                        expanded.remove("follows");
                        AttachmentInternal attachObj = null;
                        try {
                            attachObj = getAttachment(attachment, name);
                        } catch (CouchbaseLiteException e) {
                            outStatus.setCode(e.getCBLStatus().getCode());
                        }

                        if (attachObj == null) {
                            Log.w(TAG, "Can't get attachment '%s' of %s (status %d)",
                                    name, rev, outStatus.getCode());
                            return attachment;
                        }
                        byte[] data = decodeAttachments ? attachObj.getContent() :
                                attachObj.getEncodedContent();
                        if (data == null) {
                            Log.w(TAG, "Can't get binary data of attachment '%s' of %s", name, rev);
                            outStatus.setCode(Status.NOT_FOUND);
                            return attachment;
                        }
                        expanded.put("data", Base64.encodeBytes(data));
                    }
                    return expanded;
                }
            }
        });

        return outStatus.getCode() == Status.OK;
    }

    /**
     * Scans the rev's _attachments dictionary, adding inline attachment data to the blob-store
     * and turning all the attachments into stubs.
     * <p/>
     * in CBLDatabase+Attachments.m
     * - (BOOL) processAttachmentsForRevision: (CBL_MutableRevision*)rev
     * ancestry: (NSArray*)ancestry
     * status: (CBLStatus*)outStatus
     */

    @SuppressWarnings("unchecked")
    private boolean processAttachmentsForRevision(final RevisionInternal rev,
                                                  final List<String> ancestry,
                                                  final Status outStatus) {
        outStatus.setCode(Status.OK);

        Map<String, Object> revAttachments = rev.getAttachments();
        if (revAttachments == null)
            return true;  // no-op: no attachments

        // Deletions can't have attachments:
        if (rev.isDeleted() || revAttachments.size() == 0) {
            Map<String, Object> body = rev.getProperties();
            body.remove("_attachments");
            rev.setProperties(body);
            return true;
        }

        final String prevRevID = (ancestry.size() > 0) ? ancestry.get(0) : null;
        final int generation = Revision.generationFromRevID(prevRevID) + 1;
        final Map<String, Object> parentAttachments = new HashMap<String, Object>();

        rev.mutateAttachments(new Functor<Map<String, Object>, Map<String, Object>>() {
            public Map<String, Object> invoke(Map<String, Object> attachInfo) {

                String name = (String) attachInfo.get("name");

                AttachmentInternal attachment;
                try {
                    attachment = new AttachmentInternal(name, attachInfo);
                } catch (CouchbaseLiteException e) {
                    return null;
                }
                if (attachment == null) {
                    return null;
                } else if (attachment.getEncodedContent() != null) {
                    // If there's inline attachment data, decode and store it:
                    BlobKey blobKey = new BlobKey();
                    if (!attachments.storeBlob(attachment.getEncodedContent(), blobKey)) {
                        outStatus.setCode(Status.ATTACHMENT_ERROR);
                        return null;
                    }
                    attachment.setBlobKey(blobKey);
                } else if (attachInfo.containsKey("follows") &&
                        ((Boolean) attachInfo.get("follows")).booleanValue()) {
                    // "follows" means the uploader provided the attachment in a separate MIME part.
                    // This means it's already been registered in _pendingAttachmentsByDigest;
                    // I just need to look it up by its "digest" property and install it
                    // into the store:
                    try {
                        installAttachment(attachment);
                    } catch (CouchbaseLiteException e) {
                        outStatus.setCode(e.getCBLStatus().getCode());
                        return null;
                    }
                } else if (attachInfo.containsKey("stub") &&
                        ((Boolean) attachInfo.get("stub")).booleanValue()) {
                    // "stub" on an incoming revision means the attachment is the same as in the parent.
                    if (parentAttachments.isEmpty() && prevRevID != null) {
                        Map<String, Object> _parentAttachments = getAttachments(rev.getDocID(),
                                prevRevID);
                        if (_parentAttachments == null || _parentAttachments.isEmpty()) {
                            if (attachments.hasBlobForKey(attachment.getBlobKey())) {
                                // Parent revision's body isn't known (we are probably pulling
                                // a rev along with its entire history) but it's OK, we have
                                // the attachment already
                                outStatus.setCode(Status.OK);
                                return attachInfo;
                            }
                            // If parent rev isn't available, look farther back in ancestry:
                            Map<String, Object> ancestorAttachment = findAttachment(
                                    name, attachment.getRevpos(), rev.getDocID(), ancestry);
                            if (ancestorAttachment != null)
                                return ancestorAttachment;
                            outStatus.setCode(Status.BAD_ATTACHMENT);
                            return null;
                        } else {
                            parentAttachments.putAll(_parentAttachments);
                        }
                    }

                    Map<String, Object> parentAttachment = (Map<String, Object>)
                            parentAttachments.get(name);
                    if (parentAttachment == null) {
                        outStatus.setCode(Status.BAD_ATTACHMENT);
                        return null;
                    }
                    return parentAttachment;
                }

                // Set or validate the revpos:
                if (attachment.getRevpos() == 0) {
                    attachment.setRevpos(generation);
                } else if (attachment.getRevpos() > generation) {
                    outStatus.setCode(Status.BAD_ATTACHMENT);
                    return null;
                }
                assert (attachment.isValid());
                return attachment.asStubDictionary();
            }
        });

        return !outStatus.isError();
    }

    /**
     * Looks for an attachment with the given revpos in the document's ancestry.
     * in CBLDatabase+Attachments.m
     * - (NSDictionary*) findAttachment: (NSString*)name
     * revpos: (unsigned)revpos
     * docID: (NSString*)docID
     * ancestry: (NSArray*)ancestry
     */

    @SuppressWarnings("unchecked")
    private Map<String, Object> findAttachment(String name,
                                               long revpos,
                                               String docID,
                                               List<String> ancestry) {
        for (int i = ancestry.size() - 1; i >= 0; i--) {
            String revID = ancestry.get(i);
            if (Revision.generationFromRevID(revID) >= revpos) {
                Map<String, Object> attachments = getAttachments(docID, revID);
                if (attachments != null && attachments.containsKey(name)) {
                    return (Map<String, Object>) attachments.get(name);
                }
            }
        }
        return null;
    }

    @InterfaceAudience.Private
    public boolean runFilter(ReplicationFilter filter,
                             Map<String, Object> filterParams,
                             RevisionInternal rev) {
        if (filter == null) {
            return true;
        }
        SavedRevision publicRev = new SavedRevision(this, rev);
        return filter.filter(publicRev, filterParams);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public but Not API
    ///////////////////////////////////////////////////////////////////////////

    @InterfaceAudience.Private
    public static void setAutoCompact(boolean autoCompact) {
        Database.autoCompact = autoCompact;
    }

    @InterfaceAudience.Private
    interface DatabaseListener {
        void databaseClosing();
    }

    // NOTE: router-only
    @InterfaceAudience.Private
    public void addDatabaseListener(DatabaseListener listener) {
        databaseListeners.add(listener);
    }

    // NOTE: router-only
    @InterfaceAudience.Private
    public void removeDatabaseListener(DatabaseListener listener) {
        databaseListeners.remove(listener);
    }

    /**
     * Get all the active replicators associated with this database.
     */
    @InterfaceAudience.Private
    public List<Replication> getActiveReplications() {
        List<Replication> replicators = new ArrayList<Replication>();
        synchronized (activeReplicators) {
            replicators.addAll(activeReplicators);
        }
        return replicators;
    }

    @InterfaceAudience.Private
    public boolean exists() {
        return new File(path).exists();
    }

    @InterfaceAudience.Private
    private Store createStoreInstance(String storageType) {
        String className = getStoreClassName(storageType);


        try {


            Class<?> clazz = Class.forName(className);


            Constructor<?> ctor = clazz.getDeclaredConstructor(
                    String.class, Manager.class, StoreDelegate.class);

            return (Store) ctor.newInstance(path, manager, this);
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "No '%s' class found for the storage type '%s'", className, storageType);
        } catch (Exception e) {
            Log.e(TAG, "Cannot create a Store instance of class : %s for the storage type '%s'",
                    e, className, storageType);
        }
        return null;
    }

    @InterfaceAudience.Private
    private static String getStoreClassName(String storageType) {
        if (storageType == null) storageType = DEFAULT_STORAGE;
        if (storageType.equals(Manager.SQLITE_STORAGE))
            return SQLITE_STORE_CLASS;
        else if (storageType.equals(Manager.FORESTDB_STORAGE))
            return FORESTDB_STORE_CLASS;
        else {
            Log.e(Database.TAG, "Invalid storage type: " + storageType);
            return null;
        }
    }

    @InterfaceAudience.Private
    public synchronized void open() throws CouchbaseLiteException {
        open(manager.getDefaultOptions(name));
    }

    @InterfaceAudience.Private
    public synchronized void open(DatabaseOptions options) throws CouchbaseLiteException {
        if (open.get())
            return;

        Log.v(TAG, "Opening %s", this);

        // Create the database directory:
        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs())
                throw new CouchbaseLiteException("Cannot create database directory",
                        Status.INTERNAL_SERVER_ERROR);
        } else if (!dir.isDirectory())
            throw new CouchbaseLiteException("Database directory is not directory",
                    Status.INTERNAL_SERVER_ERROR);

        String storageType = options.getStorageType();
        if (storageType == null) {
            storageType = manager.getStorageType();
            if (storageType == null)
                storageType = DEFAULT_STORAGE;
        }

        Store primaryStore = createStoreInstance(storageType);
        if (primaryStore == null) {
            if (storageType.equals(Manager.SQLITE_STORAGE) || storageType.equals(Manager.FORESTDB_STORAGE))
                Log.w(TAG, "storageType is '%s' but no class implementation found", storageType);
            throw new CouchbaseLiteException("Can't open database in that storage format",
                    Status.INVALID_STORAGE_TYPE);
        }

        boolean primarySQLite = Manager.SQLITE_STORAGE.equals(storageType);
        Store otherStore = createStoreInstance(primarySQLite ? Manager.FORESTDB_STORAGE : Manager.SQLITE_STORAGE);

        boolean upgrade = false;
        if (options.getStorageType() != null) {
            // If explicit storage type given in options, always use primary storage type,
            // and if secondary db exists, try to upgrade from it:
            if (otherStore != null && otherStore.databaseExists(path) && !primaryStore.databaseExists(path))
                upgrade = true;

            if (upgrade && primarySQLite)
                throw new CouchbaseLiteException("Cannot upgrade to SQLite Storage", Status.INVALID_STORAGE_TYPE);
        } else {
            // If options don't specify, use primary unless secondary db already exists in dir:
            if (otherStore != null && otherStore.databaseExists(path))
                primaryStore = otherStore;
        }

        store = primaryStore;
        store.setAutoCompact(autoCompact);

        // Set encryption key:
        SymmetricKey encryptionKey = null;
        if (store instanceof EncryptableStore) {
            Object keyOrPassword = options.getEncryptionKey();
            if (keyOrPassword != null) {
                encryptionKey = createSymmetricKey(keyOrPassword);
                ((EncryptableStore) store).setEncryptionKey(encryptionKey);
            }
        }

        store.open();

        // First-time setup:
        if (privateUUID() == null) {
            if (store.setInfo("privateUUID", Misc.CreateUUID()) != Status.OK)
                throw new CouchbaseLiteException("Unable to set privateUUID in info", Status.DB_ERROR);
            if (store.setInfo("publicUUID", Misc.CreateUUID()) != Status.OK)
                throw new CouchbaseLiteException("Unable to set publicUUID in info", Status.DB_ERROR);
        }

        String sMaxRevs = store.getInfo("max_revs");
        int maxRevs = (sMaxRevs == null) ? DEFAULT_MAX_REVS : Integer.parseInt(sMaxRevs);
        store.setMaxRevTreeDepth(maxRevs);

        // NOTE: Migrate attachment directory path if necessary
        // https://github.com/couchbase/couchbase-lite-java-core/issues/604
        File obsoletedAttachmentStorePath = new File(getObsoletedAttachmentStorePath());
        if (obsoletedAttachmentStorePath != null &&
                obsoletedAttachmentStorePath.exists() &&
                obsoletedAttachmentStorePath.isDirectory()) {
            File attachmentStorePath = new File(getAttachmentStorePath());
            if (attachmentStorePath != null && !attachmentStorePath.exists()) {
                boolean success = obsoletedAttachmentStorePath.renameTo(attachmentStorePath);
                if (!success) {
                    Log.e(Database.TAG, "Could not rename attachment store path");
                    store.close();
                    store = null;
                    throw new CouchbaseLiteException("Could not rename attachment store path",
                            Status.INTERNAL_SERVER_ERROR);
                }
            }
        }

        // NOTE: obsoleted directory is /files/<database name>/attachments/xxxx
        //       Needs to delete /files/<database name>/ too
        File obsoletedAttachmentStoreParentPath = new File(getObsoletedAttachmentStoreParentPath());
        if (obsoletedAttachmentStoreParentPath != null &&
                obsoletedAttachmentStoreParentPath.exists()) {
            obsoletedAttachmentStoreParentPath.delete();
        }

        try {
            if (isBlobstoreMigrated() || !manager.isAutoMigrateBlobStoreFilename()) {
                attachments = new BlobStore(manager.getContext(),
                        getAttachmentStorePath(), encryptionKey, false);
            } else {
                attachments = new BlobStore(manager.getContext(),
                        getAttachmentStorePath(), encryptionKey, true);
                markBlobstoreMigrated();
            }

        } catch (IllegalArgumentException e) {
            Log.e(Database.TAG, "Could not initialize attachment store", e);
            store.close();
            store = null;
            throw new CouchbaseLiteException("Could not initialize attachment store", e,
                    Status.INTERNAL_SERVER_ERROR);
        }

        open.set(true);

        if (upgrade) {
            Log.i(TAG, "Upgrading to %s ...", storageType);
            String dbPath = new File(path, "db.sqlite3").getAbsolutePath();
            DatabaseUpgrade upgrader = new DatabaseUpgrade(manager, this, dbPath);
            if (!upgrader.importData()) {
                Log.w(TAG, "Upgrade to %s failed", storageType);
                upgrader.backOut();
                close();
                throw new CouchbaseLiteException("Cannot upgrade to " + storageType, Status.DB_ERROR);
            } else {
                upgrader.deleteSQLiteFiles();
            }
        }

        scheduleDocumentExpiration(kHousekeepingDelayAfterOpening);
    }

    /**
     * Create a SymmetricKey object from the key (byte[32]) or password string.
     *
     * @param keyOrPassword
     * @return
     * @throws CouchbaseLiteException
     */
    @InterfaceAudience.Private
    SymmetricKey createSymmetricKey(Object keyOrPassword) throws CouchbaseLiteException {
        if (keyOrPassword == null)
            return null;

        byte[] rawKey = null;
        if (keyOrPassword instanceof String) {
            rawKey = ((EncryptableStore) store).derivePBKDF2SHA256Key(
                    (String) keyOrPassword,
                    DEFAULT_PBKDF2_KEY_SALT.getBytes(),
                    DEFAULT_PBKDF2_KEY_ROUNDS);
        } else if (keyOrPassword instanceof byte[]) {
            rawKey = (byte[]) keyOrPassword;
        } else {
            throw new CouchbaseLiteException("Key must be String or byte[" +
                    SymmetricKey.KEY_SIZE + ']', Status.BAD_REQUEST);
        }

        SymmetricKey symmetricKey = null;
        try {
            symmetricKey = new SymmetricKey(rawKey);
        } catch (SymmetricKeyException e) {
            throw new CouchbaseLiteException(e, Status.BAD_REQUEST);
        }
        return symmetricKey;
    }

    @InterfaceAudience.Public
    public boolean close() {
        if (!open.get()) {
            // Ensure that the database is forgotten:
            manager.forgetDatabase(this);
            return false;
        }

        synchronized (databaseListeners) {
            for (Database.DatabaseListener listener : databaseListeners)
                listener.databaseClosing();
        }

        if (views != null) {
            for (View view : views.values())
                view.close();
        }
        views = null;

        // Make all replicators stop and wait:
        boolean stopping = false;
        synchronized (activeReplicators) {
            for (Replication replicator : activeReplicators) {
                if (replicator.getStatus() == Replication.ReplicationStatus.REPLICATION_STOPPED)
                    continue;
                replicator.stop();
                stopping = true;
            }

            // maximum wait time per replicator is 60 sec.
            // total maximum wait time for all replicators is between 60sec and 119 sec.
            long timeout = Replication.DEFAULT_MAX_TIMEOUT_FOR_SHUTDOWN * 1000;
            long startTime = System.currentTimeMillis();
            while (activeReplicators.size() > 0 && stopping &&
                    (System.currentTimeMillis() - startTime) < timeout) {
                try {
                    activeReplicators.wait(timeout);
                } catch (InterruptedException e) {


                    e.printStackTrace();
                }
            }
            // clear active replicators:
            activeReplicators.clear();
        }

        // Clear all replicators:
        allReplicators.clear();

        // cancel purge timer
        cancelPurgeTimer();

        // Close Store:
        if (store != null)
            store.close();
        store = null;

        // Clear document cache:
        clearDocumentCache();

        // Forget database:
        manager.forgetDatabase(this);

        open.set(false);
        return true;
    }

    @InterfaceAudience.Private
    BlobStoreWriter getAttachmentWriter() {
        return new BlobStoreWriter(getAttachmentStore());
    }

    // NOTE: router-only
    @InterfaceAudience.Private
    public long totalDataSize() {
        long size = 0;
        for (File f : new File(path).listFiles())
            size += f.length();
        return size;
    }

    @InterfaceAudience.Private
    String privateUUID() {
        return store.getInfo("privateUUID");
    }

    @InterfaceAudience.Private
    String publicUUID() {
        return store.getInfo("publicUUID");
    }

    @InterfaceAudience.Private
    public RevisionInternal getDocument(String docID, String revID, boolean withBody) {
        return store == null ? null : store.getDocument(docID, revID, withBody);
    }

    @InterfaceAudience.Private
    RevisionInternal loadRevisionBody(RevisionInternal rev) throws CouchbaseLiteException {
        // NOTE: loadRevisionBoy() is thread safe. It is read operation to database as storage
        //       layer is thread-safe, and also not access to instance variables.
        return store.loadRevisionBody(rev);
    }

    /**
     * NOTE: This method is internal use only (from BulkDownloader and PullerInternal)
     */
    @InterfaceAudience.Private
    List<String> getPossibleAncestorRevisionIDs(RevisionInternal rev, int limit,
                                                AtomicBoolean hasAttachment) {
        return store.getPossibleAncestorRevisionIDs(rev, limit, hasAttachment);
    }

    /**
     * Returns the revision history as a _revisions dictionary, as returned
     * by the REST API's ?revs=true option.
     */
    @InterfaceAudience.Private
    Map<String, Object> getRevisionHistoryDictStartingFromAnyAncestor(RevisionInternal rev,
                                                                      List<String> ancestorRevIDs) {
        List<RevisionInternal> history = getRevisionHistory(rev);
        // (this is in reverse order, newest..oldest
        if (ancestorRevIDs != null && ancestorRevIDs.size() > 0 && history != null) {
            for (int i = 0; i < history.size(); ++i) {
                if (ancestorRevIDs.contains(history.get(i).getRevID())) {
                    history = history.subList(0, i + 1);
                    break;
                }
            }
        }
        return RevisionUtils.makeRevisionHistoryDict(history);
    }

    @InterfaceAudience.Private
    RevisionList changesSince(long lastSeq,
                              ChangesOptions options,
                              ReplicationFilter filter,
                              Map<String, Object> filterParams) {
        return store.changesSince(lastSeq, options, filter, filterParams);
    }

    @InterfaceAudience.Private
    RevisionList unpushedRevisionsSince(String sequence,
                                        ReplicationFilter filter,
                                        Map<String, Object> filterParams) {
        long longSequence = 0;
        if (sequence != null)
            longSequence = Long.parseLong(sequence);
        ChangesOptions options = new ChangesOptions();
        options.setIncludeConflicts(true);

        return changesSince(longSequence, options, filter, filterParams);
    }

    @InterfaceAudience.Private
    private Map<String, Object> getAllDocs(QueryOptions options) throws CouchbaseLiteException {

        // For regular all-docs, let storage do it all:
        if (options == null || options.getAllDocsMode() != Query.AllDocsMode.BY_SEQUENCE)
            return store.getAllDocs(options);

        // For changes feed mode (kCBLBySequence) do more work here:
        if (options.isDescending()) {
            throw new CouchbaseLiteException(Status.NOT_IMPLEMENTED); //FIX: Implement descending order
        }

        ChangesOptions changesOpts = new ChangesOptions(
                options.getLimit(),
                options.isIncludeDocs(),
                true, true);

        long startSeq = keyToSequence(options.getStartKey(), 1);
        long endSeq = keyToSequence(options.getEndKey(), Long.MAX_VALUE);
        if (!options.isInclusiveStart())
            ++startSeq;
        if (!options.isInclusiveEnd())
            --endSeq;
        long minSeq = startSeq;
        long maxSeq = endSeq;
        if (minSeq > maxSeq) {
            return null; // empty result
        }

        RevisionList revs = store.changesSince(minSeq - 1, changesOpts, null, null);
        if (revs == null)
            return null;

        Map<String, Object> result = new HashMap<String, Object>();
        List<QueryRow> rows = new ArrayList<QueryRow>();

        Predicate<QueryRow> filter = options.getPostFilter();

        // reverse order
        if (options.isDescending()) {
            for (int i = revs.size() - 1; i >= 0; i--) {
                QueryRow row = getQueryRow(revs.get(i), minSeq, maxSeq, options.getPostFilter());
                if (row != null)
                    rows.add(row);
            }
        } else {
            for (int i = 0; i < revs.size(); i++) {
                QueryRow row = getQueryRow(revs.get(i), minSeq, maxSeq, options.getPostFilter());
                if (row != null)
                    rows.add(row);
            }
        }
        result.put("rows", rows);
        result.put("total_rows", rows.size());
        result.put("offset", options.getSkip());
        return result;
    }

    private QueryRow getQueryRow(RevisionInternal rev, long minSeq, long maxSeq, Predicate<QueryRow> filter) {
        if (rev == null)
            return null;
        long seq = rev.getSequence();
        if (seq < minSeq || seq > maxSeq)
            return null;
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("rev", rev.getRevID());
        if (rev.isDeleted())
            value.put("deleted", (rev.isDeleted() ? true : null));
        QueryRow row = new QueryRow(rev.getDocID(), seq, rev.getDocID(), value, rev);
        if (filter == null) {
            return row;
        }
        row.setDatabase(this);
        if (filter.apply(row)) {
            return row;
        }
        return null;
    }

    AttachmentInternal getAttachment(Map info, String filename)
            throws CouchbaseLiteException {
        if (info == null)
            throw new CouchbaseLiteException(Status.NOT_FOUND);
        AttachmentInternal attachment = new AttachmentInternal(filename, info);
        attachment.setDatabase(this);
        return attachment;
    }

    @InterfaceAudience.Private
    URL fileForAttachmentDict(Map<String, Object> attachmentDict) {
        String digest = (String) attachmentDict.get("digest");
        if (digest == null) {
            return null;
        }
        String path;
        Object pending = pendingAttachmentsByDigest.get(digest);
        if (pending != null) {
            if (pending instanceof BlobStoreWriter) {
                path = ((BlobStoreWriter) pending).getFilePath();
            } else {
                BlobKey key = new BlobKey((byte[]) pending);
                path = attachments.getRawPathForKey(key);
            }
        } else {
            // If it's an installed attachment, ask the blob-store for it:
            BlobKey key = new BlobKey(digest);
            path = attachments.getRawPathForKey(key);
        }

        URL retval = null;
        try {
            retval = new File(path).toURI().toURL();
        } catch (MalformedURLException e) {
            //NOOP: retval will be null
        }
        return retval;
    }

    // Replaces the "follows" key with the real attachment data in all attachments to 'doc'.
    @InterfaceAudience.Private
    boolean inlineFollowingAttachmentsIn(RevisionInternal rev) {

        return rev.mutateAttachments(new CollectionUtils.Functor<Map<String, Object>, Map<String, Object>>() {
            public Map<String, Object> invoke(Map<String, Object> attachment) {
                if (!attachment.containsKey("follows")) {
                    return attachment;
                }

                byte[] fileData;
                try {
                    BlobStore blobStore = getAttachmentStore();
                    String base64Digest = (String) attachment.get("digest");
                    BlobKey blobKey = new BlobKey(base64Digest);
                    InputStream in = blobStore.blobStreamForKey(blobKey);
                    try {
                        fileData = IOUtils.toByteArray(in);
                    } finally {
                        in.close();
                    }
                } catch (IOException e) {
                    Log.e(Log.TAG_SYNC, "could not retrieve attachment data: %S", e);
                    return null;
                }

                Map<String, Object> editedAttachment = new HashMap<String, Object>(attachment);
                editedAttachment.remove("follows");
                editedAttachment.put("data", Base64.encodeBytes(fileData));
                return editedAttachment;
            }
        });
    }

    // #pragma mark - MISC.:

    /**
     * Updates or deletes an attachment, creating a new document revision in the process.
     * Used by the PUT / DELETE methods called on attachment URLs.
     */
    @InterfaceAudience.Private
    public RevisionInternal updateAttachment(String filename,
                                             BlobStoreWriter body,
                                             String contentType,
                                             AttachmentInternal.AttachmentEncoding encoding,
                                             String docID,
                                             String oldRevID,
                                             URL source)
            throws CouchbaseLiteException {

        if (filename == null || filename.length() == 0 ||
                (body != null && contentType == null) ||
                (oldRevID != null && docID == null) ||
                (body != null && docID == null))
            throw new CouchbaseLiteException(Status.BAD_ATTACHMENT);

        RevisionInternal oldRev = new RevisionInternal(docID, oldRevID, false);
        if (oldRevID != null) {
            // Load existing revision if this is a replacement:
            try {
                loadRevisionBody(oldRev);
            } catch (CouchbaseLiteException e) {
                if (e.getCBLStatus().getCode() == Status.NOT_FOUND &&
                        getDocument(docID, null, false) != null) {
                    throw new CouchbaseLiteException(Status.CONFLICT);
                }
                throw e;
            }
        } else {
            // If this creates a new doc, it needs a body:
            oldRev.setBody(new Body(new HashMap<String, Object>()));
        }

        // Update the _attachments dictionary:
        Map<String, Object> attachments = new HashMap<String, Object>();
        if (oldRev.getAttachments() != null)
            attachments.putAll(oldRev.getAttachments());
        if (body != null) {
            BlobKey key = body.getBlobKey();
            String digest = key.base64Digest();

            // TODO: Need to update
            Map<String, BlobStoreWriter> blobsByDigest = new HashMap<String, BlobStoreWriter>();
            blobsByDigest.put(digest, body);
            rememberAttachmentWritersForDigests(blobsByDigest);

            String encodingName = (encoding ==
                    AttachmentInternal.AttachmentEncoding.AttachmentEncodingGZIP) ? "gzip" : null;
            Map<String, Object> dict = new HashMap<String, Object>();
            dict.put("digest", digest);
            dict.put("length", body.getLength());
            dict.put("follows", true);
            dict.put("content_type", contentType);
            dict.put("encoding", encodingName);
            attachments.put(filename, dict);
        } else {
            if (oldRevID != null && !attachments.containsKey(filename)) {
                throw new CouchbaseLiteException(Status.NOT_FOUND);
            }
            attachments.remove(filename);
        }

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.putAll(oldRev.getProperties());
        properties.put("_attachments", attachments);

        Status status = new Status(Status.OK);
        RevisionInternal newRev = put(docID, properties, oldRevID, false, source, status);
        if (status.isError())
            throw new CouchbaseLiteException(status.getCode());

        return newRev;
    }

    @InterfaceAudience.Private
    void rememberAttachmentWritersForDigests(Map<String, BlobStoreWriter> blobsByDigest) {
        pendingAttachmentsByDigest.putAll(blobsByDigest);
    }

    @InterfaceAudience.Private
    private void rememberPendingKey(BlobKey key, String digest) {
        pendingAttachmentsByDigest.put(digest, key.getBytes());
    }

    /**
     * Parses the _revisions dict from a document into an array of revision ID strings
     */
    @InterfaceAudience.Private

    @SuppressWarnings("unchecked")
    static List<String> parseCouchDBRevisionHistory(Map<String, Object> docProperties) {
        Map<String, Object> revisions = (Map<String, Object>) docProperties.get("_revisions");
        if (revisions == null) {
            return new ArrayList<String>();
        }
        List<String> revIDs = new ArrayList<String>((List<String>) revisions.get("ids"));
        if (revIDs == null || revIDs.isEmpty()) {
            return new ArrayList<String>();
        }
        Integer start = (Integer) revisions.get("start");
        if (start != null) {
            for (int i = 0; i < revIDs.size(); i++) {
                String revID = revIDs.get(i);
                revIDs.set(i, Integer.toString(start--) + '-' + revID);
            }
        }
        return revIDs;
    }

    @InterfaceAudience.Private
    RevisionInternal putRevision(RevisionInternal rev,
                                 String prevRevId,
                                 boolean allowConflict)
            throws CouchbaseLiteException {
        Status ignoredStatus = new Status(Status.OK);
        return putRevision(rev, prevRevId, allowConflict, ignoredStatus);
    }

    @InterfaceAudience.Private
    private RevisionInternal putRevision(RevisionInternal putRev,
                                         String inPrevRevID,
                                         boolean allowConflict,
                                         Status outStatus)
            throws CouchbaseLiteException {
        return put(putRev.getDocID(), putRev.getProperties(), inPrevRevID, allowConflict, null,
                outStatus);
    }

    public RevisionInternal put(String docID,
                                Map<String, Object> properties,
                                String prevRevID,
                                boolean allowConflict,
                                URL source,
                                Status outStatus) throws CouchbaseLiteException {

        boolean deleting = properties == null ||
                (properties.containsKey("_deleted") &&
                        ((Boolean) properties.get("_deleted")).booleanValue());

        Log.v(TAG, "%s _id=%s, _rev=%s (allowConflict=%b)", (deleting ? "DELETE" : "PUT"),
                docID, prevRevID, allowConflict);

        // Attachments
        if (properties != null && properties.containsKey("_attachments")) {
            // Add any new attachment data to the blob-store, and turn all of them into stubs:
            //FIX: Optimize this to avoid creating a revision object
            RevisionInternal tmpRev = new RevisionInternal(docID, prevRevID, deleting);
            tmpRev.setProperties(properties);
            List<String> ancestry = new ArrayList<String>();
            if (prevRevID != null)
                ancestry.add(prevRevID);
            if (!processAttachmentsForRevision(tmpRev, ancestry, outStatus)) {
                return null;
            }
            properties = tmpRev.getProperties();
        }

        // TODO: Need to implement Shared (Manager.shared)
        StorageValidation validationBlock = null;
        if (validations != null && validations.size() > 0) {
            validationBlock = new StorageValidation() {
                @Override
                public Status validate(RevisionInternal newRev, RevisionInternal prevRev,
                                       String parentRevID) {
                    try {
                        validateRevision(newRev, prevRev, parentRevID);
                    } catch (CouchbaseLiteException e) {
                        return new Status(Status.FORBIDDEN);
                    }
                    return new Status(Status.OK);
                }
            };
        }

        return store.add(
                docID,
                prevRevID,
                properties,
                deleting,
                allowConflict,
                validationBlock,
                outStatus);
    }

    /**
     * Inserts an already-existing revision replicated from a remote sqliteDb.
     * <p/>
     * It must already have a revision ID. This may create a conflict!
     * The revision's history must be given; ancestor revision IDs that don't already exist locally
     * will create phantom revisions with no content.
     *
     * @exclude in CBLDatabase+Insertion.m
     * - (CBLStatus) forceInsert: (CBL_Revision*)inRev
     * revisionHistory: (NSArray*)history  // in *reverse* order, starting with rev's revID
     * source: (NSURL*)source
     */
    @InterfaceAudience.Private
    void forceInsert(RevisionInternal inRev, List<String> history, URL source)
            throws CouchbaseLiteException {
        Log.v(TAG, "INSERT %s, history[%d]", inRev, history == null ? 0 : history.size());
        String docID = inRev.getDocID();
        String revID = inRev.getRevID();
        if (!Document.isValidDocumentId(docID) || (revID == null))
            throw new CouchbaseLiteException(Status.BAD_ID);

        int historyCount = 0;
        if (history != null)
            historyCount = history.size();

        if (historyCount == 0) {
            history = new ArrayList<String>();
            history.add(revID);
        } else if (!history.get(0).equals(revID)) {
            // If inRev's revID doesn't appear in history, add it at the start:
            List<String> nuHistory = new ArrayList<String>(history);
            nuHistory.add(0, revID);
            history = nuHistory;
        }

        // Attachments
        Map<String, Object> attachments = inRev.getAttachments();
        if (attachments != null) {
            RevisionInternal updatedRev = inRev.copy();
            List<String> ancestry = history.subList(1, history.size());
            Status status = new Status(Status.OK);
            if (!processAttachmentsForRevision(updatedRev, ancestry, status)) {
                throw new CouchbaseLiteException(status);
            }
            inRev = updatedRev;
        }

        // TODO: Need to implement Shared (Manager.shared)
        StorageValidation validationBlock = null;
        if (validations != null && validations.size() > 0) {
            validationBlock = new StorageValidation() {
                @Override
                public Status validate(RevisionInternal newRev, RevisionInternal prevRev, String parentRevID) {
                    try {
                        validateRevision(newRev, prevRev, parentRevID);
                    } catch (CouchbaseLiteException e) {
                        return new Status(Status.FORBIDDEN);
                    }
                    return new Status(Status.OK);
                }
            };
        }

        store.forceInsert(inRev, history, validationBlock, source);
    }

    @InterfaceAudience.Private
    String lastSequenceWithCheckpointId(String checkpointId) {
        return store.getInfo(checkpointInfoKey(checkpointId));
    }

    @InterfaceAudience.Private
    boolean setLastSequence(String lastSequence, String checkpointId) {
        return store.setInfo(checkpointInfoKey(checkpointId), lastSequence) != -1;
    }

    private static String checkpointInfoKey(String checkpointID) {
        return "checkpoint/" + checkpointID;
    }

    @InterfaceAudience.Private
    int findMissingRevisions(RevisionList touchRevs) throws SQLException {
        return store.findMissingRevisions(touchRevs);
    }

    /**
     * Purges specific revisions, which deletes them completely from the local database
     * _without_ adding a "tombstone" revision. It's as though they were never there.
     * This operation is described here: http://wiki.apache.org/couchdb/Purge_Documents
     *
     * @param docsToRevs A dictionary mapping document IDs to arrays of revision IDs.
     * @resultOn success will point to an NSDictionary with the same form as docsToRev,
     * containing the doc/revision IDs that were actually removed.
     */
    @InterfaceAudience.Private
    Map<String, Object> purgeRevisions(final Map<String, List<String>> docsToRevs) {
        return store.purgeRevisions(docsToRevs);
    }

    @InterfaceAudience.Private
    private RevisionInternal getLocalDocument(String docID, String revID) {
        return store.getLocalDocument(docID, revID);
    }

    // NOTE: router-only
    @InterfaceAudience.Private
    public long getStartTime() {
        return this.startTime;
    }

    /**
     * Is the database open?
     */
    @InterfaceAudience.Private
    public boolean isOpen() {
        return open.get();
    }

    @InterfaceAudience.Private
    void addReplication(Replication replication) {
        allReplicators.add(replication);
    }

    @InterfaceAudience.Private
    void addActiveReplication(Replication replication) {
        replication.addChangeListener(new Replication.ChangeListener() {
            @Override
            public void changed(Replication.ChangeEvent event) {
                ReplicationStateTransition transition = event.getTransition();
                if (transition != null && transition.getDestination() == ReplicationState.STOPPED) {
                    synchronized (activeReplicators) {
                        activeReplicators.remove(event.getSource());
                        activeReplicators.notifyAll();
                    }
                }
            }
        });
        activeReplicators.add(replication);
    }

    /**
     * Get the PersistentCookieStore associated with this database.
     * Will lazily create one if none exists.
     */
    @InterfaceAudience.Private
    PersistentCookieJar getPersistentCookieStore() {
        if (persistentCookieStore == null)
            persistentCookieStore = new PersistentCookieJar(this);
        return persistentCookieStore;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal (protected or private) Methods
    ///////////////////////////////////////////////////////////////////////////

//    /**
//     * Empties the cache of recently used Document objects.
//     * API calls will now instantiate and return new instances.
//     */
//    protected void clearDocumentCache() {
//        docCache.clear();
//    }


    /**
     * Empties the cache of recently used Document objects.
     * API calls will now instantiate and return new instances.
     */
    void clearDocumentCache() {
        docCache.clear();
    }


    /**
     * Returns the already-instantiated cached Document with the given ID,
     * or nil if none is yet cached.
     */
    protected Document getCachedDocument(String documentID) {
        return docCache.get(documentID);
    }

    void removeDocumentFromCache(Document document) {
        docCache.remove(document.getId());
    }

    String getAttachmentStorePath() {
        return new File(path, "attachments").getPath();
    }

    private String getObsoletedAttachmentStorePath() {
        String attachmentStorePath = path;
        int lastDotPosition = attachmentStorePath.lastIndexOf('.');
        if (lastDotPosition > 0) {
            attachmentStorePath = attachmentStorePath.substring(0, lastDotPosition);
        }
        attachmentStorePath = attachmentStorePath + File.separator + "attachments";
        return attachmentStorePath;
    }

    private String getObsoletedAttachmentStoreParentPath() {
        String attachmentStorePath = path;
        int lastDotPosition = attachmentStorePath.lastIndexOf('.');
        if (lastDotPosition > 0) {
            attachmentStorePath = attachmentStorePath.substring(0, lastDotPosition);
        }
        return attachmentStorePath;
    }

    private boolean isBlobstoreMigrated() {
        Map<String, Object> props = getExistingLocalDocument("_blobstore");
        if (props != null && props.containsKey("blobstoreMigrated"))
            return (Boolean) props.get("blobstoreMigrated");
        return false;
    }

    private void markBlobstoreMigrated() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("blobstoreMigrated", true);
        try {
            putLocalDocument("_blobstore", props);
        } catch (CouchbaseLiteException e) {
            Log.e(Log.TAG_DATABASE, e.getMessage());
        }
    }

    protected String getPath() {
        return path;
    }

    public Store getStore() {
        return store;
    }

    /**
     * Returns an array of TDRevs in reverse chronological order, starting with the given revision.
     */
    @InterfaceAudience.Private
    List<RevisionInternal> getRevisionHistory(RevisionInternal rev) {
        // NOTE: getRevisionHistory() is thread safe. It is read operation to database as storage
        //       layer is thread-safe, and also not access to instance variables.
        return store.getRevisionHistory(rev);
    }


    @SuppressWarnings("unchecked")
    private String getDesignDocFunction(String fnName, String key, List<String> outLanguageList) {
        String[] path = fnName.split("/");
        if (path.length != 2) {
            return null;
        }
        String docId = String.format(Locale.ENGLISH, "_design/%s", path[0]);
        RevisionInternal rev = getDocument(docId, null, true);
        if (rev == null) {
            return null;
        }

        String outLanguage = (String) rev.getPropertyForKey("language");
        if (outLanguage != null) {
            outLanguageList.add(outLanguage);
        } else {
            outLanguageList.add("javascript");
        }
        Map<String, Object> container = (Map<String, Object>) rev.getPropertyForKey(key);
        return (String) container.get(path[1]);
    }

    /**
     * Set the document type for the given view name.
     *
     * @param docType  document type
     * @param viewName view name
     */
    @InterfaceAudience.Private
    void setViewDocumentType(String docType, String viewName) {
        if (viewDocTypes == null)
            viewDocTypes = new HashMap<String, String>();
        viewDocTypes.put(viewName, docType);
    }

    /**
     * Get the document type for the given view name.
     *
     * @param viewName view name
     * @return document type if available, otherwise returns null.
     */
    @InterfaceAudience.Private
    String getViewDocumentType(String viewName) {
        if (viewDocTypes == null)
            return null;
        return viewDocTypes.get(viewName);
    }

    /**
     * Remove document type for the given view name.
     *
     * @param viewName view name
     */
    private void removeViewDocumentType(String viewName) {
        if (viewDocTypes != null)
            viewDocTypes.remove(viewName);
    }

    /**
     * in CBLDatabase+Internal.m
     * - (void) forgetViewNamed: (NSString*)name
     */
    void forgetView(String name) {
        views.remove(name);
        removeViewDocumentType(name);
    }

    private View registerView(View view) {
        if (view == null)
            return null;
        if (views == null)
            views = new HashMap<String, View>();
        views.put(view.getName(), view);
        return view;
    }


    @SuppressWarnings("unchecked")
    List<QueryRow> queryViewNamed(String viewName,
                                  QueryOptions options,
                                  List<Long> outLastSequence)
            throws CouchbaseLiteException {

        long before = System.currentTimeMillis();
        long lastSequence = 0;
        List<QueryRow> rows = null;

        if (viewName != null && viewName.length() > 0) {
            final View view = getView(viewName);
            if (view == null) {
                throw new CouchbaseLiteException(new Status(Status.NOT_FOUND));
            }
            lastSequence = view.getLastSequenceIndexed();
            if (options.getStale() == Query.IndexUpdateMode.BEFORE || lastSequence <= 0) {
                view.updateIndex();
                lastSequence = view.getLastSequenceIndexed();
            } else if (options.getStale() == Query.IndexUpdateMode.AFTER &&
                    lastSequence < getLastSequenceNumber()) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            view.updateIndex();
                        } catch (CouchbaseLiteException e) {
                            Log.e(Database.TAG, "Error updating view index on background thread", e);
                        }
                    }
                }).start();
            }
            rows = view.query(options);
        } else {
            // nil view means query _all_docs
            // note: this is a little kludgy, but we have to pull out the "rows" field from the
            // result dictionary because that's what we want.  should be refactored, but
            // it's a little tricky, so postponing.
            Map<String, Object> allDocsResult = getAllDocs(options);
            rows = (List<QueryRow>) allDocsResult.get("rows");
            lastSequence = getLastSequenceNumber();
        }
        outLastSequence.add(lastSequence);

        long delta = System.currentTimeMillis() - before;
        Log.d(Database.TAG, "Query view %s completed in %d milliseconds", viewName, delta);

        return rows;
    }

    View makeAnonymousView() {
        for (int i = 0; true; ++i) {
            String name = String.format(Locale.ENGLISH, "anon%d", i);
            View existing = getExistingView(name);
            if (existing == null) {
                // this name has not been used yet, so let's use it
                return getView(name);
            }
        }
    }

    /**
     * NOTE: Only used by Unit Tests
     */
    List<View> getAllViews() {
        List<String> names = store.getAllViewNames();
        if (names == null)
            return null;
        List<View> views = new ArrayList<View>();
        for (String name : names) {
            View view = getExistingView(name);
            if (view != null)
                views.add(view);
        }
        return views;
    }

    /**
     * Given a decoded attachment with a "follows" property, find the associated CBL_BlobStoreWriter
     * and install it into the blob-store.
     * - (CBLStatus) installAttachment: (CBL_Attachment*)attachment
     */
    private void installAttachment(AttachmentInternal attachment) throws CouchbaseLiteException {
        String digest = attachment.getDigest();
        if (digest == null)
            throw new CouchbaseLiteException(Status.BAD_ATTACHMENT);

        Object writer = null;
        if (pendingAttachmentsByDigest != null && pendingAttachmentsByDigest.containsKey(digest)) {
            writer = pendingAttachmentsByDigest.get(digest);
        }

        if (writer != null && writer instanceof BlobStoreWriter) {
            // Found a blob writer, so install the blob:
            BlobStoreWriter blobStoreWriter = (BlobStoreWriter) writer;
            if (!blobStoreWriter.install())
                throw new CouchbaseLiteException(Status.ATTACHMENT_ERROR);
            attachment.setBlobKey(blobStoreWriter.getBlobKey());
            attachment.setPossiblyEncodedLength(blobStoreWriter.getLength());
            // Remove the writer but leave the blob-key behind for future use:
            rememberPendingKey(attachment.getBlobKey(), digest);
            return;
        } else if (writer != null && writer instanceof byte[]) {
            // This attachment was already added, but the key was left behind in the dictionary:
            attachment.setBlobKey(new BlobKey((byte[]) writer));
        } else if (attachments.hasBlobForKey(attachment.getBlobKey())) {
            // It already exists in the blob-store, so it's OK
            return;
        } else {
            Log.w(Database.TAG, "No pending attachment for getDigest: " + digest);
            throw new CouchbaseLiteException(Status.BAD_ATTACHMENT);
        }
    }

    // #pragma mark - EXPIRATION:

    /* package */void setExpirationDate(Date date, String docID) {
        long unixTime = date != null ? date.getTime() / 1000 : 0;
        store.setExpirationOfDocument(unixTime, docID);
        scheduleDocumentExpiration(0);
    }

    private void scheduleDocumentExpiration(long minimumDelay) {
        if (store == null) return;

        long nextExpiration = store.nextDocumentExpiry();
        if (nextExpiration > 0) {
            long delay = Math.max((nextExpiration - System.currentTimeMillis()) / 1000 + 1, minimumDelay);
            Log.v(TAG, "Scheduling next doc expiration in %d sec", delay);
            cancelPurgeTimer();
            purgeTimer = new Timer();
            purgeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isOpen())
                        purgeExpiredDocuments();
                }
            }, delay * 1000);
        } else
            Log.v(TAG, "No pending doc expirations");
    }

    private void purgeExpiredDocuments() {
        if (store == null) return;
        int nPurged = store.purgeExpiredDocuments();
        Log.v(TAG, "Purged %d expired documents", nPurged);
        scheduleDocumentExpiration(1);
    }

    private void cancelPurgeTimer() {
        if (purgeTimer != null) {
            purgeTimer.cancel();
            purgeTimer = null;
        }
    }

    // #pragma mark - LOOKING UP ATTACHMENTS:

    /**
     * - (NSDictionary*) attachmentsForDocID: (NSString*)docID
     * revID: (NSString*)revID
     * status: (CBLStatus*)outStatus
     */
    private Map<String, Object> getAttachments(String docID, String revID) {
        RevisionInternal mrev = new RevisionInternal(docID, revID, false);
        try {
            RevisionInternal rev = loadRevisionBody(mrev);
            return rev.getAttachments();
        } catch (CouchbaseLiteException e) {
            Log.w(Log.TAG_DATABASE, "Failed to get attachments for " + mrev);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @InterfaceAudience.Private
    public AttachmentInternal getAttachment(RevisionInternal rev, String filename)
            throws CouchbaseLiteException {
        assert (filename != null);
        Map<String, Object> attachments = rev.getAttachments();
        if (attachments == null) {
            attachments = getAttachments(rev.getDocID(), rev.getRevID());
            if (attachments == null)
                return null;
        }
        return getAttachment((Map<String, Object>) attachments.get(filename), filename);
    }

    void rememberAttachmentWriter(BlobStoreWriter writer) {
        pendingAttachmentsByDigest.put(writer.mD5DigestString(), writer);
    }

    private void rememberAttachmentWriter(BlobStoreWriter writer, String digest) {
        pendingAttachmentsByDigest.put(digest, writer);
    }

    // Database+Insertion

    private boolean postChangeNotifications() {
        synchronized (lockPostingChangeNotifications) {
            if (postingChangeNotifications)
                return false;
            postingChangeNotifications = true;
        }
        try {
            boolean posted = false;
            // This is a 'while' instead of an 'if' because when we finish posting notifications, there
            // might be new ones that have arrived as a result of notification handlers making document
            // changes of their own (the replicator manager will do this.) So we need to check again.
            while (store != null && !store.inTransaction() && open.get() && changesToNotify.size() > 0) {
                List<DocumentChange> outgoingChanges = new ArrayList<DocumentChange>();
                synchronized (changesToNotify) {
                    outgoingChanges.addAll(changesToNotify);
                    changesToNotify.clear();
                }

                // TODO: postPublicChangeNotification in CBLDatabase+Internal.m should replace
                // following lines of code.
                boolean isExternal = false;
                for (DocumentChange change : outgoingChanges) {
                    Document doc = cachedDocumentWithID(change.getDocumentId());
                    if (doc != null)
                        doc.revisionAdded(change, true);
                    if (change.getSource() != null)
                        isExternal = true;
                }

                final Database.ChangeEvent changeEvent = new Database.ChangeEvent(this, isExternal, outgoingChanges);
                for (Database.ChangeListener changeListener : changeListeners) {
                    if (changeListener != null) {
                        try {
                            changeListener.changed(changeEvent);
                        } catch (Exception ex) {
                            // Implementation of ChangeListener might throw RuntimeException,
                            // ignore it.
                            Log.e(TAG, "%s got exception posting change notification: %s",
                                    ex, this, changeListener);
                        }
                    }
                }
                posted = true;
            }
            return posted;
        } catch (Exception e) {
            // In general, non of methods that are used in this method throws Exception.
            // This catch block is just in case RuntimeException is thrown.
            Log.e(TAG, "Unknown Exception: %s got exception posting change notifications", e, this);
            return false;
        } finally {
            synchronized (lockPostingChangeNotifications) {
                postingChangeNotifications = false;
            }
        }
    }

    // Database+Replication

    Replication findActiveReplicator(Replication replicator) {
        synchronized (activeReplicators) {
            String remoteCheckpointDocID = replicator.remoteCheckpointDocID();
            if (remoteCheckpointDocID == null)
                return null;

            for (Replication r : activeReplicators) {
                if (remoteCheckpointDocID.equals(r.remoteCheckpointDocID()) && r.isRunning())
                    return r;
            }
        }
        return null;
    }

    Replication createReplicator(URL remote, boolean push, HttpClientFactory factory) {
        Replication replicator;
        if (push)
            replicator = new Replication(this, remote, Replication.Direction.PUSH, factory);
        else
            replicator = new Replication(this, remote, Replication.Direction.PULL, factory);
        return replicator;
    }

    // Database+LocalDocs

    private static String makeLocalDocumentId(String documentId) {
        return String.format(Locale.ENGLISH, "_local/%s", documentId);
    }

    /**
     * Creates a one-shot query with the given map function. This is equivalent to creating an
     * anonymous View and then deleting it immediately after querying it. It may be useful during
     * development, but in general this is inefficient if this map will be used more than once,
     * because the entire view has to be regenerated from scratch every time.
     */
    protected Query slowQuery(Mapper map) {
        return new Query(this, map);
    }

    RevisionInternal getParentRevision(RevisionInternal rev) {
        return store.getParentRevision(rev);
    }

    boolean replaceUUIDs() {
        if (store.setInfo("publicUUID", Misc.CreateUUID()) == -1)
            return false;
        return store.setInfo("privateUUID", Misc.CreateUUID()) != -1;
    }

    /**
     * Set the database's name.
     */
    protected void setName(String name) {
        this.name = name;
    }

    private Document cachedDocumentWithID(String documentId) {
        return docCache.resourceWithCacheKeyDontRecache(documentId);
    }

    private boolean garbageCollectAttachments() throws CouchbaseLiteException {
        Log.v(TAG, "Scanning database revisions for attachments...");
        Set<BlobKey> keys = store.findAllAttachmentKeys();
        if (keys == null)
            return false;
        Log.v(TAG, "    ...found %d attachments", keys.size());
        List<BlobKey> keysToKeep = new ArrayList<BlobKey>(keys);
        int deleted = attachments.deleteBlobsExceptWithKeys(keysToKeep);
        Log.v(TAG, "    ... deleted %d obsolete attachment files.", deleted);
        return deleted >= 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // CBLDatabase+Replication.h/CBLDatabase+Replication.m
    ////////////////////////////////////////////////////////////////////////////

    // Local checkpoint document keys:
    private static String kCBLDatabaseLocalCheckpoint_LocalUUID = "localUUID";
    private static String kLocalCheckpointDocId = "CBL_LocalCheckpoint";

    /**
     * Save current local uuid into the local checkpoint document.
     * <p/>
     * This method is called only
     * when importing or replacing the database. The old localUUID is used by replicators
     * to get the local checkpoint from the imported database in order to start replicating
     * from from the current local checkpoint of the imported database after importing.
     * <p/>
     * in CBLDatabase+Replication.m
     * - (BOOL) saveLocalUUIDInLocalCheckpointDocument: (NSError**)outError;
     */
    protected boolean saveLocalUUIDInLocalCheckpointDocument() {
        return putLocalCheckpointDocumentWithKey(
                kCBLDatabaseLocalCheckpoint_LocalUUID,
                privateUUID());
    }

    /**
     * Put a property with a given key and value into the local checkpoint document.
     * <p/>
     * in CBLDatabase+Replication.m
     * - (BOOL) putLocalCheckpointDocumentWithKey: (NSString*)key value:(id)value outError: (NSError**)outError
     */
    private boolean putLocalCheckpointDocumentWithKey(String key, Object value) {
        if (key == null || value == null) return false;

        Map<String, Object> localCheckpointDoc = getLocalCheckpointDocument();
        Map<String, Object> document;
        if (localCheckpointDoc != null)
            document = new HashMap<String, Object>(localCheckpointDoc);
        else
            document = new HashMap<String, Object>();
        document.put(key, value);
        boolean result = false;
        try {
            result = putLocalDocument(kLocalCheckpointDocId, document);
            if (!result)
                Log.w(Log.TAG_DATABASE, "CBLDatabase: Could not create a local checkpoint document with an error");
        } catch (CouchbaseLiteException e) {
            Log.w(Log.TAG_DATABASE, "CBLDatabase: Could not create a local checkpoint document with an error", e);
        }
        return result;
    }

    /**
     * Returns a property value specifiec by the key from the local checkpoint document.
     */
    protected Object getLocalCheckpointDocumentPropertyValueForKey(String key) {
        return getLocalCheckpointDocument().get(key);
    }

    /**
     * Returns local checkpoint document if it exists. Otherwise returns nil.
     */
    private Map<String, Object> getLocalCheckpointDocument() {
        return getExistingLocalDocument(kLocalCheckpointDocId);
    }

    private static long keyToSequence(Object key, long dflt) {
        return key instanceof Number ? ((Number) key).longValue() : dflt;
    }


}
