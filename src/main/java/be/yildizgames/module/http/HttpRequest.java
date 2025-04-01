/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2019 Grégory Van den Borre
 *
 *  More infos available: https://engine.yildiz-games.be
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  SOFTWARE.
 *
 */
package be.yildizgames.module.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apiguardian.api.API;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Http call to retrieve text or binary content.
 *
 * @author Grégory Van den Borre
 */
public class HttpRequest {

    /**
     * Logger.
     */
    private static final System.Logger LOGGER = System.getLogger(HttpRequest.class.toString());

    /**
     * Buffer size.
     */
    private static final int BUFFER_SIZE = 1024;

    public static final String ERROR_HTTP_CONTENT_RETRIEVE = "error.http.content.retrieve";

    private final HttpClient client = HttpClient.newHttpClient();

    private final List<HttpTransferListener> listeners = new ArrayList<>();

    private final int timeout;

    public HttpRequest(int timeout) {
        this.timeout = timeout;
    }

    public HttpRequest() {
        this(-1);
    }

    /**
     * Request the text content.
     *
     * @param uri URI to reach.
     * @return The content of the uri destination.
     */
    @API(status= API.Status.STABLE)
    public final String getText(final URI uri) {
        return this.getStream(uri, java.net.http.HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Request the text content.
     *
     * @param uri URI to reach.
     * @return The content of the uri destination.
     */
    @API(status= API.Status.STABLE)
    public final String getText(final String uri) {
        return this.getText(URI.create(uri));
    }

    public final HttpResponse<String> getTextResponse(final URI uri) {
        return this.getStreamResponse(uri, java.net.http.HttpResponse.BodyHandlers.ofString());
    }

    public final HttpResponse<String> getTextResponse(final String uri) {
        return this.getTextResponse(URI.create(uri));
    }

    /**
     * Make a request expecting a json object, and return
     * @param uri Address to call.
     * @param clazz Class of the object to return.
     * @param <T> Type of the object to return.
     * @return The mapped object.
     */
    @API(status= API.Status.STABLE)
    public final <T> T getObject(URI uri, Class<T> clazz) {
        try {
            var content = this.getText(uri);
            var mapper = new ObjectMapper();
            return mapper.readValue(content,clazz);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @API(status= API.Status.STABLE)
    public final <T> T getObject(String uri, Class<T> clazz) {
        return this.getObject(URI.create(uri), clazz);
    }

    @API(status= API.Status.STABLE)
    public final InputStream getInputStream(final URI uri) {
        return this.getStream(uri, java.net.http.HttpResponse.BodyHandlers.ofInputStream());
    }

    @API(status= API.Status.STABLE)
    public final InputStream getInputStream(final String uri) {
        return this.getInputStream(URI.create(uri));
    }

    @API(status= API.Status.STABLE)
    public final Reader getReader(final URI uri) {
        return new InputStreamReader(this.getStream(uri, java.net.http.HttpResponse.BodyHandlers.ofInputStream()));
    }

    @API(status= API.Status.STABLE)
    public final Reader getReader(final String uri) {
        return this.getReader(URI.create(uri));
    }

    @API(status= API.Status.STABLE)
    public final void sendFile(URI uri, Path origin, String mime) {
        try {
            var request = java.net.http.HttpRequest.newBuilder()
                    .header("Content-Type", mime)
                    .uri(uri)
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofFile(origin))
                    .build();
            var response = this.client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (HttpCode.isError(response.statusCode())) {
                LOGGER.log(System.Logger.Level.ERROR, "Error sending content: {0} status: {1}", uri, response.statusCode());
                throw new IllegalStateException("error.http.content.send");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("error.http.file.send", e);
        } catch (Exception e) {
            throw new IllegalStateException("error.http.file.send", e);
        }
    }

    @API(status= API.Status.STABLE)
    public final void receiveFile(URI uri, Path destination) {
        try {
            Files.createDirectories(destination.getParent());
        } catch (IOException e) {
            throw new IllegalStateException("error.file.create", e);
        }
        try (
                var bis = new BufferedInputStream(this.getStream(uri, java.net.http.HttpResponse.BodyHandlers.ofInputStream()));
                var bos = new BufferedOutputStream(Files.newOutputStream(destination))) {
            var buf = new byte[BUFFER_SIZE];
            int len;
            long currentlyTransferred = 0;
            while ((len = bis.read(buf)) > 0) {
                bos.write(buf, 0, len);
                currentlyTransferred += len;
                for(HttpTransferListener l : this.listeners) {
                    l.received(uri, len, currentlyTransferred);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("error.http.file.retrieve", e);
        }
    }

    @API(status= API.Status.STABLE)
    public final void addTransferListener(HttpTransferListener l) {
        this.listeners.add(l);
    }

    /**
     * Call to an HTTP get method, return the stream generated by the response.
     *
     * @param url Url to request.
     * @return The stream for the request url.
     * @throws IllegalStateException If an exception occurs.
     */
    private <T> T getStream(final URI url, java.net.http.HttpResponse.BodyHandler<T> bodyHandler){
        java.net.http.HttpRequest request;
        if(this.timeout == -1) {
            request = java.net.http.HttpRequest.newBuilder(url).build();
        } else {
            request = java.net.http.HttpRequest.newBuilder(url).timeout(Duration.ofSeconds(timeout)).build();
        }
        try {
            var response = this.client.send(request, bodyHandler);
            if (HttpCode.isError(response.statusCode())) {
                LOGGER.log(System.Logger.Level.ERROR, "Error retrieving content: {0} status: {1}", url, response.statusCode());
                throw new IllegalStateException(ERROR_HTTP_CONTENT_RETRIEVE);
            }
            return response.body();
        } catch (IOException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error retrieving content: {0}", url, e);
            throw new IllegalStateException(ERROR_HTTP_CONTENT_RETRIEVE);
        } catch (InterruptedException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error retrieving content: {0}", url, e);
            Thread.currentThread().interrupt();
            throw new IllegalStateException(ERROR_HTTP_CONTENT_RETRIEVE);
        }
    }

    private <T> be.yildizgames.module.http.HttpResponse<T> getStreamResponse(final URI url, java.net.http.HttpResponse.BodyHandler<T> bodyHandler){
        java.net.http.HttpRequest request;
        if(this.timeout == -1) {
            request = java.net.http.HttpRequest.newBuilder(url).build();
        } else {
            request = java.net.http.HttpRequest.newBuilder(url).timeout(Duration.ofSeconds(timeout)).build();
        }
        try {
            var response = this.client.send(request, bodyHandler);
            return new HttpResponse<>(response.statusCode(), response.body());
        } catch (Throwable e) {
            return new HttpResponse<>(e);
        }
    }
}
