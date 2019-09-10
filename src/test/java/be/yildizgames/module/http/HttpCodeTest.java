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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author Grégory Van den Borre
 */
class HttpCodeTest {

    @Nested
    class IsError {

        @Test
        void happyFlow() {
            for(int i = 400; i < 600; i++) {
                Assertions.assertTrue(HttpCode.isError(i));
            }
        }

        @Test
        void tooLow() {
            Assertions.assertFalse(HttpCode.isError(399));
        }

        @Test
        void tooHigh() {
            Assertions.assertFalse(HttpCode.isError(600));
        }

    }

    @Nested
    class IsSuccessful {

        @Test
        void happyFlow() {
            for(int i = 200; i < 300; i++) {
                Assertions.assertTrue(HttpCode.isSuccessful(i));
            }
        }

        @Test
        void tooLow() {
            Assertions.assertFalse(HttpCode.isSuccessful(199));
        }

        @Test
        void tooHigh() {
            Assertions.assertFalse(HttpCode.isSuccessful(300));
        }

    }

    @Nested
    class IsRedirected {

        @Test
        void happyFlow() {
            for(int i = 300; i < 400; i++) {
                Assertions.assertTrue(HttpCode.isRedirected(i));
            }
        }

        @Test
        void tooLow() {
            Assertions.assertFalse(HttpCode.isRedirected(299));
        }

        @Test
        void tooHigh() {
            Assertions.assertFalse(HttpCode.isRedirected(400));
        }

    }
}
