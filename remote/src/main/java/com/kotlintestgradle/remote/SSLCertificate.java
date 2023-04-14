package com.kotlintestgradle.remote;

import static com.kotlintestgradle.remote.RemoteConstants.ALIAS_NAME;
import static com.kotlintestgradle.remote.RemoteConstants.ARRAY_SIZE;
import static com.kotlintestgradle.remote.RemoteConstants.CERTIFICATE_TYPE;
import static com.kotlintestgradle.remote.RemoteConstants.CERTIFICATION_ERROR;
import static com.kotlintestgradle.remote.RemoteConstants.LOWER_BOUND;
import static com.kotlintestgradle.remote.RemoteConstants.REQUESTED_PROTOCOL;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLCertificate {
  public static SSLContext initSSL(Context context) {
    SSLContext sslContext = null;
    try {
      sslContext = createCertificate(context.getResources().openRawResource(R.raw.dochat));
    } catch (CertificateException | IOException | KeyStoreException | KeyManagementException
        | NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return sslContext;
  }

  private static SSLContext createCertificate(InputStream trustedCertificateIS)
      throws CertificateException, IOException, KeyStoreException, KeyManagementException,
      NoSuchAlgorithmException {
    CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE);
    Certificate ca;
    try {
      ca = cf.generateCertificate(trustedCertificateIS);
    } finally {
      trustedCertificateIS.close();
    }
    // creating a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry(ALIAS_NAME, ca);
    // creating a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);
    // creating an SSLSocketFactory that uses our TrustManager
    SSLContext sslContext = SSLContext.getInstance(REQUESTED_PROTOCOL);
    sslContext.init(null, tmf.getTrustManagers(), null);
    return sslContext;
  }

  public static X509TrustManager systemDefaultTrustManager() {
    try {
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
          TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init((KeyStore) null);
      TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
      if (trustManagers.length != ARRAY_SIZE
          || !(trustManagers[LOWER_BOUND] instanceof X509TrustManager)) {
        throw new IllegalStateException(
            CERTIFICATION_ERROR + Arrays.toString(trustManagers));
      }
      return (X509TrustManager) trustManagers[LOWER_BOUND];
    } catch (GeneralSecurityException e) {
      throw new AssertionError(); // The system has no TLS. Just give up.
    }
  }
}
