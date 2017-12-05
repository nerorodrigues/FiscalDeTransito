package com.example.neror.trabalho_4.Utils;

/**
 * Created by neror on 25/11/2017.
 */

import android.util.Log;

import java.io.DataOutputStream;
import java.util.List;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server.
 *
 * @author www.codejava.net
 */
public class MultipartUtility {
    private final String twoHyphens = "--";
    private String boundary =  "QwKJ4eqm8NT6IcgOTKO3K4gEivkpf5jEs6ynaqS";
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection connection;
    private String charset;
    private DataOutputStream request;


    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset,String RequestMethod)
            throws IOException {
        this.charset = charset;

        // creates a unique boundary based on time stamp
        this.boundary = Long.toString(System.currentTimeMillis());

        URL url = new URL(requestURL);
        connection = (HttpURLConnection)url.openConnection();
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod(RequestMethod);
        connection.setRequestProperty("Connection","Keep-Alive");
        connection.setRequestProperty("Cache-Control","no-cache");
        connection.setRequestProperty("Content-Type","multipart/form-data;boundary="+this.boundary);

        connection.setRequestProperty("User-Agent", "CodeJava Agent");
        request  = new DataOutputStream(connection.getOutputStream());
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) throws IOException {
        request.writeBytes("--" + this.boundary);
        request.writeBytes(LINE_FEED);
        request.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"");
        request.writeBytes(LINE_FEED);
        request.writeBytes("Content-Type: text/plain; charset=" + charset);
        request.writeBytes(LINE_FEED);
        request.writeBytes(LINE_FEED);
        request.writeBytes(value);
        request.writeBytes(LINE_FEED);
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        request.writeBytes("--" + boundary);
        request.writeBytes(LINE_FEED);
        request.writeBytes("Content-Disposition: form-data; name=\"" + fieldName+ "\"; filename=\"" + fileName + "\"");
        request.writeBytes(LINE_FEED);
        request.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(fileName));
        request.writeBytes(LINE_FEED);
        request.writeBytes("Content-Transfer-Encoding: binary");
        request.writeBytes(LINE_FEED);
        request.writeBytes(LINE_FEED);

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            request.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        request.writeBytes(LINE_FEED);
    }

    public void addFilePart(String fieldName, byte[] fileData)
            throws IOException {
        request.writeBytes("--" + boundary);
        request.writeBytes(LINE_FEED);
        request.writeBytes("Content-Disposition: form-data; name=\"" + fieldName+ "\"; filename=\"image.png\"");
        request.writeBytes(LINE_FEED);
        request.writeBytes("Content-Type: image/png" );
        request.writeBytes(LINE_FEED);
        request.writeBytes("Content-Transfer-Encoding: binary");
        request.writeBytes(LINE_FEED);
        request.writeBytes(LINE_FEED);

        request.write(fileData);

        request.writeBytes(LINE_FEED);
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws IOException {
        StringBuilder response = new StringBuilder();
        request.writeBytes(this.twoHyphens + boundary + this.twoHyphens);
        request.writeBytes(LINE_FEED);
        request.flush();
        request.close();

        // checks server's status code first
        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }

        return response.toString();
    }
}