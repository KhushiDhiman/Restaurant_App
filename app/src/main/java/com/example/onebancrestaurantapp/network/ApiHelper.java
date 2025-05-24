package com.example.onebancrestaurantapp.network;

import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;

public class ApiHelper {

    public static String post(String action, String jsonBody) {
        try {
            URL url = new URL("https://uat.onebanc.ai/emulator/interview/" + action);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-Partner-API-Key", "uonebancservceemultrS3cg8RaL30");
            conn.setRequestProperty("X-Forward-Proxy-Action", action);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(jsonBody.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();
            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
