package utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.GooglePojo;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

public class GoogleUtils {
    public static String getToken(final String code) throws IOException {
        String response = Request.Post("https://accounts.google.com/o/oauth2/token")
                .bodyForm(Form.form()
                        .add("client_id", "1089942878583-pq5ui5eubco8s2lav650ln4gn19gogfe.apps.googleusercontent.com")
                        .add("client_secret", "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")
                        .add("redirect_uri", "http://localhost:8080/demo/google-login")
                        .add("code", code)
                        .add("grant_type", "authorization_code").build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        return jobj.get("access_token").toString().replaceAll("\"", "");
    }

    public static GooglePojo getUserInfo(final String accessToken) throws IOException {
        String link = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        return new Gson().fromJson(response, GooglePojo.class);
    }
}
