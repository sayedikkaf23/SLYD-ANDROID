package chat.hola.com.app.Database;
/*
 * Created by moda on 09/01/17.
 */

import com.couchbase.lite.Status;


interface StorageValidation {
   /**
    * Document validation callback, passed to the insertion methods.
    */
   Status validate(RevisionInternal newRev, RevisionInternal prevRev, String parentRevID);
}
