/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2025 Grégory Van den Borre
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

import java.util.List;
import java.util.Optional;

/**
 * Response to every http request, it contains the http code, the headers and the body.
 * @param <T> Response body type.
 *
 * @author Grégory Van den Borre
 */
public class HttpResponse<T> {

    /**
     * Response http code.
     */
    private final int httpCode;

    /**
     * Response body, if the call succeed, can be null otherwise.
     */
    private final T body;

    /**
     * Response headers.
     */
    private final Headers headers;

    /**
     * Error if the call failed, can be null otherwise.
     */
    private final Throwable error;

    /**
     * Constructor for a successful call, the error will be null.
     *
     * @param httpCode Http code of the response.
     * @param body     Body of the response.
     * @param headers  Headers of the response.
     */
    public HttpResponse(int httpCode, T body, Headers headers) {
        super();
        this.httpCode = httpCode;
        this.body = body;
        this.error = null;
        this.headers = headers;
    }


    /**
     * Constructor for an empty successful call, body and error will be null.
     *
     * @param httpCode Http code of the response.
     * @param headers  Headers of the response.
     */
    public HttpResponse(int httpCode, Headers headers) {
        super();
        this.httpCode = httpCode;
        this.body = null;
        this.error = null;
        this.headers = headers;
    }


    /**
     * Constructor for a failed call, the body will be null.
     *
     * @param error Error thrown during the call.
     */
    public HttpResponse(Throwable error) {
        super();
        this.httpCode = -1;
        this.body = null;
        this.error = error;
        this.headers = new Headers(List.of());
    }

    /**
     * Apply a behavior for the different possibilities: success(2xx), error(4xx or 5xx) or failure(exception).
     *
     * @param behavior Behavior to apply.
     */
    public final void handle(HttpResponseBehavior<T> behavior) {
        if (this.error != null) {
            behavior.onCallFailure(this.error);
        } else if (HttpCode.isSuccessful(this.httpCode)) {
            behavior.onHttpSuccess(this.httpCode, this.headers, this.body);
        } else if (HttpCode.isError(this.httpCode)) {
            behavior.onHttpError(this.httpCode, this.headers, this.body);
        }
    }


    /**
     * @return The response body if present, empty optional otherwise.
     */
    public final Optional<T> body() {
        return Optional.ofNullable(this.body);
    }
}
