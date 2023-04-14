package chat.hola.com.app.Database;

/*
 * Created by moda on 09/01/17.
 */
import com.couchbase.lite.replicator.RemoteRequest;
import com.couchbase.lite.replicator.RemoteRequestCompletion;
import com.couchbase.lite.replicator.RemoteRequestResponseException;
import com.couchbase.lite.support.HttpClientFactory;
import com.couchbase.lite.util.Log;
import com.couchbase.lite.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


class RemoteMultipartDownloaderRequest extends RemoteRequest {
    ////////////////////////////////////////////////////////////
    // Constants
    ////////////////////////////////////////////////////////////
    private static final int BUF_LEN = 1024;

    ////////////////////////////////////////////////////////////
    // Member variables
    ////////////////////////////////////////////////////////////

    private Database db;

    ////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////

    RemoteMultipartDownloaderRequest(
            HttpClientFactory clientFactory,
            String method,
            URL url,
            boolean cancelable,
            Map<String, ?> body,
            Database db,
            Map<String, Object> requestHeaders,
            RemoteRequestCompletion onCompletion) {
        super(clientFactory, method, url, cancelable, body, requestHeaders, onCompletion);
        this.db = db;
    }

    ////////////////////////////////////////////////////////////
    // Override methods
    ////////////////////////////////////////////////////////////

    /**
     * set headers
     */
    protected Request.Builder addHeaders(Request.Builder builder) {
        builder.addHeader("Accept", "multipart/related, application/json");
        builder.addHeader("User-Agent", Manager.getUserAgent());
        builder.addHeader("Accept-Encoding", "gzip, deflate");
        builder.addHeader("X-Accept-Part-Encoding", "gzip"); // <-- removable??
        return addRequestHeaders(builder);
    }

    /**
     * Execute request
     */
    protected void executeRequest(OkHttpClient httpClient, Request request) {
        Object fullBody = null;
        Throwable error = null;
        Response response = null;
        try {
            try {
                Log.v(TAG, "%s: RemoteMultipartDownloaderRequest call execute(), url: %s", this, url);
                call = httpClient.newCall(request);
                response = call.execute();
                Log.v(TAG, "%s: RemoteMultipartDownloaderRequest called execute(), url: %s", this, url);
                storeCookie(response);
                // error
                if (response.code() >= 300) {
                    Log.w(TAG, "%s: Got error status: %d for %s. Reason: %s",
                            this, response.code(), url, response.message());
                    error = new RemoteRequestResponseException(response.code(), response.message());
                    RequestUtils.closeResponseBody(response);
                }
                // success
                else {
                    ResponseBody responseBody = response.body();
                    InputStream stream = responseBody.byteStream();
                    try {
                        // decompress if contentEncoding is gzip
                        if (Utils.isGzip(response))
                            stream = new GZIPInputStream(stream);
                        MediaType type = responseBody.contentType();
                        if (type != null) {
                            // multipart
                            if (type.type().equals("multipart") &&
                                    type.subtype().equals("related")) {
                                MultipartDocumentReader reader = new MultipartDocumentReader(db);
                                reader.setHeaders(Utils.headersToMap(response.headers()));
                                byte[] buffer = new byte[BUF_LEN];
                                int numBytesRead;
                                while ((numBytesRead = stream.read(buffer)) != -1)
                                    reader.appendData(buffer, 0, numBytesRead);
                                reader.finish();
                                fullBody = reader.getDocumentProperties();
                            }
                            // JSON (non-multipart)
                            else
                                fullBody = Manager.getObjectMapper().readValue(
                                        stream, Object.class);
                        }
                    } finally {
                        try {
                            stream.close();
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                // call.execute(), GZIPInputStream, or ObjectMapper.readValue()
                Log.w(TAG, "%s: executeRequest() Exception: %s.  url: %s", this, e, url);
                error = e;
            }
            respondWithResult(fullBody, error, response);
        } finally {
            RequestUtils.closeResponseBody(response);
        }
    }
}
