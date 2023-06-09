package chat.hola.com.app.Database;

import com.couchbase.lite.ChangeValidator;
import com.couchbase.lite.CouchbaseLiteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
 * Created by moda on 09/01/17.
 */


class ValidationContextImpl implements ValidationContext {

    private Database database;
    private RevisionInternal currentRevision;
    private RevisionInternal newRev;
    private String rejectMessage;
    private List<String> changedKeys;

    ValidationContextImpl(Database database, RevisionInternal currentRevision,
                          RevisionInternal newRev) {
        this.database = database;
        this.currentRevision = currentRevision;
        this.newRev = newRev;
    }

    private RevisionInternal getCurrentRevisionInternal() {
        if (currentRevision != null) {
            try {
                currentRevision = database.loadRevisionBody(currentRevision);
            } catch (CouchbaseLiteException e) {
                throw new RuntimeException(e);
            }
        }
        return currentRevision;
    }

    @Override
    public SavedRevision getCurrentRevision() {
        final RevisionInternal cur = getCurrentRevisionInternal();
        return cur != null ? new SavedRevision(database, cur) : null;
    }

    @Override
    public List<String> getChangedKeys() {
        if (changedKeys == null) {
            changedKeys = new ArrayList<String>();
            Map<String, Object> cur = null;
            if (getCurrentRevision() != null)
                cur = getCurrentRevision().getProperties();
            Map<String, Object> nuu = newRev.getProperties();
            if (nuu != null) {
                if (cur != null) {
                    for (String key : cur.keySet()) {
                        if (cur.get(key) != null && !cur.get(key).equals(nuu.get(key)) && !key.equals("_rev"))
                            changedKeys.add(key);
                    }
                }
                for (String key : nuu.keySet()) {
                    if ((cur == null || cur.get(key) == null) && !key.equals("_rev") && !key.equals("_id"))
                        changedKeys.add(key);
                }
            }
        }
        return changedKeys;
    }

    @Override
    public void reject() {
        if (rejectMessage == null) {
            rejectMessage = "invalid document";
        }
    }

    @Override
    public void reject(String message) {
        if (rejectMessage == null) {
            rejectMessage = message;
        }
    }

    @Override
    public boolean validateChanges(ChangeValidator changeValidator) {
        Map<String, Object> cur = getCurrentRevision().getProperties();
        Map<String, Object> nuu = newRev.getProperties();
        for (String key : getChangedKeys()) {
            if (!changeValidator.validateChange(key, cur.get(key), nuu.get(key))) {
                reject(String.format(Locale.ENGLISH, "Illegal change to '%s' property", key));
                return false;
            }
        }
        return true;
    }

    String getRejectMessage() {
        return rejectMessage;
    }
}
