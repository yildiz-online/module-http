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
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * Http call to retrieve text or binary content.
 * Every call will return an http response that can either be handled or simply provide an optional body.
 *
 * @author Grégory Van den Borre
 */
public interface HttpClient {

    /**
     * Request the text content.
     *
     * @param to URI to reach, must be a valid URI, cannot be null.
     * @return The response to the call to the given URI, the body is a String, it can be empty, never null.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<String> getText(final String to);

    /**
     * Send a text content and receive a text response.
     *
     * @param to      Destination.
     * @param content Text to send.
     * @param mime    Data mime type.
     * @return A text response, it can be empty.
     */
    HttpResponse<String> postText(final String to, final String content, String mime);

    /**
     * Request a binary content and persist it as a file.
     *
     * @param to               URI to reach, must be a valid URI, cannot be null.
     * @param destination      Path to the file to create, cannot be null.
     * @param transferListener Listener to track the download progress, can be null.
     * @return The response to the call to the given URI, the body is the provided path for the file to save, it can be empty, never null.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<Path> getFile(String to, Path destination, HttpTransferListener transferListener);

    /**
     * Request a binary content and persist it as a file.
     *
     * @param to          URI to reach, must be a valid URI, cannot be null.
     * @param destination Path to the file to create, cannot be null.
     * @return The response to the call to the given URI, the body is the provided path for the file to save, it can be empty, never null.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<Path> getFile(String to, Path destination);

    /**
     * Send a file and receive a text response.
     *
     * @param to      Destination.
     * @param content File to send.
     * @param mime    File mime type.
     * @return A text response, it can be empty.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<String> postFile(String to, Path content, String mime);

    /**
     * Request a binary content as InputStream.
     *
     * @param to URI to reach, must be a valid URI, cannot be null.
     * @return The response to the call to the given URI, the body is the InputStream to retrieve the binary content, never null.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<InputStream> getInputStream(final String to);

    /**
     * Send a binary content as InputStream and receive a text response.
     *
     * @param to      Destination.
     * @param content Data to send.
     * @param mime    Data mime type.
     * @return A text response, it can be empty.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<String> postInputStream(String to, Supplier<InputStream> content, String mime);


}
