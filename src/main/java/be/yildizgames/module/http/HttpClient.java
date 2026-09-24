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
 * HTTP client contract for text and binary transfers.
 * Every call will return an http response that can either be handled or simply provide an optional body.
 *
 * @author Grégory Van den Borre
 */
public interface HttpClient {

    /**
     * Constant empty header to reuse.
     */
    Headers EMPTY_HEADERS = Headers.empty();

    /**
     * Request the text content.
     *
     * @param to URI to reach, must be a valid URI, cannot be null.
     * @return The response to the call to the given URI, the body is a String, it can be empty, never null.
     */
    @API(status = API.Status.STABLE)
    default HttpResponse<String> getText(final String to) {
        return getText(to, EMPTY_HEADERS);
    }

    /**
     * Request the text content.
     *
     * @param to      URI to reach, must be a valid URI, cannot be null.
     * @param headers Request headers.
     * @return The response to the call to the given URI, the body is a String, it can be empty, never null.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<String> getText(final String to, Headers headers);

    /**
     * Send a text content and receive a text response.
     *
     * @param to      Destination.
     * @param content Text to send.
     * @param mime    Data mime type.
     * @return A text response, it can be empty.
     */
    @API(status = API.Status.STABLE)
    default HttpResponse<String> postText(final String to, final String content, String mime) {
        return this.postText(to, EMPTY_HEADERS, content, mime);
    }

    /**
     * Send a text content and receive a text response.
     *
     * @param to      Destination.
     * @param headers Request headers.
     * @param content Text to send.
     * @param mime    Data mime type.
     * @return A text response, it can be empty.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<String> postText(String to, Headers headers, String content, String mime);

    /**
     * Request a binary content and persist it as a file.
     *
     * @param to               URI to reach, must be a valid URI, cannot be null.
     * @param destination      Path to the file to create, cannot be null.
     * @param transferListener Listener to track the download progress, can be null.
     * @return The response to the call to the given URI, the body is the provided path for the file to save, it can be empty, never null.
     */
    @API(status = API.Status.STABLE)
    default HttpResponse<Path> getFile(String to, Path destination, HttpTransferListener transferListener) {
        return getFile(to, EMPTY_HEADERS, destination, transferListener);
    }

    /**
     * Request a binary content and persist it as a file.
     *
     * @param to               URI to reach, must be a valid URI, cannot be null.
     * @param headers          Request headers.
     * @param destination      Path to the file to create, cannot be null.
     * @param transferListener Listener to track the download progress, can be null.
     * @return The response to the call to the given URI, the body is the provided path for the file to save, it can be empty, never null.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<Path> getFile(String to, Headers headers, Path destination, HttpTransferListener transferListener);

    /**
     * Request a binary content and persist it as a file.
     *
     * @param to          URI to reach, must be a valid URI, cannot be null.
     * @param destination Path to the file to create, cannot be null.
     * @return The response to the call to the given URI, the body is the provided path for the file to save, it can be empty, never null.
     */
    @API(status = API.Status.STABLE)
    default HttpResponse<Path> getFile(String to, Path destination) {
        return getFile(to, EMPTY_HEADERS, destination);
    }

    /**
     * Request a binary content and persist it as a file.
     *
     * @param to          URI to reach, must be a valid URI, cannot be null.
     * @param headers     Request headers.
     * @param destination Path to the file to create, cannot be null.
     * @return The response to the call to the given URI, the body is the provided path for the file to save, it can be empty, never null.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<Path> getFile(String to, Headers headers, Path destination);

    /**
     * Send a file and receive a text response.
     *
     * @param to      Destination.
     * @param content File to send.
     * @param mime    File mime type.
     * @return A text response, it can be empty.
     */
    @API(status = API.Status.STABLE)
    default HttpResponse<String> postFile(String to, Path content, String mime) {
        return this.postFile(to, EMPTY_HEADERS, content, mime);
    }

    /**
     * Send a file and receive a text response.
     *
     * @param to      Destination.
     * @param headers Request headers.
     * @param content File to send.
     * @param mime    File mime type.
     * @return A text response, it can be empty.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<String> postFile(String to, Headers headers, Path content, String mime);

    /**
     * Request a binary content as InputStream.
     *
     * @param to URI to reach, must be a valid URI, cannot be null.
     * @return The response to the call to the given URI, the body is the InputStream to retrieve the binary content, never null.
     */
    @API(status = API.Status.STABLE)
    default HttpResponse<InputStream> getInputStream(String to) {
        return getInputStream(to, EMPTY_HEADERS);
    }

    /**
     * Request a binary content as InputStream.
     *
     * @param to      URI to reach, must be a valid URI, cannot be null.
     * @param headers Request headers.
     * @return The response to the call to the given URI, the body is the InputStream to retrieve the binary content, never null.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<InputStream> getInputStream(String to, Headers headers);

    /**
     * Send a binary content as InputStream and receive a text response.
     *
     * @param to      Destination.
     * @param content Data to send.
     * @param mime    Data mime type.
     * @return A text response, it can be empty.
     */
    @API(status = API.Status.STABLE)
    default HttpResponse<String> postInputStream(String to, Supplier<InputStream> content, String mime) {
        return this.postInputStream(to, EMPTY_HEADERS, content, mime);
    }

    /**
     * Send a binary content as InputStream and receive a text response.
     *
     * @param to      Destination.
     * @param headers Request headers.
     * @param content Data to send.
     * @param mime    Data mime type.
     * @return A text response, it can be empty.
     */
    @API(status = API.Status.STABLE)
    HttpResponse<String> postInputStream(String to, Headers headers, Supplier<InputStream> content, String mime);


}
