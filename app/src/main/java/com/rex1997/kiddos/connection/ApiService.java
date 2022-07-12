package com.rex1997.kiddos.connection;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.auth.AUTH;
import cz.msebera.android.httpclient.auth.AuthScope;
import cz.msebera.android.httpclient.auth.MalformedChallengeException;
import cz.msebera.android.httpclient.auth.UsernamePasswordCredentials;
import cz.msebera.android.httpclient.client.AuthCache;
import cz.msebera.android.httpclient.client.CredentialsProvider;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.protocol.HttpClientContext;
import cz.msebera.android.httpclient.config.RegistryBuilder;
import cz.msebera.android.httpclient.conn.socket.ConnectionSocketFactory;
import cz.msebera.android.httpclient.conn.socket.PlainConnectionSocketFactory;
import cz.msebera.android.httpclient.conn.ssl.NoopHostnameVerifier;
import cz.msebera.android.httpclient.conn.ssl.SSLConnectionSocketFactory;
import cz.msebera.android.httpclient.impl.auth.BasicScheme;
import cz.msebera.android.httpclient.impl.auth.DigestScheme;
import cz.msebera.android.httpclient.impl.client.BasicAuthCache;
import cz.msebera.android.httpclient.impl.client.BasicCredentialsProvider;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.impl.conn.PoolingHttpClientConnectionManager;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.ssl.SSLContextBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ApiService {
    // Test get URL.
    static String sGetURL = "https://192.168.0.1/info";
    // Test post URL
    static String sPostURL = "https://api.everypixel.com/v1/faces";

    // Fetch a URL
    //      Authentication
    //          Assumes user and password are required
    //          Will determine if Basic or Digest authentication is needed based on response header
    //      HTTP Verb
    //          If "postParams" is not null, will use POST, otherwise will use GET
    //
    public void URLFetch(String sURL, String sUser, String sPW, List<NameValuePair> postParams,
                         boolean bIgnoreCerts) {

        try {
            // Create empty objects for POST and GET since we don't know which we'll use
            // below
            HttpPost httppost;
            HttpGet httpget;

            // Crate a URL object
            URL url = new URL(sURL);

            // Now create the HttpHost object from the URL
            HttpHost targetHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());

            // Here we need an HTTP client either with or without the SSLContext that
            // ignores certs
            CloseableHttpClient httpClient;
            if (bIgnoreCerts) {
                // Create an SSL context that accepts certs regardless of name match or trust (for self-signed)
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (x509CertChain, authType) -> true)
                        .build();

                httpClient = HttpClientBuilder.create().setSSLContext(sslContext)
                        .setConnectionManager(new PoolingHttpClientConnectionManager(RegistryBuilder
                                .<ConnectionSocketFactory>create()
                                .register("http", PlainConnectionSocketFactory.INSTANCE)
                                .register("https",
                                        new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                                .build()))
                        .build();
            } else {
                httpClient = HttpClients.createDefault();
            }

            // The HttpCLientContext
            final HttpClientContext context = HttpClientContext.create();

            // We'll need to allocate the response object below depending on type
            CloseableHttpResponse response = null;
            try {
                if (postParams != null) {
                    httppost = new HttpPost(sURL);
                    // Get the response
                    response = httpClient.execute(targetHost, httppost, context);
                } else {
                    httpget = new HttpGet(sURL);
                    // Get the response
                    response = httpClient.execute(targetHost, httpget, context);
                }
            } catch (SSLHandshakeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Add credentials for digest header
            assert response != null;
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                // Change to just pass user and password
                Header authHeader = response.getFirstHeader(AUTH.WWW_AUTH);
                HeaderElement[] element = authHeader.getElements();
                if (element.length != 0) {
                    AuthCache authCache = new BasicAuthCache();
                    CredentialsProvider credsProvider = new BasicCredentialsProvider();
                    credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(sUser, sPW));
                    if (element[0].getName().startsWith("Basic")) {
                        authCache.put(targetHost, new BasicScheme());
                    } else if (element[0].getName().startsWith("Digest")) {
                        DigestScheme digestScheme = new DigestScheme();
                        digestScheme.overrideParamter("realm", "thermostat");
                        digestScheme.processChallenge(authHeader);
                        authCache.put(targetHost, digestScheme);
                    }
                    context.setCredentialsProvider(credsProvider);
                    context.setAuthCache(authCache);
                }
            }

            // This ensures that the resource gets cleaned up
            if (postParams != null) {
                httppost = new HttpPost(sURL);
                httppost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));

                // Get the response
                response = httpClient.execute(targetHost, httppost, context);
            } else {
                httpget = new HttpGet(sURL);
                // Get the response
                response = httpClient.execute(targetHost, httpget, context);
            }

            // Get the data
            HttpEntity entity = response.getEntity();
            System.out.println(response.getStatusLine());
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
            }

            try {

                try (BufferedReader in = new BufferedReader(new InputStreamReader(entity != null ? entity.getContent() : null))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(sURL + " : " + inputLine);
                    }
                    EntityUtils.consume(entity);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } finally {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | MalformedChallengeException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getLatestImage (){
        File imagePath = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + "com.rex1997.kiddos"
                + "/Files");
        FileFilter imageFilter = new WildcardFileFilter("*.jpg");
        File[] images = imagePath.listFiles(imageFilter);

        assert images != null;
        int imagesCount = images.length; // get the list of images from folder
        Bitmap getImage = BitmapFactory.decodeFile(images[imagesCount - 1].getAbsolutePath());

    }

    private String latestImage (Bitmap bitmap){
        File imagePath = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + "com.rex1997.kiddos"
                + "/Files");
        FileFilter imageFilter = new WildcardFileFilter("*.jpg");
        File[] images = imagePath.listFiles(imageFilter);

        assert images != null;
        int imagesCount = images.length; // get the list of images from folder
        Bitmap getImage = BitmapFactory.decodeFile(images[imagesCount - 1].getAbsolutePath());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();

        // Get the Base64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    // Main, takes 4 arguments (see println below)
    public static void main(String[] args) {
        String username= "6aywCResze0K9kh8u70Ky8WG";
        String password = "crD8hhONleq3AGHB7fPuIKdv9Iln8M3iQeOwx0Qz8gvlHcyv";

        List<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("away", "1"));
        ApiService d = new ApiService();

        d.URLFetch(sPostURL, username, password, postParameters, false);
        d.URLFetch(sGetURL, username, password, null, true);
    }
}
