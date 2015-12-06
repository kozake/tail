package com.google.kozake.sh.tail;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadTest {

    @Test
    public void test() throws IOException {
        byte[] arr = "abcdefddddddddddddddd\ntest\ntest2".getBytes();

        try (InputStream is = new ByteArrayInputStream(arr)) {

            Read.bytes(is, 50, true);
        }
    }
}
