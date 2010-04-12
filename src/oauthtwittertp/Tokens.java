/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oauthtwittertp;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;

/**
 *
 * @author catherine
 */
public class Tokens {

    private String token;
    private String tokensecret;

    public Tokens()
    {

    }

    public Tokens(String token, String tokensecret)
    {
        this.token=token;
        this.tokensecret=tokensecret;
    }

    public String getToken()
    {
        return this.token;
    }

    public void setToken(String token)
    {
        this.token=token;
    }

    public String getTokensecret()
    {
        return this.tokensecret;
    }

    public void setTokensecret(String tokensecret)
    {
        this.tokensecret=tokensecret;
    }

    
    public static String writeTokens(String tok, String toksecr, String pathname, String filename) throws IOException
    {
        String erreur="";
        Tokens toks = new Tokens(tok, toksecr);
        Gson gson_tok = new Gson();
        String json_tok=gson_tok.toJson(toks);
        System.out.println(json_tok);
        File path = new File(pathname);
        if (!path.exists()) (path).mkdir();
            
        if (path.exists())
        {
            File file = new File(filename);
            if (!file.exists())
            {
                BufferedWriter pen = new BufferedWriter(new FileWriter(filename, true));
                pen.write(json_tok);
                pen.close();
            }
        }
        else erreur="Sorry. Your computer could not record the tokens.";
        return erreur;
    }

    public static Tokens readTokens(String pathname, String filename) throws IOException
    {
        Gson gson_tok = new Gson();
        Tokens toks=new Tokens();
        File path = new File(pathname);
        if (path.exists())
        {
            File file = new File(filename);
            if (file.exists())
            {
                BufferedReader eye = new BufferedReader(new FileReader(filename));
                int character;
                while ((character = eye.read()) != -1)
                {
                    System.out.print((char)character);
                }
                eye.close();

                toks = gson_tok.fromJson(new FileReader(filename), Tokens.class);
            }
        }
        return toks;
    }
}
