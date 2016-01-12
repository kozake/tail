package com.google.kozake.sh.tail;

import java.io.*;

public final class TailTarget implements Closeable {

    private final File file;

    private final FileInputStream fis;

    private TailTarget(final File file) throws IOException {
        this.file = file;
        this.fis = new FileInputStream(file);
    }

    public static TailTarget open(final File file) throws IOException {
        return new TailTarget(file);
    }

    public void forwardAndPrint(final Style style, final long off) throws IOException {
        forwardAndPrint(style, off, System.out);
    }

    public void forwardAndPrint(final Style style, final long off, final PrintStream out) throws IOException {
        assert fis != null : "fis is null";

        forward(fis, style, off);

        int ch;
        while ((ch = fis.read()) != -1) {
            out.write(ch);
        }
        out.flush();
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

    public void printfn(final PrintStream out, final boolean print_nl) {

        if (print_nl) {
            out.println();
        }
        out.println("==> " + file.getName() + " <==");
    }

    public boolean show(final PrintStream out, final boolean isPrintfn) throws IOException {
        int ch;

        boolean printedfn = false;
        while ((ch = fis.read()) != -1) {

            if (isPrintfn && !printedfn) {
                printfn(out, true);
                printedfn = true;
            }
            out.write(ch);
        }
        out.flush();

        return printedfn;
    }

    @Override
    public void close() throws IOException {
        fis.close();
    }
}
