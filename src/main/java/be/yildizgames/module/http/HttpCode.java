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

/**
 * Helper class to handle easily http codes.
 *
 * @author Grégory Van den Borre
 */
public class HttpCode {

    /**
     * Private constructor to prevent instantiation.
     */
    private HttpCode() {
        super();
    }

    /**
     * Check if a code is in error range (400 - 599).
     *
     * @param code Code to check.
     *
     * @return true if it is an error code.
     */
    @API(status= API.Status.STABLE)
    public static boolean isError(int code) {
        return code >= 400 && code < 600;
    }

    /**
     * Check if a code is in success range (200 - 299).
     *
     * @param code Code to check.
     *
     * @return true if it is an error code.
     */
    @API(status= API.Status.STABLE)
    public static boolean isSuccessful(int code) {
        return code >= 200 && code < 300;
    }

    /**
     * Check if a code is in redirection range (300 - 399).
     *
     * @param code Code to check.
     *
     * @return true if it is an error code.
     */
    @API(status= API.Status.STABLE)
    public static boolean isRedirected(int code) {
        return code >= 300 && code < 400;
    }

}
