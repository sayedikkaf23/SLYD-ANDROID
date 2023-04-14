package chat.hola.com.app.live_stream.pubsub;

import android.content.Context;
import android.util.Log;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import chat.hola.com.app.Networking.SocketFactory;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.live_stream.MQtt.CustomMQtt.IMqttActionListener;
import chat.hola.com.app.live_stream.MQtt.CustomMQtt.IMqttDeliveryToken;
import chat.hola.com.app.live_stream.MQtt.CustomMQtt.IMqttToken;
import chat.hola.com.app.live_stream.MQtt.CustomMQtt.MqttCallback;
import chat.hola.com.app.live_stream.MQtt.CustomMQtt.MqttConnectOptions;
import chat.hola.com.app.live_stream.MQtt.CustomMQtt.MqttMessage;
import chat.hola.com.app.live_stream.MQtt.MQttMessages.AllStreams;
import chat.hola.com.app.live_stream.MQtt.MQttMessages.ParticularStreamChat;
import chat.hola.com.app.live_stream.MQtt.MQttMessages.ParticularStreamGift;
import chat.hola.com.app.live_stream.MQtt.MQttMessages.ParticularStreamLike;
import chat.hola.com.app.live_stream.MQtt.MQttMessages.ParticularStreamPresence;
import chat.hola.com.app.live_stream.MQtt.MQttMessages.ParticularStreamRestart;
import chat.hola.com.app.live_stream.MQtt.MqttAndroidClient;
import chat.hola.com.app.manager.session.SessionManager;

/**
 * <h1>MQTTManager</h1>
 * This class is used to handle the MQTT data
 *
 * @author 3Embed
 * @since on 21-12-2017.
 */
public class MQTTManager {

    private static final String TAG = "MQTTManager";
    private IMqttActionListener mMQTTListener;
    private MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions mqttConnectOptions;
    private Context mContext;

    private Gson gson;
    private SessionManager sessionManager;
    private AllStreams allStreams;
    private ParticularStreamChat particularStreamChat;
    private ParticularStreamPresence particularStreamPresence;
    private ParticularStreamGift particularStreamGift;
    private ParticularStreamLike particularStreamLike;
    private ParticularStreamRestart particularStreamRestart;

    @Inject
    public MQTTManager(Context context, SessionManager sessionManager, Gson gson) {

        allStreams = new AllStreams();
        particularStreamChat = new ParticularStreamChat();
        particularStreamPresence = new ParticularStreamPresence();

        particularStreamLike = new ParticularStreamLike();
        particularStreamGift = new ParticularStreamGift();
        particularStreamRestart = new ParticularStreamRestart();


        mContext = context;
        this.gson = gson;

        this.sessionManager = sessionManager;
        mMQTTListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d("log1", "mqtt connected");
                updateConnected();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.d("log1", "failed to connect");
                exception.printStackTrace();
                updateDisconnected();
            }
        };
    }

    /**
     * <h2>subscribeToTopic</h2>
     * This method is used to subscribe to the mqtt topic
     */
    public void subscribeToTopic(String mqttTopic, int qos) {
        try {
            if (mqttAndroidClient != null) {
                mqttAndroidClient.subscribe(mqttTopic, qos);
            }
        } catch (MqttException e) {

            e.printStackTrace();
        }
    }

    /**
     * <h2>unSubscribeFromTopic</h2>
     * This method is used to unSubscribe to topic already subscribed
     *
     * @param topic Topic name from which to  unSubscribe
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    public void unSubscribeFromTopic(String topic) {
        try {
            if (mqttAndroidClient != null)
                mqttAndroidClient.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * <h2>isMQttConnected</h2>
     * This method is used to check whether MQTT is connected
     *
     * @return boolean value whether MQTT is connected
     */
    public boolean isMQttConnected() {
        return mqttAndroidClient != null && mqttAndroidClient.isConnected();
    }

    /**
     * <h2>connectMQttClient</h2>
     * This method is used to create the connection with MQTT
     *
     * @param clientId customer ID to connect MQTT
     */
    @SuppressWarnings("unchecked")
    public void connectMQttClient(String clientId) {


        String serverUri =  ApiOnServer.HOST_API_MQTT + ":" + ApiOnServer.LIVE_MQTT_PORT;
//        String serverUri = "ssl://mqtt.vybesclub.com:8883";

        if (mqttAndroidClient == null) {
            mqttAndroidClient = new MqttAndroidClient(mContext, serverUri, clientId + (System.currentTimeMillis() / 1000));
            mqttAndroidClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                    updateDisconnected();
                    Log.d("log1", "mqtt connection lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {


                    //                  Log.d("log1", "mqtt message arrived");
//                    Log.d("log1", "messageArrived: topic " + topic);
//                    Log.d("log1", "messageArrived: message " + message);

                    String[] topicSplit = topic.split("/");


//                if (topic.equals(sessionManagerImpl.getUserId())) {
//                    handleNewCall(new String(message.getPayload()));
//
//                } else if (topic.equals(MqttEvents.Call.value + "/" + UtilityVideoCall.getInstance().getActiveCallId())) {
//                    handleActiveCall(new String(message.getPayload()));
//
//                } else
//

                    if (topicSplit[0].equals(MqttEvents.AllStreams.value)) {
                        allStreams.handleAllStreamsMessage(new String(message.getPayload()), gson);

                    } else if (topicSplit[0].equals(MqttEvents.ParticularStreamPresenceEvents.value)) {

                        String payload = new String(message.getPayload());

                        JSONObject obj = new JSONObject(payload);

                        if (obj.has("data")) {
                            particularStreamPresence.handleParticularStreamPresenceEvent(payload, gson, topicSplit[1]);
                        } else {
                            particularStreamRestart.handleParticularStreamRestartEvent(payload, gson);
                        }

                    } else if (topicSplit[0].equals(MqttEvents.ParticularStreamChatMessages.value)) {
                        particularStreamChat.handleParticularStreamChatMessage(new String(message.getPayload()), gson, topicSplit[1]);

                    } else if (topicSplit[0].equals(MqttEvents.ParticularStreamLikeEvent.value)) {
                        particularStreamLike.handleParticularStreamLikeEvent(new String(message.getPayload()), gson);

                    } else if (topicSplit[0].equals(MqttEvents.ParticularStreamGiftEvent.value)) {
                        particularStreamGift.handleParticularStreamGiftEvent(new String(message.getPayload()), gson);

                    }


                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.w(TAG, " deliveryComplete: " + token);
                }
            });
        }
        byte[] payload = sessionManager.getUserId().getBytes();

        if (mqttConnectOptions == null) {
            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setKeepAliveInterval(60);
            mqttConnectOptions.setMaxInflight(1000);

            /* Use below code of socket factory when ssl required in MQTT*/
//            SocketFactory.SocketFactoryOptions socketFactoryOptions = new SocketFactory.SocketFactoryOptions();
//            try {
//                socketFactoryOptions.withCaInputStream(mContext.getResources().openRawResource(R.raw.dochat_mqtt));
//                //socketFactoryOptions.withClientP12Password("");
//                //socketFactoryOptions.withClientP12InputStream(getResources().openRawResource(R.raw.dochat_mqtt));
//                mqttConnectOptions.setSocketFactory(new SocketFactory(socketFactoryOptions));
//            } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | UnrecoverableKeyException | CertificateException e) {
//                e.printStackTrace();
//            }
            //------------------------------end------------------------------//

            mqttConnectOptions.setUserName(BuildConfig.MQTT_USERNAME);
            mqttConnectOptions.setPassword(BuildConfig.MQTT_PASSWORD.toCharArray());

            mqttConnectOptions.setWill(MqttEvents.WillTopic.value, payload, 1, false);

//            try {
//                // Install the all-trusting trust manager
////                final SSLContext sslContext = SSLContext.getInstance("SSL");
////                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
////
////                sslContext.init(null, trustedCertificatesInputStream(), new java.security.SecureRandom());
//
////                mqttConnectOptions.setSocketFactory(getSocketFactory(R.raw.vybesclub,""));
//                mqttConnectOptions.setSocketFactory(createSSLSocketFactory());
//            }catch(Exception e){e.printStackTrace();}

        }
        connectMQttClient(mContext);
    }


//    public SSLSocketFactory getSocketFactory(int certificateId, String certificatePassword ) {
//        SSLSocketFactory result = mSocketFactoryMap.get(certificateId);
//        if ( ( null == result) && ( null != mContext ) ) {
//
//            try {
//                KeyStore keystoreTrust = KeyStore.getInstance("BKS");
//                keystoreTrust.load(mContext.getResources().
//                        openRawResource(certificateId),certificatePassword.toCharArray());
//                TrustManagerFactory trustManagerFactory =
//                        TrustManagerFactory.getInstance(TrustManagerFactory
//                                .getDefaultAlgorithm());
//                trustManagerFactory.init(keystoreTrust);
//                SSLContext sslContext = SSLContext.getInstance("TLS");
//                sslContext.init(null, trustManagerFactory.getTrustManagers(),
//                        new SecureRandom());
//                result = sslContext.getSocketFactory();
//                mSocketFactoryMap.put( certificateId, result);
//            }
//            catch ( Exception ex ) {
//            }
//        }
//        return result;
//    }
//    private HashMap<Integer, SSLSocketFactory> mSocketFactoryMap = new
//            HashMap<Integer, SSLSocketFactory>();


    private SSLSocketFactory createSSLSocketFactory()
    {

        try{ ProviderInstaller.installIfNeeded(mContext.getApplicationContext());}
        catch(Exception e){e.printStackTrace();}

        try
        {
//            CertificateFactory caCF = CertificateFactory.getInstance("X.509");
//            X509Certificate ca = (X509Certificate) caCF.generateCertificate(mContext.getResources().openRawResource(R.raw.vybesclub));
//
//            KeyStore caKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            caKeyStore.load(null, null);
//            caKeyStore.setCertificateEntry(ca.getSubjectX500Principal().getName(), ca);

//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            tmf.init(caKeyStore);

            SSLContext context = SSLContext.getInstance("SSL");//SSLContext.getInstance("TLS");
            context.init(null, trustManagerForCertificates(mContext.getResources().openRawResource(R.raw.dochat_mqtt)), new java.security.SecureRandom());
//            context.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
            return context.getSocketFactory();
        }
        catch (GeneralSecurityException     e)
        {
//            LOG.error("Creating ssl socket factory failed", e);
            return null;
        }
    }

    private static X509TrustManager[] trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
//        return (X509TrustManager) trustManagers[0];

    return (X509TrustManager[])trustManagers;
    }

    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
    /**
     * <h2>connectMQttClient</h2>
     * This method is used to connect to MQTT client
     */
    private void connectMQttClient(Context mContext) {
        try {
            if (mqttAndroidClient != null && !mqttAndroidClient.isConnected()) {


                mqttAndroidClient.connect(mqttConnectOptions, mContext, mMQTTListener);
            }
        } catch (MqttException e) {

            e.printStackTrace();
        }


    }

    /**
     * <h2>disconnect</h2>
     * This method is used To disconnect the MQtt client
     */
    public void disconnect() {
        try {
            if (mqttAndroidClient != null) {
                //  unSubscribeFromTopic(mqttTopic);

                mqttAndroidClient.disconnect();
//                mqttAndroidClient =null;
//
//                mqttConnectOptions=null;
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public void publish(String topicName, JSONObject obj, int qos, boolean retained) {

        try {

            if (mqttAndroidClient != null) {

                mqttAndroidClient.publish(topicName, obj.toString().getBytes(), qos, retained);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topicName, byte[] message, int qos, boolean retained) {

        try {

            if (mqttAndroidClient != null) {

                mqttAndroidClient.publish(topicName, message, qos, retained);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static Bus getBus() {
        return bus;
    }

    private static Bus bus = new Bus(ThreadEnforcer.ANY);

    public static void updateConnected() {
        try {

            JSONObject obj = new JSONObject();
            obj.put("eventName", MqttEvents.Connect.value);

            bus.post(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void updateDisconnected() {
        try {

            JSONObject obj = new JSONObject();
            obj.put("eventName", MqttEvents.Disconnect.value);

            bus.post(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
