package com.github.rkdharun.flexidesk.config;

import com.github.rkdharun.flexidesk.constants.AppConstant;
import com.github.rkdharun.flexidesk.utilities.SelfSignedCertificateGenerator;

import javax.net.ssl.*;
import java.security.KeyStore;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class ConnectionConfiguration {

  TrustManager[] trustAllCerts;
  KeyStore keyStore = null;

  SSLContext sslContext = null;
  KeyManagerFactory keyManagerFactory = null;
  SelfSignedCertificateGenerator cg;
  SSLServerSocketFactory sslServerSocketFactory = null;
  SSLSocket sslSocket = null;

  public static int APP_MODE;

  public ConnectionConfiguration() {
    try {
      //create a dummy trust manager to trust all the certificates
      trustAllCerts = new TrustManager[]{new DefaultTrustManager()};

      //initialize a key store
      keyStore = KeyStore.getInstance("PKCS12");
      keyStore.load(null, null);

      //initialize a key manager factory
      keyManagerFactory = KeyManagerFactory.getInstance("SunX509");

      //get the ssl context
      sslContext = SSLContext.getInstance("TLS");

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void setDefaultCertificate() {

    //create certificate generator object
     cg = new SelfSignedCertificateGenerator();

    //create a certificate chain to hold the returned certificate
    X509Certificate[] certificateChain = new X509Certificate[0];

    try {
      //create a self-signed certificate
      certificateChain = cg.generate();
      //add the key and certificate chain to our keystore
      keyStore.setKeyEntry("alias", cg.keyPair.getPrivate(), "password".toCharArray(), certificateChain);

      //initialize the KeyManagerFactory with our keystore object with password
      keyManagerFactory.init(keyStore, AppConstant.CERTIFICATE_PASSWORD.toCharArray());

      //initialize the ssl context
      sslContext.init(keyManagerFactory.getKeyManagers(),
        new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());

      //get ServerSocketFactory object for ssl server creation
      sslServerSocketFactory = sslContext.getServerSocketFactory();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public SSLContext getSslContext() {
    return sslContext;
  }

  public static int getAppMode() {
    return APP_MODE;
  }

  public SSLServerSocketFactory getSslServerSocketFactory() {
    return sslServerSocketFactory;
  }


  public SSLSocket getSslSocket() {
    return sslSocket;
  }
}

