package com.google.kozake.sh.tail;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public final class FileMapTest {

    @Test
    public void testAssert() throws IOException {
        File file = new File("src/test/data/FileMapTest0Byte.txt");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bos);
        try (FileInputStream fis = new FileInputStream(file)) {

            FileMap fileMap = new FileMap(file, fis);

            bos.reset();
            try {
                fileMap.print(-1, 0, out);
                fail("test failed.");
            } catch (AssertionError ae) {
                assertThat(ae.getMessage(), is("startOffset is invalied.[-1]"));
            }

            bos.reset();
            try {
                fileMap.print(0, -1, out);
                fail("test failed.");
            } catch (AssertionError ae) {
                assertThat(ae.getMessage(), is("len is invalied.[-1]"));
            }

            bos.reset();
            try {
                fileMap.print(0, 0, null);
                fail("test failed.");
            } catch (AssertionError ae) {
                assertThat(ae.getMessage(), is("out is null."));
            }
        }
    }

    @Test
    public void test0Byte() throws IOException {
        File file = new File("src/test/data/FileMapTest0Byte.txt");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bos);
        try (FileInputStream fis = new FileInputStream(file)) {

            FileMap fileMap = new FileMap(file, fis);

            bos.reset();
            fileMap.print(0, 0, out);
            assertThat(bos.toByteArray().length, is(0));

            bos.reset();
            fileMap.print(0, 1, out);
            assertThat(bos.toByteArray().length, is(0));

            bos.reset();
            fileMap.print(1, 0, out);
            assertThat(bos.toByteArray().length, is(0));

            bos.reset();
            fileMap.print(1, 1, out);
            assertThat(bos.toByteArray().length, is(0));
        }

    }

    @Test
    public void test1Byte() throws IOException {
        File file = new File("src/test/data/FileMapTest1Byte.txt");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bos);
        try (FileInputStream fis = new FileInputStream(file)) {

            FileMap fileMap = new FileMap(file, fis);

            bos.reset();
            fileMap.print(0, 0, out);
            assertThat(bos.toByteArray().length, is(0));

            bos.reset();
            fileMap.print(0, 1, out);
            assertThat(bos.toByteArray().length, is(1));
            assertThat(bos.toByteArray(), is("1".getBytes()));

            bos.reset();
            fileMap.print(0, 2, out);
            assertThat(bos.toByteArray().length, is(1));
            assertThat(bos.toByteArray(), is("1".getBytes()));

            bos.reset();
            fileMap.print(1, 0, out);
            assertThat(bos.toByteArray().length, is(0));

            bos.reset();
            fileMap.print(1, 1, out);
            assertThat(bos.toByteArray().length, is(0));

            bos.reset();
            fileMap.print(1, 2, out);
            assertThat(bos.toByteArray().length, is(0));
        }
    }

    @Test
    public void test4MByte() throws IOException {
        File file = new File("src/test/data/FileMapTest4MByte.txt");
        file.delete();

        String dataStr = Stream.iterate(1, i -> i == 10 ? 1 : i++ % 10).limit(4 << 20).reduce(
                new StringBuilder(),
                (buff, i) -> {
                    buff.append(String.valueOf(i));
                    return buff;
                }, StringBuilder::append).toString();

        Files.write(file.toPath(), dataStr.getBytes());

        assertThat(file.length(), is((long) (4 << 20)));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bos);
        try (FileInputStream fis = new FileInputStream(file)) {

            FileMap fileMap = new FileMap(file, fis);

            bos.reset();
            fileMap.print(0, 0, out);
            assertThat(bos.toByteArray().length, is(0));

            bos.reset();
            fileMap.print(0, 1, out);
            assertThat(bos.toByteArray().length, is(1));
            assertThat(bos.toByteArray(), is("1".getBytes()));

            bos.reset();
            fileMap.print(0, 2, out);
            assertThat(bos.toByteArray().length, is(2));
            assertThat(bos.toByteArray(), is("12".getBytes()));

            bos.reset();
            fileMap.print(0, 4 << 20, out);
            assertThat(bos.toByteArray().length, is(4 << 20));
            assertThat(bos.toByteArray(), is(dataStr.getBytes()));

            bos.reset();
            fileMap.print(0, (4 << 20) + 1, out);
            assertThat(bos.toByteArray().length, is(4 << 20));
            assertThat(bos.toByteArray(), is(dataStr.getBytes()));

            bos.reset();
            fileMap.print(1, 0, out);
            assertThat(bos.toByteArray().length, is(0));

            bos.reset();
            fileMap.print(1, 1, out);
            assertThat(bos.toByteArray().length, is(1));
            assertThat(bos.toByteArray(), is("2".getBytes()));

            bos.reset();
            fileMap.print(1, 4 << 20, out);
            assertThat(bos.toByteArray().length, is((4 << 20) - 1));

            bos.reset();
            fileMap.print(1, (4 << 20) + 1, out);
            assertThat(bos.toByteArray().length, is((4 << 20) - 1));

            file.delete();
        }
    }
}
