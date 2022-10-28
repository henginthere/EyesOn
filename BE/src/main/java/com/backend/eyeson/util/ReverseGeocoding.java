package com.backend.eyeson.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ReverseGeocoding {

    public static void getAddress(String address) throws IOException {

        String client_id = "avlfvma0aj";
        String client_secret = "K257cf4j3Tr8vsVohZFnWP5YKDYkglKn4TmkcEFa";
        String endpoint = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";
        String coords = address;
        String output = "json";
        String orders = "roadaddr";

        StringBuilder sb = new StringBuilder();
        sb.append(endpoint);
        sb.append("?coords=" + coords);
        sb.append("&orders=" + orders);
        sb.append("&output=" + output);


        URL url = new URL(sb.toString());
        HttpsURLConnection http = (HttpsURLConnection)url.openConnection();
        http.setRequestProperty("Content-Type", "application/json;charset=euc-kr");
        http.setRequestProperty("X-NCP-APIGW-API-KEY-ID", client_id);
        http.setRequestProperty("X-NCP-APIGW-API-KEY", client_secret);
        http.setRequestMethod("GET");
        http.connect();

        int status = http.getResponseCode();
        System.out.println("status: " + status);
        BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), "euc-kr"));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);
        System.out.println(element);
//        String value = element.getAsJsonObject().get("results")


    }


}
