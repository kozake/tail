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
                    rlines(file, fis, off);
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

    private static void rlines(File file, FileInputStream fis, long off) throws IOException {

        int i;

        long size = file.length();
        if (size == 0) {
            return;
        }
        FileMap map = new FileMap(file, fis);

    	/*
    	 * Last char is special, ignore whether newline or not. Note that
    	 * size == 0 is dealt with above, and size == 1 sets curoff to -1.
	     */
        long curoff = size - 2;
        while (curoff >= 0) {
            if (curoff < map.getCurrOffset()) {
                map.around(curoff);
            }
            for (i = (int) (curoff - map.getCurrOffset()); i >= 0; i--) {
                if (map.read(i) == '\n' && --off == 0) {
                    break;
                }
            }
    		/* `i' is either the map offset of a '\n', or -1. */
            curoff = map.getCurrOffset() + i;
            if (i >= 0) {
                break;
            }
        }
        curoff++;
        map.print(curoff, (int) (size - curoff));

    	/* Set the file pointer to reflect the length displayed. */
        fis.getChannel().position(file.length());

    }
}
