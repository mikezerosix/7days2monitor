package org.mikezerosix.rest;

import org.mikezerosix.ftp.FTPService;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static spark.Spark.*;

@SuppressWarnings("unchecked")
public class FTPResource {
    private static final Logger log = LoggerFactory.getLogger(FTPResource.class);

    public FTPResource(FTPService ftpService) {

        get(PROTECTED_URL + "ftp", (request, response) -> ftpService.isConnected(), new JsonTransformer());

        post(PROTECTED_URL + "ftp", (request, response) -> {
            try {
                ftpService.connect();
                return true;
            } catch (IOException e) {
                return false;
            }

        }, new JsonTransformer());

        delete(PROTECTED_URL + "ftp", (request, response) -> {
            try {
                ftpService.disconnect();
                return false;
            } catch (IOException e) {
                return true;
            }
        }, new JsonTransformer());

    }
}
