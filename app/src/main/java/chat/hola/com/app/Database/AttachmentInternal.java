package chat.hola.com.app.Database;

/*
 * Created by moda on 07/01/17.
 */


import com.couchbase.lite.BlobKey;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Status;
import com.couchbase.lite.support.Base64;
import com.couchbase.lite.util.Log;
import com.couchbase.lite.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple container for attachment metadata.
 */
class AttachmentInternal {

    enum AttachmentEncoding {
        AttachmentEncodingNone, AttachmentEncodingGZIP
    }

    private String name;
    private String contentType;
    private Database database;

    // in CBL_Attachment.h
    private int length;
    private int encodedLength;
    private AttachmentInternal.AttachmentEncoding encoding = AttachmentInternal.AttachmentEncoding.AttachmentEncodingNone;
    private int revpos;

    // in CBL_Attachment.m
    private BlobKey blobKey;
    private String digest;
    private byte[] data;

    /**
     * - (instancetype) initWithName: (NSString*)name contentType: (NSString*)contentType
     */
    private AttachmentInternal(String name, String contentType) {
        this.name = name;
        this.contentType = contentType;
    }

    /**
     * - (instancetype) initWithName: (NSString*)name
     * info: (NSDictionary*)attachInfo
     * status: (CBLStatus*)outStatus
     */
    AttachmentInternal(String name, Map<String, Object> attachInfo)
            throws CouchbaseLiteException {
        this(name, (String) attachInfo.get("content_type"));

        Number explicitLength = (Number) attachInfo.get("length");
        if (explicitLength != null)
            length = explicitLength.intValue();
        explicitLength = (Number) attachInfo.get("encoded_length");
        if (explicitLength != null)
            encodedLength = explicitLength.intValue();

        digest = (String) attachInfo.get("digest");
        if (digest != null) {
            try {
                blobKey = new BlobKey(digest); // (but getDigest might not map to a blob key)
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }

        String encodingStr = (String) attachInfo.get("encoding");
        if (encodingStr != null && encodingStr.length() > 0) {
            if (encodingStr.equalsIgnoreCase("gzip"))
                encoding = AttachmentInternal.AttachmentEncoding.AttachmentEncodingGZIP;
            else
                throw new CouchbaseLiteException(Status.BAD_ENCODING);
        }

        Object newContentBase64 = attachInfo.get("data");
        if (newContentBase64 != null) {
            // If there's inline attachment data, decode and store it:
            if (newContentBase64 instanceof String) {
                try {
                    data = Base64.decode((String) newContentBase64, Base64.DONT_GUNZIP);
                } catch (IOException e) {
                    throw new CouchbaseLiteException(Status.BAD_ENCODING);
                }
            } else {
                data = (byte[]) newContentBase64;
            }
            if (data == null)
                throw new CouchbaseLiteException(Status.BAD_ENCODING);
            setPossiblyEncodedLength(data.length);
        } else if (attachInfo.containsKey("stub") &&
                ((Boolean) attachInfo.get("stub")).booleanValue()) {
            // This item is just a stub; validate and skip it
            if (attachInfo.containsKey("revpos")) {
                int revPos = ((Number) attachInfo.get("revpos")).intValue();
                if (revPos <= 0) {
                    throw new CouchbaseLiteException(Status.BAD_ATTACHMENT);
                }
                setRevpos(revPos);
            }
            // skip
        } else if (attachInfo.containsKey("follows") &&
                ((Boolean) attachInfo.get("follows")).booleanValue()) {
            // I can't handle this myself; my caller will look it up from the getDigest
            if (digest == null)
                throw new CouchbaseLiteException(Status.BAD_ATTACHMENT);
        } else {
            throw new CouchbaseLiteException(Status.BAD_ATTACHMENT);
        }
    }

    private boolean hasBlobKey() {
        return blobKey != null && blobKey.hasBlobKey();
    }

    public String getDigest() {
        if (digest != null)
            return digest;
        else if (hasBlobKey())
            return blobKey.base64Digest();
        else
            return null;
    }

    public boolean isValid() {
        if (encoding != AttachmentInternal.AttachmentEncoding.AttachmentEncodingNone) {
            if (encodedLength == 0 && length > 0) {
                return false;
            }
        } else if (encodedLength > 0) {
            return false;
        }
        return revpos != 0;
    }

    Map<String, Object> asStubDictionary() {
        Map<String, Object> dict = new HashMap<String, Object>();
        dict.put("stub", true);
        dict.put("digest", blobKey.base64Digest());
        if (contentType != null)
            dict.put("content_type", contentType);
        dict.put("revpos", revpos);
        dict.put("length", length);
        if (encodedLength > 0)
            dict.put("encoded_length", encodedLength);
        switch (encoding) {
            case AttachmentEncodingGZIP:
                dict.put("encoding", "gzip");
                break;
            case AttachmentEncodingNone:
                break;
        }
        return dict;
    }

    byte[] getEncodedContent() {
        if (data != null)
            return data;
        else
            return database != null ? database.getAttachmentStore().blobForKey(blobKey) : null;
    }

    public byte[] getContent() {
        byte[] data = getEncodedContent();
        switch (encoding) {
            case AttachmentEncodingGZIP:
                if (data != null)
                    data = Utils.decompressByGzip(data);
                break;
            case AttachmentEncodingNone:
                // special case
                if (data != null && blobKey != null && database != null &&
                        database.getAttachmentStore().isGZipped(blobKey)) {
                    data = Utils.decompressByGzip(data);
                    encoding = AttachmentInternal.AttachmentEncoding.AttachmentEncodingGZIP;
                }
                break;
        }
        if (data == null)
            Log.w(Database.TAG, "Unable to decode attachment!");

        return data;
    }

    public InputStream getContentInputStream() {
        return new ByteArrayInputStream(getContent());
    }

    public InputStream getEncodedContentInputStream() {
        return new ByteArrayInputStream(getEncodedContent());
    }

    URL getContentURL() throws MalformedURLException {
        String path = database.getAttachmentStore().getBlobPathForKey(blobKey);
        return path != null ? new File(path).toURI().toURL() : null;
    }


    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public AttachmentInternal.AttachmentEncoding getEncoding() {
        return encoding;
    }

    public void setEncoding(AttachmentInternal.AttachmentEncoding encoding) {
        this.encoding = encoding;
    }

    BlobKey getBlobKey() {
        return blobKey;
    }

    void setBlobKey(BlobKey blobKey) {
        this.blobKey = blobKey;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getEncodedLength() {
        return encodedLength;
    }

    public void setEncodedLength(int encodedLength) {
        this.encodedLength = encodedLength;
    }

    int getRevpos() {
        return revpos;
    }

    void setRevpos(int revpos) {
        this.revpos = revpos;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    /**
     * Sets encodedLength if there is an encoding, else length.
     */
    void setPossiblyEncodedLength(int len) {
        if (encoding != AttachmentInternal.AttachmentEncoding.AttachmentEncodingNone)
            encodedLength = len;
        else
            length = len;
    }
}
