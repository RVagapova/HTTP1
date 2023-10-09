package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {
    public static final String URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static ObjectMapper mapper = new ObjectMapper();

    public Main() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        //создание клиента
        CloseableHttpClient client = HttpClientBuilder.create()
                .setUserAgent("Agent")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000) //макс время ожидания от сервера
                        .setSocketTimeout(30000) //макс время ожидания получения данных
                        .setRedirectsEnabled(false) //возможность следовать редиректу
                        .build())
                .build();

        HttpGet request = new HttpGet(URL);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = client.execute(request);

        InputStream content  = response.getEntity().getContent();

        List<Post> posts = mapper.readValue(content, new TypeReference<>() {});
        posts.stream()
                .filter(post -> post.getUpvotes() != null)
                .forEach(System.out::println);
    }
}
