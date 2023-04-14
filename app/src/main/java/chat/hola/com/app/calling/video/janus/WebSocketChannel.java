package chat.hola.com.app.calling.video.janus;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.calling.myapplication.utility.CallStatus;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

public class WebSocketChannel {
    private static final String TAG = "WebSocketChannel";

    private WebSocket mWebSocket;
    private ConcurrentHashMap<String, JanusTransaction> transactions = new ConcurrentHashMap<>();
    private ConcurrentHashMap<BigInteger, JanusHandle> handles = new ConcurrentHashMap<>();
    private ConcurrentHashMap<BigInteger, JanusHandle> feeds = new ConcurrentHashMap<>();
    private Handler mHandler;
    private BigInteger mSessionId;
    private JanusRTCInterface delegate;

    public WebSocketChannel() {
        mHandler = new Handler();
    }

    private SessionIdInterface sessionIdInterface;

    public interface SessionIdInterface {
        void onSessionId(BigInteger handleId);
    }

    public void initConnection(String url, Long roomId, CallStatus callStatus,SessionIdInterface sessionIdInterface) {
        OkHttpClient httpClient =
                new OkHttpClient.Builder()
                        .addNetworkInterceptor(
                                new HttpLoggingInterceptor()
                                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addInterceptor(
                                chain -> {
                                    Request.Builder builder = chain.request().newBuilder();
                                    builder.addHeader("Sec-WebSocket-Protocol", "janus-protocol");
                                    return chain.proceed(builder.build());
                                })
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();
        Request request = new Request.Builder().url(url).build();
        mWebSocket =
                httpClient.newWebSocket(
                        request,
                        new WebSocketListener() {
                            @Override
                            public void onOpen(WebSocket webSocket, Response response) {
                                Log.e(TAG, "onOpen");
                                createSession(roomId, callStatus,sessionIdInterface);
                            }

                            @Override
                            public void onMessage(WebSocket webSocket, String text) {
                                Log.e(TAG, "onMessage");
                                WebSocketChannel.this.onMessage(text, roomId);
                            }

                            @Override
                            public void onMessage(WebSocket webSocket, ByteString bytes) {}

                            @Override
                            public void onClosing(WebSocket webSocket, int code, String reason) {
                                Log.e(TAG, "onClosing");
                            }

                            @Override
                            public void onClosed(WebSocket webSocket, int code, String reason) {
                                Log.e(TAG, "onClosed " + code + " " + reason);
                            }

                            @Override
                            public void onFailure(
                                    WebSocket webSocket, Throwable t, Response response) {
                                Log.e(TAG, "onFailure" + t.toString());
                            }
                        });
    }

    /** used to close the socket */
    public void closeSocket() {
        boolean closed = mWebSocket.close(1000, "close");
        Log.d(TAG, "closeSocket: " + closed);
    }

    private void onMessage(String message, Long roomId) {
        Log.e(TAG, "onMessage" + message);
        try {
            JSONObject jo = new JSONObject(message);
            String janus = jo.optString("janus");
            if (janus.equals("success")) {
                String transaction = jo.optString("transaction");
                JanusTransaction jt = transactions.get(transaction);
                if (jt.success != null) {
                    jt.success.success(jo);
                }
                transactions.remove(transaction);
            } else if (janus.equals("error")) {
                String transaction = jo.optString("transaction");
                JanusTransaction jt = transactions.get(transaction);
                if (jt.error != null) {
                    jt.error.error(jo);
                }
                transactions.remove(transaction);
            } else if (janus.equals("ack")) {
                Log.e(TAG, "Just an ack");
            } else {
                JanusHandle handle = handles.get(new BigInteger(jo.optString("sender")));
                if (handle == null) {
                    Log.e(TAG, "missing handle");
                } else if (janus.equals("event")) {
                    JSONObject plugin = jo.optJSONObject("plugindata").optJSONObject("data");
                    if (plugin.optString("videoroom").equals("joined")) {
                        handle.onJoined.onJoined(handle);
                    }

                    JSONArray publishers = plugin.optJSONArray("publishers");
                    if (publishers != null && publishers.length() > 0) {
                        for (int i = 0, size = publishers.length(); i <= size - 1; i++) {
                            JSONObject publisher = publishers.optJSONObject(i);
                            BigInteger feed = new BigInteger(publisher.optString("id"));
                            String display = publisher.optString("display");
                            subscriberCreateHandle(feed, display, roomId);
                        }
                    }

                    String leaving = plugin.optString("leaving");
                    if (!TextUtils.isEmpty(leaving)) {
                        JanusHandle jhandle = feeds.get(new BigInteger(leaving));
                        jhandle.onLeaving.onJoined(jhandle);
                    }

                    JSONObject jsep = jo.optJSONObject("jsep");
                    if (jsep != null) {
                        handle.onRemoteJsep.onRemoteJsep(handle, jsep);
                    }

                } else if (janus.equals("detached")) {
                    handle.onLeaving.onJoined(handle);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createSession(Long roomId, CallStatus callStatus, SessionIdInterface sessionIdInterface) {
        String transaction = randomString(12);
        JanusTransaction jt = new JanusTransaction();
        jt.tid = transaction;
        jt.success =
                jo -> {
                    mSessionId = new BigInteger(jo.optJSONObject("data").optString("id"));
                    sessionIdInterface.onSessionId(mSessionId);
                    mHandler.post(fireKeepAlive);
                    publisherCreateHandle(roomId, callStatus);
                };
        jt.error = jo -> {};
        transactions.put(transaction, jt);
        JSONObject msg = new JSONObject();
        try {
            msg.putOpt("janus", "create");
            msg.putOpt("transaction", transaction);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " send request: create " + msg.toString());
        mWebSocket.send(msg.toString());
    }

    private void publisherCreateHandle(Long roomId, CallStatus callStatus) {
        String transaction = randomString(12);
        JanusTransaction jt = new JanusTransaction();
        jt.tid = transaction;
        jt.success =
                jo -> {
                    JanusHandle janusHandle = new JanusHandle();
                    janusHandle.handleId = new BigInteger(jo.optJSONObject("data").optString("id"));
                    janusHandle.onJoined = jh -> delegate.onPublisherJoined(jh.handleId);
                    janusHandle.onRemoteJsep =
                            (jh, jsep) -> delegate.onPublisherRemoteJsep(jh.handleId, jsep);
                    handles.put(janusHandle.handleId, janusHandle);
                    switch (callStatus) {
                        case CALLING:
                            createRoom(roomId, janusHandle);
                            break;

                        case NEW_CALL:
                            publisherJoinRoom(janusHandle, roomId);
                            break;
                    }
                };
        jt.error = jo -> {};
        transactions.put(transaction, jt);
        JSONObject msg = new JSONObject();
        try {
            msg.putOpt("janus", "attach");
            msg.putOpt("plugin", "janus.plugin.videoroom");
            msg.putOpt("transaction", transaction);
            msg.putOpt("session_id", mSessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " send request: attach " + msg.toString());
        mWebSocket.send(msg.toString());
    }

    private void createRoom(Long roomId, JanusHandle janusHandle) {
        String transaction = randomString(12);
        JanusTransaction jt = new JanusTransaction();
        jt.tid = transaction;
        jt.success = jo -> publisherJoinRoom(janusHandle, roomId);
        jt.error =
                jo -> {
                    Log.d(TAG, " send request: create room error  " + jo.toString());
                };
        transactions.put(transaction, jt);
        JSONObject request = new JSONObject();
        JSONObject message = new JSONObject();
        try {
            request.putOpt("request", "create");
            request.putOpt("room", roomId);
            request.putOpt("is_private", false);

            System.out.println("bitrate: " + Utilities.bitrateValue());
           // request.putOpt("bitrate", Utilities.bitrateValue());
            request.putOpt("audiocodec", "opus");
            request.putOpt("videocodec", "VP8"); // VP9
            request.putOpt("videoorient_ext", true);

            message.putOpt("janus", "message");
            message.putOpt("body", request);
            message.putOpt("transaction", transaction);
            message.putOpt("session_id", mSessionId);
            message.putOpt("handle_id", janusHandle.handleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " send request: create room " + message.toString());
        mWebSocket.send(message.toString());
    }

    private void publisherJoinRoom(JanusHandle handle, Long roomId) {
        JSONObject msg = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            body.putOpt("request", "join");
            body.putOpt("room", roomId);
            body.putOpt("ptype", "publisher");
            body.putOpt("display", "Android webrtc");

            System.out.println("bitrate: " + Utilities.bitrateValue());
           // body.putOpt("bitrate", Utilities.bitrateValue());
            body.putOpt("audiocodec", "opus");
            body.putOpt("videocodec", "VP8");
            body.putOpt("videoorient_ext", true);

            msg.putOpt("janus", "message");
            msg.putOpt("body", body);
            msg.putOpt("transaction", randomString(12));
            msg.putOpt("session_id", mSessionId);
            msg.putOpt("handle_id", handle.handleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " publisher send request: join " + msg.toString());
        mWebSocket.send(msg.toString());
    }

    public void publisherCreateOffer(final BigInteger handleId, final SessionDescription sdp,Boolean isVideoEnable) {
        JSONObject publish = new JSONObject();
        JSONObject jsep = new JSONObject();
        JSONObject message = new JSONObject();
        try {
            publish.putOpt("request", "configure");
            publish.putOpt("audio", true);
            publish.putOpt("video", isVideoEnable);

            jsep.putOpt("type", sdp.type);
            jsep.putOpt("sdp", sdp.description);

            message.putOpt("janus", "message");
            message.putOpt("body", publish);
            message.putOpt("jsep", jsep);
            message.putOpt("transaction", randomString(12));
            message.putOpt("session_id", mSessionId);
            message.putOpt("handle_id", handleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " send request: configure " + message.toString());
        mWebSocket.send(message.toString());
    }

    public void muteOpponent(final BigInteger handleId) {
        JSONObject publish = new JSONObject();
        JSONObject jsep = new JSONObject();
        JSONObject message = new JSONObject();
        try {
            publish.putOpt("request", "configure");
            publish.putOpt("audio", false);

            message.putOpt("janus", "message");
            message.putOpt("body", publish);
            message.putOpt("transaction", randomString(12));
            message.putOpt("session_id", mSessionId);
            message.putOpt("handle_id", handleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " send request: audio false " + message.toString());
        mWebSocket.send(message.toString());
    }

    public void subscriberCreateAnswer(
            final BigInteger handleId, final SessionDescription sdp, Long roomId) {
        JSONObject body = new JSONObject();
        JSONObject jsep = new JSONObject();
        JSONObject message = new JSONObject();

        try {
            body.putOpt("request", "start");
            body.putOpt("room", roomId);

            jsep.putOpt("type", sdp.type);
            jsep.putOpt("sdp", sdp.description);
            message.putOpt("janus", "message");
            message.putOpt("body", body);
            message.putOpt("jsep", jsep);
            message.putOpt("transaction", randomString(12));
            message.putOpt("session_id", mSessionId);
            message.putOpt("handle_id", handleId);
            Log.e(TAG, "-------------" + message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " send request: start " + message.toString());
        mWebSocket.send(message.toString());
    }

    public void trickleCandidate(final BigInteger handleId, final IceCandidate iceCandidate) {
        JSONObject candidate = new JSONObject();
        JSONObject message = new JSONObject();
        try {
            candidate.putOpt("candidate", iceCandidate.sdp);
            candidate.putOpt("sdpMid", iceCandidate.sdpMid);
            candidate.putOpt("sdpMLineIndex", iceCandidate.sdpMLineIndex);

            message.putOpt("janus", "trickle");
            message.putOpt("candidate", candidate);
            message.putOpt("transaction", randomString(12));
            message.putOpt("session_id", mSessionId);
            message.putOpt("handle_id", handleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " send request: trickle " + message.toString());
        mWebSocket.send(message.toString());
    }

    public void disconnectPeerConnection() {
        JSONObject message = new JSONObject();
        try {
            message.putOpt("janus", "hangup");
            message.putOpt("transaction", randomString(12));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " send request: disconnect " + message.toString());
        mWebSocket.send(message.toString());
    }

    public void trickleCandidateComplete(final BigInteger handleId) {
        JSONObject candidate = new JSONObject();
        JSONObject message = new JSONObject();
        try {
            candidate.putOpt("completed", true);

            message.putOpt("janus", "trickle");
            message.putOpt("candidate", candidate);
            message.putOpt("transaction", randomString(12));
            message.putOpt("session_id", mSessionId);
            message.putOpt("handle_id", handleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void subscriberCreateHandle(final BigInteger feed, final String display, Long roomId) {
        String transaction = randomString(12);
        JanusTransaction jt = new JanusTransaction();
        jt.tid = transaction;
        jt.success =
                jo -> {
                    JanusHandle janusHandle = new JanusHandle();
                    janusHandle.handleId = new BigInteger(jo.optJSONObject("data").optString("id"));
                    janusHandle.feedId = feed;
                    janusHandle.display = display;
                    janusHandle.onRemoteJsep =
                            (jh, jsep) -> delegate.subscriberHandleRemoteJsep(jh.handleId, jsep);
                    janusHandle.onLeaving = jh -> subscriberOnLeaving(jh);
                    handles.put(janusHandle.handleId, janusHandle);
                    feeds.put(janusHandle.feedId, janusHandle);
                    subscriberJoinRoom(janusHandle, roomId);
                };
        jt.error = jo -> Log.d(TAG, "error: " + jo.toString());

        transactions.put(transaction, jt);
        JSONObject msg = new JSONObject();
        try {
            msg.putOpt("janus", "attach");
            msg.putOpt("plugin", "janus.plugin.videoroom");
            msg.putOpt("transaction", transaction);
            msg.putOpt("session_id", mSessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " send request: attach " + msg.toString());
        mWebSocket.send(msg.toString());
    }

    private void subscriberJoinRoom(JanusHandle handle, Long roomId) {

        JSONObject msg = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            body.putOpt("request", "join");
            body.putOpt("room", roomId);
            body.putOpt("ptype", "listener");
            body.putOpt("feed", handle.feedId);

            msg.putOpt("janus", "message");
            msg.putOpt("body", body);
            msg.putOpt("transaction", randomString(12));
            msg.putOpt("session_id", mSessionId);
            msg.putOpt("handle_id", handle.handleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " subscriber send request: join " + msg.toString());
        mWebSocket.send(msg.toString());
    }

    private void subscriberOnLeaving(final JanusHandle handle) {
        String transaction = randomString(12);
        JanusTransaction jt = new JanusTransaction();
        jt.tid = transaction;
        jt.success =
                new TransactionCallbackSuccess() {
                    @Override
                    public void success(JSONObject jo) {
                        delegate.onLeaving(handle.handleId);
                        handles.remove(handle.handleId);
                        feeds.remove(handle.feedId);
                    }
                };
        jt.error =
                new TransactionCallbackError() {
                    @Override
                    public void error(JSONObject jo) {
                        Log.d(TAG, "error: TransactionCallbackError " + jo.toString());
                    }
                };

        transactions.put(transaction, jt);

        JSONObject jo = new JSONObject();
        try {
            jo.putOpt("janus", "detach");
            jo.putOpt("transaction", transaction);
            jo.putOpt("session_id", mSessionId);
            jo.putOpt("handle_id", handle.handleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " send request: detach " + jo.toString());
        mWebSocket.send(jo.toString());
    }

    private void keepAlive() {
        String transaction = randomString(12);
        JSONObject msg = new JSONObject();
        try {
            msg.putOpt("janus", "keepalive");
            msg.putOpt("session_id", mSessionId);
            msg.putOpt("transaction", transaction);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " send request: keepalive " + msg.toString());
        mWebSocket.send(msg.toString());
    }

    private Runnable fireKeepAlive =
            new Runnable() {
                @Override
                public void run() {
                    keepAlive();
                    mHandler.postDelayed(fireKeepAlive, 30000);
                }
            };

    public void setDelegate(JanusRTCInterface delegate) {
        this.delegate = delegate;
    }

    private String randomString(Integer length) {
        final String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(rnd.nextInt(str.length())));
        }
        return sb.toString();
    }
}
