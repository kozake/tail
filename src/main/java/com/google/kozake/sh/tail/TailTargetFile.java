package com.google.kozake.sh.tail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

public final class TailTargetFile {

    private final File file;

    public TailTargetFile(final File file) {
        this.file = file;
    }

    public void forwardAndPrint(final Style style, final long off) throws IOException {
        forwardAndPrint(style, off, System.out);
    }

    public void forwardAndPrint(final Style style, final long off, final PrintStream out) throws IOException {

        try (FileInputStream fis = new FileInputStream(file)) {
            forward(fis, style, off);

            int ch;
            while ((ch = fis.read()) != -1) {
                out.write(ch);
            }
            out.flush();
        }
    }

    private void forward(final FileInputStream fis, final Style style, final long off) throws IOException {

        switch(style) {
            case FBYTES:
                if (off == 0) {
                    break;
                }
                fis.getChannel().position(Math.min(file.length(), off));
                break;
            case FLINES:
                if (off != 0) {
                    int ch;
                    long offset = off;
                    for (; ; ) {
                        if ((ch = fis.read()) == -1) {
                            break;
                        }
                        if (ch == '\n' && --offset == 0) {
                            break;
                        }
                    }
                }
                break;
            case RBYTES:
                if (file.length() >= off) {
                    fis.getChannel().position(file.length() - off);
                }
                break;
            case RLINES:
                if (off == 0) {
                    fis.getChannel().position(file.length());
                } else {
                    new FileMap(file, fis).rlines(off);
                }
                break;
            default:
                break;
        }
    }
}
