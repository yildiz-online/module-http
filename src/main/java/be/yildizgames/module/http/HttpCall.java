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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Http call to retrieve text or binary content.
 *
 * @author Grégory Van den Borre
 */
public class HttpCall {

    /**
     * Logger.
     */
    private static final System.Logger LOGGER = System.getLogger(HttpCall.class.toString());

    /**
     * Buffer size.
     */
    private static final int BUFFER_SIZE = 1024;

    private final HttpClient client = HttpClient.newHttpClient();

    private final List<HttpTransferListener> listeners = new ArrayList<>();

    /**
     * Request the text content.
     *
     * @param uri URI to reach.
     * @return The content of the uri destination.
     */
    public final String getText(final URI uri) {
        return this.getStream(uri, HttpResponse.BodyHandlers.ofString());
    }

    public final InputStream getInputStream(final URI uri) {
        return this.getStream(uri, HttpResponse.BodyHandlers.ofInputStream());
    }

    public final void receiveFile(URI uri, Path destination) {
        try {
            Files.createDirectories(destination.getParent());
        } catch (IOException e) {
            throw new IllegalStateException("error.file.create", e);
        }
        try (
                BufferedInputStream bis = new BufferedInputStream(this.getStream(uri, HttpResponse.BodyHandlers.ofInputStream()));
                BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(destination))) {
            byte[] buf = new byte[BUFFER_SIZE];
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
    private <T> T getStream(final URI url, HttpResponse.BodyHandler<T> bodyHandler){
        HttpRequest request = HttpRequest.newBuilder(url).build();
        try {
            HttpResponse<T> response = this.client.send(request, bodyHandler);
            if (HttpCode.isError(response.statusCode())) {
                LOGGER.log(System.Logger.Level.ERROR, "Error retrieving content: {}", url);
                throw new IllegalStateException("error.http.content.retrieve");
            }
            return response.body();
        } catch (IOException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error retrieving content: {}", url, e);
            throw new IllegalStateException("error.http.content.retrieve");
        } catch (InterruptedException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error retrieving content: {}", url, e);
            Thread.currentThread().interrupt();
            throw new IllegalStateException("error.http.content.retrieve");
        }
    }
}
