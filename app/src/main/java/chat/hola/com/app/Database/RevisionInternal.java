package chat.hola.com.app.Database;

/*
 * Created by moda on 09/01/17.
 */


import com.couchbase.lite.internal.Body;
import com.couchbase.lite.util.CollectionUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Stores information about a revision -- its docID, revID, and whether it's deleted.
 * <p/>
 * It can also store the sequence number and document contents (they can be added after creation).
 */
public class RevisionInternal {

    ////////////////////////////////////////////////////////////
    // Member variables
    ////////////////////////////////////////////////////////////

    private String docID;
    private String revID;
    private boolean deleted;
    private boolean missing;
    private Body body;
    private long sequence;

    ////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////

    public RevisionInternal(String docID, String revID, boolean deleted) {
        this.docID = docID;
        this.revID = revID;
        this.deleted = deleted;
    }

    public RevisionInternal(Body body) {
        this((String) body.getPropertyForKey("_id"),
                (String) body.getPropertyForKey("_rev"),
                ((body.getPropertyForKey("_deleted") != null)
                        && ((Boolean) body.getPropertyForKey("_deleted"))));
        this.body = body;
    }

    public RevisionInternal(Map<String, Object> properties) {
        this(new Body(properties));
    }

    ////////////////////////////////////////////////////////////
    // Setter / Getter methods
    ////////////////////////////////////////////////////////////

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getRevID() {
        return revID;
    }

    public void setRevID(String revID) {
        this.revID = revID;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public boolean isMissing() {
        return missing;
    }

    void setMissing(boolean missing) {
        this.missing = missing;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public long getSequence() {
        return sequence;
    }

    ////////////////////////////////////////////////////////////
    // Overwride methods
    ////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof RevisionInternal) {
            RevisionInternal other = (RevisionInternal) o;
            if (docID.equals(other.docID) && revID.equals(other.revID)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return docID.hashCode() ^ revID.hashCode();
    }

    @Override
    public String toString() {
        return '{' + this.docID + " #" + this.revID + " @" + sequence + (deleted ? " DEL" : "") + '}';
    }

    ////////////////////////////////////////////////////////////
    // Public Methods
    ////////////////////////////////////////////////////////////

    public Map<String, Object> getProperties() {
        Map<String, Object> result = null;
        if (body != null) {
            Map<String, Object> prop;
            try {
                prop = body.getProperties();
            } catch (IllegalStateException e) {
                // handle when both object and json are null for this body
                return null;
            }
            if (result == null) {
                result = new HashMap<String, Object>();
            }
            result.putAll(prop);
        }
        return result;
    }

    Object getPropertyForKey(String key) {
        Map<String, Object> prop = getProperties();
        if (prop == null) {
            return null;
        }
        return prop.get(key);
    }

    public void setProperties(Map<String, Object> properties) {
        this.body = new Body(properties);
    }

    public byte[] getJson() {
        byte[] result = null;
        if (body != null) {
            result = body.getJson();
        }
        return result;
    }

    public void setJSON(byte[] json) {
        this.body = new Body(json, docID, revID, deleted);
    }

    public RevisionInternal copy() {
        return copyWithDocID(docID, revID);
    }

    RevisionInternal copyWithDocID(String docID, String revID) {
        assert (docID != null);
        assert ((this.docID == null) || (this.docID.equals(docID)));
        RevisionInternal rev = new RevisionInternal(docID, revID, deleted);
        Map<String, Object> unmodifiableProperties = getProperties();
        Map<String, Object> properties = new HashMap<String, Object>();
        if (unmodifiableProperties != null) {
            properties.putAll(unmodifiableProperties);
        }
        properties.put("_id", docID);
        properties.put("_rev", revID);
        rev.setProperties(properties);
        return rev;
    }

    RevisionInternal copyWithoutBody() {
        if (body == null) {
            return this;
        }
        RevisionInternal rev = new RevisionInternal(docID, revID, deleted);
        rev.setSequence(sequence);
        rev.setMissing(missing);
        return rev;
    }

    /**
     * Generation number: 1 for a new document, 2 for the 2nd revision, ...
     * Extracted from the numeric prefix of the revID.
     */
    int getGeneration() {
        return generationFromRevID(revID);
    }

    // Calls the block on every attachment dictionary. The block can return a different dictionary,
    // which will be replaced in the rev's properties. If it returns nil, the operation aborts.
    // Returns YES if any changes were made.


    @SuppressWarnings("unchecked")
    boolean mutateAttachments(CollectionUtils.Functor<Map<String, Object>,
            Map<String, Object>> functor) {
        {
            Map<String, Object> properties = getProperties();
            Map<String, Object> editedProperties = null;
            Map<String, Object> attachments = (Map<String, Object>) properties.get("_attachments");
            Map<String, Object> editedAttachments = null;
            if (attachments != null) {
                for (String name : attachments.keySet()) {

                    Map<String, Object> attachment = new HashMap<String, Object>(
                            (Map<String, Object>) attachments.get(name));
                    attachment.put("name", name);
                    Map<String, Object> editedAttachment = functor.invoke(attachment);
                    if (editedAttachment == null) {
                        return false;  // block canceled
                    }
                    if (editedAttachment != attachment) {
                        if (editedProperties == null) {
                            // Make the document properties and _attachments dictionary mutable:
                            editedProperties = new HashMap<String, Object>(properties);
                            editedAttachments = new HashMap<String, Object>(attachments);
                            editedProperties.put("_attachments", editedAttachments);
                        }
                        editedAttachment.remove("name");
                        editedAttachments.put(name, editedAttachment);
                    }
                }
            }
            if (editedProperties != null) {
                setProperties(editedProperties);
                return true;
            }
            return false;
        }
    }


    @SuppressWarnings("unchecked")
    public Map<String, Object> getAttachments() {
        Map<String, Object> props = getProperties();
        if (props != null && props.containsKey("_attachments")) {
            return (Map<String, Object>) props.get("_attachments");
        }
        return null;
    }

    /**
     * in CBL_Revision.m
     * - (id)objectForKeyedSubscript:(id)key
     */
    Object getObject(String key) {
        return body != null ? body.getObject(key) : null;
    }

    ////////////////////////////////////////////////////////////
    // Public Static Methods
    ////////////////////////////////////////////////////////////

    static int generationFromRevID(String revID) {
        if (revID == null) return 0;
        int generation = 0;
        int dashPos = revID.indexOf('-');
        if (dashPos > 0) {
            generation = Integer.parseInt(revID.substring(0, dashPos));
        }
        return generation;
    }

    public static String digestFromRevID(String revID) {
        String digest = "error";
        int dashPos = revID.indexOf('-');
        if (dashPos > 0) {
            digest = revID.substring(dashPos + 1);
            return digest;
        }
        throw new RuntimeException(String.format(Locale.ENGLISH, "Invalid rev id: %s", revID));
    }

    private static int CBLCollateRevIDs(String revId1, String revId2) {

        String rev1GenerationStr = null;
        String rev2GenerationStr = null;
        String rev1Hash = null;
        String rev2Hash = null;

        StringTokenizer st1 = new StringTokenizer(revId1, "-");
        try {
            rev1GenerationStr = st1.nextToken();
            rev1Hash = st1.nextToken();
        } catch (Exception e) {

            e.printStackTrace();
        }

        StringTokenizer st2 = new StringTokenizer(revId2, "-");
        try {
            rev2GenerationStr = st2.nextToken();
            rev2Hash = st2.nextToken();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // improper rev IDs; just compare as plain text:
        if (rev1GenerationStr == null || rev2GenerationStr == null) {
            return revId1.compareToIgnoreCase(revId2);
        }

        Integer rev1Generation;
        Integer rev2Generation;

        try {
            rev1Generation = Integer.parseInt(rev1GenerationStr);
            rev2Generation = Integer.parseInt(rev2GenerationStr);
        } catch (NumberFormatException e) {
            // improper rev IDs; just compare as plain text:
            return revId1.compareToIgnoreCase(revId2);
        }

        // Compare generation numbers; if they match, compare suffixes:
        if (rev1Generation.compareTo(rev2Generation) != 0) {
            return rev1Generation.compareTo(rev2Generation);
        } else if (rev1Hash != null && rev2Hash != null) {
            // compare suffixes if possible
            return rev1Hash.compareTo(rev2Hash);
        } else {
            // just compare as plain text:
            return revId1.compareToIgnoreCase(revId2);
        }
    }

    static int CBLCompareRevIDs(String revId1, String revId2) {
        assert (revId1 != null);
        assert (revId2 != null);
        return CBLCollateRevIDs(revId1, revId2);
    }
}
