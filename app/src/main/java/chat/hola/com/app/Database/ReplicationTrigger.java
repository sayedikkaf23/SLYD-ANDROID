package chat.hola.com.app.Database;

/*
 * Created by moda on 07/01/17.
 */

enum ReplicationTrigger {
    START,
    WAITING_FOR_CHANGES,
    RESUME, // send the RESUME trigger when a replication is IDLE but has new items to process
    GO_OFFLINE,
    GO_ONLINE,
    STOP_GRACEFUL,
    STOP_IMMEDIATE
}

