package com.google.kozake.sh.tail;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TailTargetTest {

    @Test
    public void test4MByte() throws IOException {

        File file = new File("src/test/data/ForwardTest4MByte.txt");
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

        try (TailTarget target = TailTarget.open(file)) {
            target.printfn(System.out, true);
            target.forwardAndPrint(Style.FBYTES, (4 << 20) - 20);
        }
        file.delete();
    }
}
