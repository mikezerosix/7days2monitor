package org.mikezerosix.tumblr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TumblrService {
    private static final Logger log = LoggerFactory.getLogger(TumblrService.class);
    public static final String JOEL_S_URL = "http://joelhuenink.tumblr.com/api/read/json?num=1";
    private static final long CACHE_TIME = 1000 * 60 * 30;
    private static long cacheTTL = System.currentTimeMillis();
    private static JsonNode cache;

    protected JsonNode readJsonNode(String uri) throws IOException {
        String json = null;
        String data = null;
        try {
            final Response response = Request.Get(uri).execute();
            data = getResponseContent(response.returnResponse());

            json = data.substring(22, data.length() - 2);
            return new ObjectMapper().readTree(json);
        } catch (Exception e) {
           log.error("Reading Tumblr failed from: '" + data + "' to '" + json, e);
            throw new IOException(e);
        }
    }

    public JsonNode getLastJoelsBlog() throws IOException {
        if (System.currentTimeMillis() > cacheTTL) {
            cache = readJsonNode(JOEL_S_URL);
            cacheTTL = System.currentTimeMillis() + CACHE_TIME;
        }

        return cache;
    }

    private String getResponseContent(HttpResponse response) throws IOException {
        final String content = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new RuntimeException("Request failed! Response code: " + statusCode + ", content: " + content);
        }
        return content;
    }
}
