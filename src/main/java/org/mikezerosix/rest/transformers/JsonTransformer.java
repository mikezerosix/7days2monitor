package org.mikezerosix.rest.transformers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private Gson gson =  new GsonBuilder()
            .setDateFormat("dd MM yyyy HH:mm:ss").create();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

}

