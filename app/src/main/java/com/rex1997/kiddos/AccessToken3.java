package com.rex1997.kiddos;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

public class AccessToken3
{
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main (String [] args) throws OAuthSystemException, OAuthProblemException {

        String clientID = "6aywCResze0K9kh8u70Ky8WG";
        String clientSecret = "crD8hhONleq3AGHB7fPuIKdv9Iln8M3iQeOwx0Qz8gvlHcyv";
        String tokenURL = "https://api.everypixel.com/oauth/token";
        String scopeToken = "keywording";
        String redirectUri ="http://localhost:3000/tokens";


        // create OAuth 2.0 Client using Apache HTTP Client
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        // construct OAuth 2.0 Token request for the authorization code
        OAuthClientRequest request = OAuthClientRequest
                .tokenLocation(tokenURL)
                .setGrantType(GrantType.CLIENT_CREDENTIALS)
                .setClientId(clientID)
                .setClientSecret(clientSecret)
                .setRedirectURI(redirectUri)
                .buildBodyMessage();

// request the token via the OAuth 2.0 client
        OAuthJSONAccessTokenResponse response = oAuthClient.accessToken(request);

// extract the data from the response
        String accessToken = response.getAccessToken();
        String refreshToken = response.getRefreshToken();
        String grantedScope = response.getScope();
        Long expiresIn = response.getExpiresIn();

        System.out.println(accessToken);
        System.out.println(refreshToken);
        System.out.println(grantedScope);
        System.out.println(expiresIn);

    }
}
