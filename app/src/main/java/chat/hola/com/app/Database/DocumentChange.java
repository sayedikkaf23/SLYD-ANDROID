package chat.hola.com.app.Database;

/*
 * Created by moda on 07/01/17.
 */

import com.couchbase.lite.internal.InterfaceAudience;

import java.net.URL;
import java.util.Locale;


/**
 * Provides details about a Document change.
 */
public class DocumentChange {
    private RevisionInternal addedRevision;
    private String documentID;
    private String winningRevisionID;
    private boolean isConflict;
    private URL source;

    /**
     * @exclude
     */
    @InterfaceAudience.Private
    DocumentChange(RevisionInternal addedRevision,
                   String winningRevisionID,
                   boolean isConflict,
                   URL source) {
        this.addedRevision = addedRevision;
        this.documentID = addedRevision.getDocID();
        this.winningRevisionID = winningRevisionID;
        this.isConflict = isConflict;
        this.source = source;
    }

    // - (instancetype) initWithPurgedDocument: (NSString*)docID in CBLDatabaseChange.m
    @InterfaceAudience.Private
    DocumentChange(String docID) {
        this.documentID = docID;
    }

    @InterfaceAudience.Public
    public String getDocumentId() {
        return documentID;
    }

    @InterfaceAudience.Public
    String getRevisionId() {
        return addedRevision != null ? addedRevision.getRevID() : null;
    }

    @InterfaceAudience.Public
    private boolean isCurrentRevision() {
        return winningRevisionID != null && addedRevision != null && addedRevision.getRevID().equals(winningRevisionID);
    }

    @InterfaceAudience.Public
    public boolean isConflict() {
        return isConflict;
    }

    @InterfaceAudience.Public
    public URL getSource() {
        return source;
    }

    @InterfaceAudience.Public
    public String toString() {
        return String.format(Locale.ENGLISH, "%s[%s]", this.getClass().getName(), addedRevision);
    }

    /**
     * @exclude
     */
    @InterfaceAudience.Private
    RevisionInternal getAddedRevision() {
        return addedRevision;
    }

    /**
     * @exclude
     */
    @InterfaceAudience.Private
    RevisionInternal getWinningRevisionIfKnown() {
        return isCurrentRevision() ? addedRevision : null;
    }

    /**
     * @exclude
     */
    @InterfaceAudience.Private
    String getWinningRevisionID() {
        return winningRevisionID;
    }

    void reduceMemoryUsage() {
        if (addedRevision != null)
            addedRevision = addedRevision.copyWithoutBody();
    }
}
