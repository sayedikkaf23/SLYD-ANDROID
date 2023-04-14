package chat.hola.com.app.Utilities;

import android.os.AsyncTask;
import chat.hola.com.app.AppController;
import com.bumptech.glide.Glide;

public class ClearGlideCacheAsyncTask extends AsyncTask<Void, Void, Boolean> {

  private boolean result;

  @Override
  protected Boolean doInBackground(Void... params) {
    try {
      Glide.get(AppController.getInstance()).clearDiskCache();
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override
  protected void onPostExecute(Boolean result) {
    super.onPostExecute(result);

    if (result) Glide.get(AppController.getInstance()).clearMemory();
  }
}