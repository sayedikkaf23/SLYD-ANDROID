package chat.hola.com.app.dubly;

import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.DublyService;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.post.ReportReason;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>SearchUserPresenter</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class DubsPresenter implements DubsContract.Presenter, ClickListner {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static int page = 0;
    private int downloadedSize = 0;
    @Inject
    DublyService service;
    @Inject
    DubsContract.View view;
    @Inject
    DubsModel model;
    @Inject
    HowdooService howdooService;
    @Inject
    NetworkConnector networkConnector;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    DubsPresenter() {
    }

    @Override
    public void getDubs(int offset, int limit, String categoryId) {
        isLoading = true;
        service.getMusicListByCategory(AppController.getInstance().getApiToken(), Constants.LANGUAGE, categoryId, offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<DubResponse>>() {
                    @Override
                    public void onNext(Response<DubResponse> response) {
                        try {

                            switch (response.code()) {
                                case 200:
                                    isLastPage = response.body().getDubs().size() < PAGE_SIZE;
                                    if (offset == 0)
                                        model.clearList();
                                    model.setData(response.body().getDubs());
                                    break;
                                case 401:
                                    view.sessionExpired();
                                    break;
                                case 406:
                                    SessionObserver sessionObserver = new SessionObserver();
                                    sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DisposableObserver<Boolean>() {
                                                @Override
                                                public void onNext(Boolean flag) {
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            getDubs(offset, limit, categoryId);
                                                        }
                                                    }, 1000);
                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }

                                                @Override
                                                public void onComplete() {
                                                }
                                            });
                                    sessionApiCall.getNewSession(sessionObserver);
                                    break;
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }


    @Override
    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount, String categoryId) {

        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                page++;
                getDubs(PAGE_SIZE * page, PAGE_SIZE, categoryId);
            }
        }
    }

    @Override
    public ClickListner getPresenter() {
        return this;
    }

    @Override
    public void play(int position, boolean isPlaying) {
        model.setPlaying(position, isPlaying);
        view.play(model.getAudio(position), isPlaying, position, model.getDuration(position));
    }

    @Override
    public void dubWithIt(String name, int position) {
        download(position);
    }

    @Override
    public void download(int position) {
        view.startedDownload();
        new Thread(() -> downloadFile(position)).start();
    }

    @Override
    public void like(int position, boolean flag) {
        Map<String, Object> map = new HashMap<>();
        map.put("musicId", model.getMusicId(position));
        map.put("isFavourite", flag);
        howdooService.favourite(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ReportReason>>() {
                    @Override
                    public void onNext(Response<ReportReason> reportReasonResponse) {
                        switch (reportReasonResponse.code()) {
                            case 200:
                                model.setFavourite(position, flag);
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        like(position, flag);
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {
                                            }
                                        });
                                sessionApiCall.getNewSession(sessionObserver);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void startedDownload() {

    }

    private void downloadFile(int position) {

        try {
          //
          int downloadedSize;
          String filePath = model.getPath(position);
          URL url = new URL(filePath);
          URLConnection urlConnection = url.openConnection();
          urlConnection.connect();
          // getting file length
          int contentLength = urlConnection.getContentLength();

          // input stream to read file - with 8k buffer
          InputStream input = new BufferedInputStream(url.openStream(), 8192);
          String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
          // Output stream to write file
          OutputStream output = new FileOutputStream(
              new File(AppController.getInstance().getExternalFilesDir(null) + "/" + fileName));

          byte[] data = new byte[1024];

          long total = 0;

          while ((downloadedSize = input.read(data)) != -1) {
            total += downloadedSize;
            // publishing the progress....
            // After this onProgressUpdate will be called

            if (view != null) {
              view.progress((int) ((total * 100) / contentLength));
            }
            // writing data to file
            output.write(data, 0, downloadedSize);
          }

          // flushing output
          output.flush();

          // closing streams
          output.close();
          input.close();
          if (view != null) {
            view.finishedDownload(
                AppController.getInstance().getExternalFilesDir(null) + "/" + fileName,
                model.getName(position), model.getMusicId(position));

          }
          //
          //  String filePath = model.getPath(position);
          //
          //  URL url = new URL(model.getPath(position));
          //  InputStream is = url.openStream();
          //
          //  DataInputStream dis = new DataInputStream(is);
          //
          //  byte[] buffer = new byte[1024];
          //  int length;
          //
          //  String filename = filePath.substring(filePath.lastIndexOf('/') + 1);
          //  FileOutputStream fos = new FileOutputStream(
          //          new File(AppController.getInstance().getExternalFilesDir(null) + "/" + filename));
          //
          //  while ((length = dis.read(buffer)) > 0) {
          //      fos.write(buffer, 0, length);
          //      downloadedSize += length;
          //      // update the progressbar //
          //      view.progress(downloadedSize);
          //  }
          //  //close the output stream when complete //
          //  fos.close();
          //  view.finishedDownload(AppController.getInstance().getExternalFilesDir(null) + "/" + filename,
          //          model.getName(position), model.getMusicId(position));
        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        } catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
        }
    }

    String showError(final String err) {
        return err;
    }
}
