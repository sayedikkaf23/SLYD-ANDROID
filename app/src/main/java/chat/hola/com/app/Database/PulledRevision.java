package chat.hola.com.app.Database;

/*
 * Created by moda on 07/01/17.
 */


import com.couchbase.lite.internal.Body;
import com.couchbase.lite.internal.InterfaceAudience;

import java.util.Map;

/**
 * A revision received from a remote server during a pull. Tracks the opaque remote sequence ID.
 */
@InterfaceAudience.Private
class PulledRevision extends RevisionInternal {

    private String remoteSequenceID = null;
    private boolean conflicted = false;

    public PulledRevision(Body body) {
        super(body);
    }

    PulledRevision(String docId, String revId, boolean deleted) {
        super(docId, revId, deleted);
    }

    PulledRevision(Map<String, Object> properties) {
        super(properties);
    }

    String getRemoteSequenceID() {
        return remoteSequenceID;
    }

    void setRemoteSequenceID(String remoteSequenceID) {
        this.remoteSequenceID = remoteSequenceID;
    }

    boolean isConflicted() {
        return conflicted;
    }

    void setConflicted(boolean conflicted) {
        this.conflicted = conflicted;
    }
}