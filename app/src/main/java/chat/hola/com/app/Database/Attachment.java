package chat.hola.com.app.Database;

/*
 * Created by moda on 07/01/17.
 */


import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Status;
import com.couchbase.lite.internal.InterfaceAudience;
import com.couchbase.lite.support.security.SymmetricKeyException;
import com.couchbase.lite.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A Couchbase Lite Document Attachment.
 */
final class Attachment {

    /**
     * The owning document revision.
     */
    private Revision revision = null;

    /**
     * The filename.
     */
    private String name = null;

    /**
     * The CouchbaseLite metadata about the attachment, that lives in the document.
     */
    private Map<String, Object> metadata = null;

    /**
     * The body data.
     */
    private InputStream body = null;

    /**
     * Constructor
     */
    @InterfaceAudience.Private
    Attachment(InputStream contentStream, String contentType) {
        this.body = contentStream;
        metadata = new HashMap<String, Object>();
        metadata.put("content_type", contentType);
        metadata.put("follows", true);
    }

    /**
     * Constructor
     */
    @InterfaceAudience.Private
    Attachment(Revision revision, String name, Map<String, Object> metadata) {
        this.revision = revision;
        this.name = name;
        this.metadata = metadata;
    }

    private AttachmentInternal internalAttachment() throws CouchbaseLiteException {
        return revision.getDatabase().getAttachment(metadata, name);
    }

    /**
     * Get the owning document revision.
     */
    @InterfaceAudience.Public
    public Revision getRevision() {
        return revision;
    }

    /**
     * Get the owning document.
     */
    @InterfaceAudience.Public
    public Document getDocument() {
        return revision != null ? revision.getDocument() : null;
    }

    /**
     * Get the filename.
     */
    @InterfaceAudience.Public
    public String getName() {
        return name;
    }

    /**
     * Get the MIME type of the contents.
     */
    @InterfaceAudience.Public
    public String getContentType() {
        return (String) metadata.get("content_type");
    }


    /**
     * Get the input stream of the content (aka 'body') data.
     *
     * @throws CouchbaseLiteException
     */
    @InterfaceAudience.Public
    public InputStream getContent() throws CouchbaseLiteException {
        if (body != null) {
            return body;
        } else {
            return new ByteArrayInputStream(internalAttachment().getContent());
        }
    }

    /**
     * Get the URL of the file containing the contents.
     * This property is somewhat deprecated and is made available only for use with platform APIs that
     * require file paths/URLs, e.g. some media playback APIs. Whenever possible, use the `getContent()`
     * method to get the input stream of the content instead.
     * The file must be treated as read-only! DO NOT MODIFY OR DELETE IT.
     * If the database is encrypted, attachment files are also encrypted and not directly readable,
     * so this property will return null.
     */
    @InterfaceAudience.Public

    @SuppressWarnings("TryWithIdenticalCatches")
    public URL getContentURL() {
        try {
            return internalAttachment().getContentURL();
        } catch (MalformedURLException e) {
            Log.w(Database.TAG, e.toString());
        } catch (CouchbaseLiteException e) {
            Log.w(Database.TAG, e.toString());
        }
        return null;
    }

    /**
     * Get the length in bytes of the contents.
     */
    @InterfaceAudience.Public
    public long getLength() {
        Number length = (Number) metadata.get("length");
        if (length != null) {
            return length.longValue();
        } else {
            return 0;
        }
    }

    /**
     * The CouchbaseLite metadata about the attachment, that lives in the document.
     */
    @InterfaceAudience.Public
    public Map<String, Object> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    @InterfaceAudience.Private
    protected void setName(String name) {
        this.name = name;
    }

    @InterfaceAudience.Private
    protected void setRevision(Revision revision) {
        this.revision = revision;
    }

    @InterfaceAudience.Private
    private InputStream getBodyIfNew() {
        return body;
    }

    /**
     * Goes through an _attachments dictionary and replaces any values that are Attachment objects
     * with proper JSON metadata dicts. It registers the attachment bodies with the blob store and sets
     * the metadata 'getDigest' and 'follows' properties accordingly.
     */
    @InterfaceAudience.Private
    static Map<String, Object> installAttachmentBodies(Map<String, Object> attachments,
                                                       Database database)
            throws CouchbaseLiteException {
        Map<String, Object> updatedAttachments = new HashMap<String, Object>();
        for (String name : attachments.keySet()) {
            Object value = attachments.get(name);
            if (value instanceof Attachment) {
                Attachment attachment = (Attachment) value;
                Map<String, Object> metadataMutable = new HashMap<String, Object>();
                metadataMutable.putAll(attachment.getMetadata());
                InputStream body = attachment.getBodyIfNew();
                if (body != null) {
                    // Copy attachment body into the database's blob store:
                    BlobStoreWriter writer;
                    try {
                        writer = blobStoreWriterForBody(body, database);
                    } catch (Exception e) {
                        throw new CouchbaseLiteException(e.getMessage(), Status.ATTACHMENT_ERROR);
                    }
                    metadataMutable.put("length", writer.getLength());
                    metadataMutable.put("digest", writer.mD5DigestString());
                    metadataMutable.put("follows", true);
                    database.rememberAttachmentWriter(writer);
                }
                updatedAttachments.put(name, metadataMutable);
            } else if (value instanceof AttachmentInternal) {
                throw new IllegalArgumentException("AttachmentInternal objects not expected here.  Could indicate a bug");
            } else if (value != null) {
                updatedAttachments.put(name, value);
            }
        }
        return updatedAttachments;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @InterfaceAudience.Private
    private static BlobStoreWriter blobStoreWriterForBody(InputStream body, Database database)
            throws IOException, SymmetricKeyException {
        BlobStoreWriter writer = database.getAttachmentWriter();
        try {
            writer.appendInputStream(body);
            writer.finish();
        } catch (IOException e) {
            writer.cancel();
            throw e;
        } catch (SymmetricKeyException e) {
            writer.cancel();
            throw e;
        }
        return writer;
    }
}
