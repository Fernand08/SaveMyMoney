package com.demo.savemymoney.common.mail;

import android.os.AsyncTask;

import com.mailjet.client.Base64;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Email;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class MailSender {
    private static final String MJ_API_KEY_PRIVATE = "MGNmZDZhMDkzNjg5NjQ4M2Q0ZmQ3ZGFmYTMwZGE0YzY=";
    private static final String MJ_API_KEY_PUBLIC = "ZDdhY2FiZDI4MmNiMWU1NjBmZmYwMDE2MDEyYTFlNjY=";

    public static void sendWelcomeMail(String email, String name) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                MailjetClient client;
                MailjetRequest request = null;
                MailjetResponse response = null;
                client = new MailjetClient(getCred(MJ_API_KEY_PUBLIC), getCred(MJ_API_KEY_PRIVATE));
                try {
                    String name = strings[1];
                    request = new MailjetRequest(Email.resource)
                            .property(Email.FROMEMAIL, "heiner23.0@gmail.com")
                            .property(Email.FROMNAME, "Equipo de SaveMyMoney")
                            .property(Email.SUBJECT, "Bienvenido a SaveMyMoney")
                            .property(Email.TEXTPART, "Hola " + name + ". Bienvenido a SaveMyMoney, la aplicación que te ayudará a manejar mejor tus ingresos ;)")
                            .property(Email.HTMLPART, "<p>Hola <strong>" + name + "</strong></p><p>Bienvenido a <strong>SaveMyMoney</strong>, la aplicación que te ayudará a manejar mejor tus ingresos ;)</p><p>Equipo de SaveMyMoney</p>")
                            .property(Email.RECIPIENTS, new JSONArray()
                                    .put(new JSONObject()
                                            .put("Email", strings[0])));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    response = client.post(request);
                } catch (MailjetException e) {
                    e.printStackTrace();
                } catch (MailjetSocketTimeoutException e) {
                    e.printStackTrace();
                }
                System.out.println(response.getStatus());
                System.out.println(response.getData());
                return null;
            }
        }.execute(email, name);


    }

    private static String getCred(String mjApiKeyPublic) {
        return new String(Base64.decode(mjApiKeyPublic), StandardCharsets.UTF_8);
    }
}
