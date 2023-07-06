package com.hls.logback.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AccessClient {
    ExecutorService service = Executors.newFixedThreadPool(10);

    public static String setGet(URL realUrl){
        String result = "";
        BufferedReader reader = null;
        try {
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "PostmanRuntime-ApipostRuntime/1.1.0");
            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = reader.readLine())!=null){
                result += line;
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void access() throws MalformedURLException, InterruptedException {
        URL url = new URL("http://localhost:8080/test/access");
        for (int i = 0; i < 10; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(setGet(url));
                }
            });
        }

        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        AccessClient client = new AccessClient();
        client.access();
    }
}
