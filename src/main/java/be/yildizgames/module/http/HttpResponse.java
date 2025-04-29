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

/**
 * @author Grégory Van den Borre
 */
public class HttpResponse<T> {

    private final int httpCode;

    private final T body;

    private final Headers headers;

    private final Throwable error;

    public HttpResponse(int httpCode, T body, Headers headers) {
        super();
        this.httpCode = httpCode;
        this.body = body;
        this.error = null;
        this.headers = headers;
    }

    public HttpResponse(Throwable error) {
        super();
        this.httpCode = -1;
        this.body = null;
        this.error = error;
        this.headers = new Headers(List.of());
    }

    public final void handle(HttpResponseBehavior<T> behavior) {
        if(this.error != null) {
            behavior.onCallFailure(this.error);
        } else if(HttpCode.isSuccessful(this.httpCode)) {
            behavior.onHttpSuccess(this.httpCode, this.headers, this.body);
        } else if (HttpCode.isError(this.httpCode)) {
            behavior.onHttpError(this.httpCode, this.headers, this.body);
        }
    }
}
