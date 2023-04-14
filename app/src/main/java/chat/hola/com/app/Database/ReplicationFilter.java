package chat.hola.com.app.Database;

/*
 * Created by moda on 09/01/17.
 */


import java.util.Map;

/**
 * Filter block, used in changes feeds and replication.
 */
interface ReplicationFilter {
    /**
     * True if the Revision should be included in the pushed replication, otherwise false.
     */
    boolean filter(SavedRevision revision, Map<String, Object> params);
}
