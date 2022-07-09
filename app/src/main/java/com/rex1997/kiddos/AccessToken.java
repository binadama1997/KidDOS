package com.rex1997.kiddos;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AccessToken {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main (String [] args) throws OAuthSystemException, OAuthProblemException {

        String clientID = "6aywCResze0K9kh8u70Ky8WG";
        String clientSecret = "crD8hhONleq3AGHB7fPuIKdv9Iln8M3iQeOwx0Qz8gvlHcyv";
        String tokenURL = "https://api.everypixel.com/oauth/token";
        String scopeToken = "keywording";
        String siteURI ="http://localhost:3000/tokens";
        String fireBaseURI ="http://localhost:50199/";

        String finalToken;
        String bearerValue;
        String encodeValue = getBase64Encoder(clientID, clientSecret);

        OAuthClient client = new OAuthClient(new URLConnectionClient());

        OAuthClientRequest request = OAuthClientRequest
                .tokenLocation(tokenURL)
                .setClientId(clientID)
                .setClientSecret(clientSecret)
                .setGrantType(GrantType.CLIENT_CREDENTIALS)
                .buildBodyMessage();

        request.addHeader("Authorization", encodeValue);
        request.addHeader("Accept", "*/*");
        request.addHeader("Content-Type", "application/json");

        System.out.println(request.getBody());

        OAuthJSONAccessTokenResponse oAuthResponse = client.accessToken(request, OAuth.HttpMethod.POST, OAuthJSONAccessTokenResponse.class);

        finalToken = oAuthResponse.getAccessToken();
        bearerValue = "Bearer " + finalToken;
        System.out.println(bearerValue);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getBase64Encoder(String id, String password){
        return Base64.getEncoder().encodeToString((id + "+" + password).getBytes(StandardCharsets.UTF_8));
    }
}
