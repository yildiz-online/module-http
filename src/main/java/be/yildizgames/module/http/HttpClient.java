/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2019 Grégory Van den Borre
 *  More infos available: https://engine.yildiz-games.be
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright
 *  notice and this permission notice shall be included in all copies or substantial portions of the  Software.
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *  OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package be.yildizgames.module.http;

import org.apiguardian.api.API;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Path;

/**
 * Http call to retrieve text or binary content.
 * It can either provide simple call or call with a behavior to be notified when the call is done.
 *
 * @author Grégory Van den Borre
 */
public interface HttpClient {

    /**
     * Request the text content.
     *
     * @param to URI to reach, cannot be null.
     * @throws IllegalStateException in case of failure, being technical or by receiving a 4xx or 5xx http code.
     * @return The body of the response to the call to the given URI, can be empty, never null.
     */
    @API(status = API.Status.STABLE)
    String getText(final URI to);

    /**
     * Request the text content.
     *
     * @param to URI to reach, must be a valid URI, cannot be null.
     * @throws IllegalStateException in case of failure, being technical or by receiving a 4xx or 5xx http code.
     * @return The body of the response to the call to the given URI, can be empty, never null.
     */
    @API(status = API.Status.STABLE)
    String getText(final String to);

    @API(status = API.Status.STABLE)
    HttpResponse<String> getTextResponse(final URI uri);

    @API(status = API.Status.STABLE)
    HttpResponse<String> getTextResponse(final String uri);

    <T,R> HttpResponse<R> postObject(final String uri, T objectToPost, Class<R> responseClazz);

    /**
     * Make a request expecting a json object, and return
     *
     * @param to    Address to call.
     * @param clazz Class of the object to return.
     * @param <T>   Type of the object to return.
     * @return The mapped object.
     */
    @API(status = API.Status.STABLE)
    <T> T getObject(URI to, Class<T> clazz);

    @API(status = API.Status.STABLE)
    <T> T getObject(String to, Class<T> clazz);

    @API(status = API.Status.STABLE)
    InputStream getInputStream(final URI to);

    @API(status = API.Status.STABLE)
    InputStream getInputStream(final String to);

    @API(status = API.Status.STABLE)
    Reader getReader(final URI to);

    @API(status = API.Status.STABLE)
    Reader getReader(final String to);

    @API(status = API.Status.STABLE)
    void sendFile(URI to, Path file, String mime);

    /**
     * Send a file and receive a response.
     *
     * @param to   Destination.
     * @param file File to send.
     * @param mime File mime type.
     * @return A text response.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<String> sendFileResponse(URI to, Path file, String mime);

    @API(status = API.Status.STABLE)
    void receiveFile(URI to, Path destination);

    @API(status = API.Status.STABLE)
    void addTransferListener(HttpTransferListener l);
}
