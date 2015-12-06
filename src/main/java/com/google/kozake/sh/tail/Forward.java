package com.google.kozake.sh.tail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class Forward {

    public static void forward(File file, FileInputStream fis, Style style, long off) throws IOException {
        int ch;

        switch(style) {
            case FBYTES:
                if (off == 0) {
                    break;
                }
                if (file.length() < off) {
                    off = file.length();
                }
                fis.getChannel().position(off);
                break;
            case FLINES:
                if (off == 0)
                    break;
                for (; ; ) {
                    if ((ch = fis.read()) == -1) {
                        break;
                    }
                    if (ch == '\n' && --off == 0)
                        break;
                }
                break;
            case RBYTES:
                if (file.length() >= off) {
                    fis.getChannel().position(file.length() - off);
                }
                break;
            case RLINES:
                if (off == 0) {
                    fis.getChannel().position(file.length() - 1);
                } else {
                    rlines(fp, fn, off, sbp);
                }
                break;
            default:
                break;
        }

        while ((ch = fis.read()) != -1) {
            System.out.write(ch);
        }
        System.out.flush();
    }
}
