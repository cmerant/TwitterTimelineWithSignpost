/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oauthtwittertp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

/**
 *
 * @author catherine
 */
public class OauthTwitter {

    public static void main(String[] args) throws Exception {

        OAuthConsumer consumer = new DefaultOAuthConsumer(
            "iIlNngv1KdV6XzNYkoLA",
            "exQ94pBpLXFcyttvLoxU2nrktThrlsj580zjYzmoM");

        OAuthProvider provider = new DefaultOAuthProvider(
                "http://twitter.com/oauth/request_token",
                "http://twitter.com/oauth/access_token",
                "http://twitter.com/oauth/authorize");
        
        String token_file_name="OAuth_tokens.json";
        String home=System.getProperty("user.home");
        String fs=System.getProperty("file.separator");
        String token_file_dir="dir1";

        // String token_file_path="/home/catherine/Documents/";
        String token_file_path = home+fs+token_file_dir;
        String token_file_wholename = home+fs+token_file_dir+fs+token_file_name;

        String tok="", toksecr="";
        Tokens toks = null;
        if (Tokens.readTokens(token_file_path, token_file_wholename) != null) toks = Tokens.readTokens(token_file_path, token_file_wholename);
        if (toks.getToken() != null) tok=toks.getToken();
        if (toks.getTokensecret() != null) toksecr=toks.getTokensecret();
        System.out.println("token :"+tok);
        System.out.println("toksecr : "+toksecr);

        if (tok.equals("") || toksecr.equals(""))
        {
            // we do not support callbacks, thus pass OOB
            String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);

            printTokens("Request token :", consumer);

            System.out.println("Now visit:\n" + authUrl
                    + "\n... and grant this app authorization");
            System.out.println("Enter the PIN code and hit ENTER when you're done:");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String pin = br.readLine();

            printTokens("Access token : ", consumer);
            provider.retrieveAccessToken(consumer, pin);
            String token="", tokensecret="";            
            if (consumer.getToken() != null) token=consumer.getToken();
            if (consumer.getTokenSecret() != null) tokensecret=consumer.getTokenSecret();

            Tokens.writeTokens(token, tokensecret, token_file_path, token_file_wholename);
        }
        else
        {
            consumer.setTokenWithSecret(tok, toksecr);
        }


        URL url = new URL("http://api.twitter.com/1/statuses/home_timeline.json");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        consumer.sign(request);

        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));

        JsonParser jp = new JsonParser();
        JsonElement jo = jp.parse(in);
        JsonArray ja = jo.getAsJsonArray();
        for (Iterator it = ja.iterator(); it.hasNext();)
        {
            JsonObject object = (JsonObject) it.next();
            System.out.println(object.get("text"));
        }



    }

    public static void printTokens(String token_type, OAuthConsumer consumer)
    {
        System.out.println("Fetching request token from Twitter...");        
        System.out.println(token_type + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());
    }

    


}
