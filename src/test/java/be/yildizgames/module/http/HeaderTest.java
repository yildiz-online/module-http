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

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeaderTest {

    @Test
    void testConstructor() {
        List<String> values = List.of("value1", "value2");
        Header header = new Header("testKey", values);
        
        assertEquals("testKey", header.key());
        assertEquals(values, header.value());
    }

    @Test
    void testNullValues() {
        assertThrows(NullPointerException.class, () -> new Header("testKey", null));
    }

    @Test
    void testEmptyValues() {
        List<String> emptyValues = List.of();
        Header header = new Header("testKey", emptyValues);
        
        assertEquals("testKey", header.key());
        assertEquals(emptyValues, header.value());
    }

    @Test
    void testEqualsAndHashCode() {
        List<String> values1 = List.of("value1", "value2");
        List<String> values2 = List.of("value1", "value2");
        List<String> values3 = List.of("value1", "value3");
        
        Header header1 = new Header("testKey", values1);
        Header header2 = new Header("testKey", values2);
        Header header3 = new Header("testKey", values3);
        Header header4 = new Header("otherKey", values1);
        
        // Test equality
        assertEquals(header1, header2);
        assertNotEquals(header1, header3);
        assertNotEquals(header1, header4);
        assertNotEquals(header1, null);
        assertNotEquals(header1, "not a header");
        
        // Test hash code consistency
        assertEquals(header1.hashCode(), header2.hashCode());
        assertNotEquals(header1.hashCode(), header3.hashCode());
        assertNotEquals(header1.hashCode(), header4.hashCode());
    }

    @Test
    void testToString() {
        List<String> values = List.of("value1", "value2");
        Header header = new Header("testKey", values);
        
        String expected = "Header[key=testKey, value=[value1, value2]]";
        assertEquals(expected, header.toString());
    }
}
