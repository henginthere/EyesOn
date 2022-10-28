package com.backend.eyeson.util;

import com.google.api.Http;
import com.google.api.client.json.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ReverseGeocoding {

    public static <MultiValuemap> String getAddress(String address) throws IOException {

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
        http.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        http.setRequestProperty("X-NCP-APIGW-API-KEY-ID", client_id);
        http.setRequestProperty("X-NCP-APIGW-API-KEY", client_secret);
        http.setRequestProperty("Accept","application/json");
        http.setRequestMethod("GET");
        http.connect();


        int status = http.getResponseCode();
        System.out.println("status: " + status);
        BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) http.getContent(),"UTF-8"));
        String line = "";
        String result = "";

        while((line=br.readLine())!=null) {
            result += line;
        }

        // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);
        String value = element.getAsJsonObject().get("results").toString();
        JsonArray value2 = parser.parse(value).getAsJsonArray();
        JsonObject value3 = (value2.get(0).getAsJsonObject()).get("region").getAsJsonObject();

        //대구광역시 수성구 수성1가동 출력
        //128.60945,35.85343 대구
        //128.41719,36.10684 구미
        String location = "";
        for(int i=1; i<=3; i++) {
            String area = "area" + Integer.toString(i);
            String value4 = (value3.get(area).getAsJsonObject()).get("name").toString();
            location += value4 + " ";
        }
        System.out.println(location);
        return location;

    }


}
