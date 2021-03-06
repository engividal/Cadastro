package br.com.caelum.cadastro.support;

import android.content.Entity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by android5843 on 10/12/15.
 */
public class WebClient {
    private static final String URL = "http://www.caelum.com.br/mobile";

    public String post(String json){
        HttpPost post = new HttpPost(URL);
        try {
            post.setEntity(new StringEntity(json));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        DefaultHttpClient client = new DefaultHttpClient();

        String jsonDeResposta = null;
        try {
            HttpResponse response = client.execute(post);
            jsonDeResposta = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonDeResposta;
    }
}
