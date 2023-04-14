package chat.hola.com.app.MessagesHandler.Utilities;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import chat.hola.com.app.AppController;
import chat.hola.com.app.DownloadFile.FileDownloadService;
import chat.hola.com.app.DownloadFile.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("TryWithIdenticalCatches")
public class DownloadMessage extends AsyncTask {
    private AppController instance = AppController.getInstance();
    /*
     *         String messageType = (String) params[1];
     *
     *           String replyTypeString = (String) params[2];
     *
     *            String thumbnailPath = (String) params[3];
     *            String filePath = (String) params[4];
     *           String messageId = (String) params[5];
     *
     *            String docId = (String) params[6];
     *            String senderId = (String) params[7];
     */

    @Override
    protected Object doInBackground(final Object[] params) {


        String url = (String) params[0];


        final FileDownloadService downloadService =
                ServiceGenerator.createService(FileDownloadService.class);


        Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlAsync(url);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {


                if (response.isSuccessful()) {
                    new AsyncTask<Void, Long, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {


                            int replyType = -1;


                            if ((params[1]).equals("10")) {


                                replyType = Integer.parseInt((String) params[2]);
                            }


                            boolean writtenToDisk = instance.writeResponseBodyToDisk(response.body(), (String) params[4],
                                    (String) params[1], (String) params[5], replyType);


                            if (writtenToDisk) {


                                //  deleteFileFromServer(url);


                                if (params[3] != null) {
                                    /*
                                     *
                                     * incase of image or video delete the thumbnail
                                     *
                                     * */

                                    File fDelete = new File((String) params[3]);
                                    if (fDelete.exists()) fDelete.delete();


                                }


                                /*
                                 *To send the callback for automatically media downloaded
                                 */
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("eventName", "MessageDownloaded");

                                    obj.put("messageType", params[1]);
                                    obj.put("replyType", params[2]);
                                    obj.put("filePath", params[4]);
                                    obj.put("messageId", params[5]);
                                    obj.put("docId", params[6]);
                                    obj.put("senderId", params[7]);

                                    AppController.getBus().post(obj);
                                } catch (JSONException e) {

                                }


                                try {

                                    instance.getDbController().updateDownloadStatusAndPath((String) params[6],
                                            (String) params[4], (String) params[5]);
                                } catch (Exception e) {

                                }

                            }


                            return null;


                        }
                    }.execute();


                }


            }

            @Override
            public void onFailure(final Call<ResponseBody> call, Throwable t) {


            }

        });


        return null;
    }

}