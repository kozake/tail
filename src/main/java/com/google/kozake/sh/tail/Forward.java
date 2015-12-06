package com.google.kozake.sh.tail;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

public final class Forward {

    public static void forward(File file, FileChannel fileChannel, Style style, long off) throws IOException {
        int ch;

        switch(style) {
            case FBYTES:
                if (off == 0) {
                    break;
                }
                if (file.length() < off) {
                    off = file.length();
                }
                fileChannel.position(off);
                break;
            case FLINES:
                if (off == 0)
                    break;
                for (;;) {
                    if ((ch = getc(fp)) == EOF) {
                        if (ferror(fp)) {
                            ierr(fn);
                            return;
                        }
                        break;
                    }
                    if (ch == '\n' && !--off)
                        break;
                }
                break;
            case RBYTES:
                if (S_ISREG(sbp->st_mode)) {
                    if (sbp->st_size >= off &&
                            fseeko(fp, -off, SEEK_END) == -1) {
                        ierr(fn);
                        return;
                    }
                } else if (off == 0) {
                    while (getc(fp) != EOF);
                    if (ferror(fp)) {
                        ierr(fn);
                        return;
                    }
                } else
                if (bytes(fp, fn, off))
                    return;
                break;
            case RLINES:
                if (S_ISREG(sbp->st_mode))
                    if (!off) {
                        if (fseeko(fp, (off_t)0, SEEK_END) == -1) {
                            ierr(fn);
                            return;
                        }
                    } else
                        rlines(fp, fn, off, sbp);
                else if (off == 0) {
                    while (getc(fp) != EOF);
                    if (ferror(fp)) {
                        ierr(fn);
                        return;
                    }
                } else
                if (lines(fp, fn, off))
                    return;
                break;
            default:
                break;
        }

        while ((ch = getc(fp)) != EOF)
            if (putchar(ch) == EOF)
                oerr();
        if (ferror(fp)) {
            ierr(fn);
            return;
        }
        (void)fflush(stdout);
    }
}
