package chat.hola.com.app.Database;

import com.couchbase.lite.Status;
import com.couchbase.lite.storage.Cursor;

/*
 * Created by moda on 09/01/17.
 */

interface QueryRowBlock {
    Status onRow(byte[] keyData, byte[] valueData, String docID, Cursor cursor);
}
