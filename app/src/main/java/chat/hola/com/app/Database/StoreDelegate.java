package chat.hola.com.app.Database;

/*
 * Created by moda on 09/01/17.
 */


import java.util.Map;

/**
 * Delegate of a CBL_Storage instance. CBLDatabase implements this.
 */
interface StoreDelegate {
    /**
     * Called whenever the outermost transaction completes.
     *
     * @param committed YES on commit, NO if the transaction was aborted.
     */
    void storageExitedTransaction(boolean committed);

    /**
     * Called whenever a revision is added to the database (but not for local docs or for purges.)
     */
    void databaseStorageChanged(DocumentChange change);

    /**
     * Generates a revision ID for a new revision.
     *
     * @param json      The canonical JSON of the revision (with metadata properties removed.)
     * @param deleted   YES if this revision is a deletion
     * @param prevRevID The parent's revision ID, or nil if this is a new document.
     */
    String generateRevID(byte[] json, boolean deleted, String prevRevID);

    // TODO: This minght not be a delegate methods. Review later.
    boolean runFilter(ReplicationFilter filter,
                      Map<String, Object> filterParams,
                      RevisionInternal rev);
}
