package com.rex1997.kiddos;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.utils.JSONUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AccessToken2 {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) throws OAuthSystemException, OAuthProblemException {

        String CLIENT_ID = "6aywCResze0K9kh8u70Ky8WG";
        String CLIENT_SECRET = "crD8hhONleq3AGHB7fPuIKdv9Iln8M3iQeOwx0Qz8gvlHcyv";
        String TOKEN_REQUEST_URL = "https://api.everypixel.com/oauth/token";
        String SCOPE = "keywording";
        String REDIRECT_URL ="http://localhost:3000/tokens";

        String finalToken;
        String bearerValue;
        String encodeValue = getBase64Encoder(CLIENT_ID, CLIENT_SECRET);

        try {

            OAuthClient client = new OAuthClient(new URLConnectionClient());

            OAuthClientRequest request = OAuthClientRequest
                    .tokenLocation(TOKEN_REQUEST_URL)
                    .setGrantType(GrantType.CLIENT_CREDENTIALS)
                    .setClientId(CLIENT_ID)
                    .setClientSecret(CLIENT_SECRET)
                    .buildBodyMessage();

            System.out.println(request.getBody());
            request.addHeader("Authorization", encodeValue);
            OAuthJSONAccessTokenResponse oAuthResponse = client.accessToken(request, OAuth.HttpMethod.POST, OAuthJSONAccessTokenResponse.class);

            finalToken = oAuthResponse.getAccessToken();
            bearerValue = "Bearer " + finalToken;
            System.out.println(bearerValue);
        } catch (Exception exn) {
            throw OAuthProblemException.error(OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE,
                    "Invalid response! Response body is not " + OAuth.ContentType.JSON + " encoded");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getBase64Encoder(String id, String password){
        return Base64.getEncoder().encodeToString((id + "+" + password).getBytes(StandardCharsets.UTF_8));
    }
}
