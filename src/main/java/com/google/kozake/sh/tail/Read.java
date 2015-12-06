package com.google.kozake.sh.tail;

import java.io.IOException;
import java.io.InputStream;

public final class Read {

    public static void bytes(final InputStream is, final int off, final boolean rflag) throws IOException {

        boolean wrap = false;

        int p = 0;
        byte buff[] = new byte[off];

        int ch;
        while ((ch = is.read()) != -1) {
            buff[p] = (byte) ch;

            if (++p == off) {
                wrap = true;
                p = 0;
            }
        }

        int len = 0;
        if (rflag) {
            int t = p - 1;
            while (t >= 0) {
                if (buff[t] == '\n' && len != 0) {
                    System.out.write(buff, t + 1, len);
                    len = 0;
                }
                --t;
                ++len;
            }
            if (wrap) {
                int tlen = len;
                for (t = off - 1, len = 0; t >= p; --t, ++len)
                    if (buff[t] == '\n') {
                        if (len != 0) {
                            System.out.write(buff, t + 1, len);
                            len = 0;
                        }
                        if (tlen != 0) {
                            System.out.write(buff, 0, tlen);
                            tlen = 0;
                        }
                    }
                if (len != 0) {
                    System.out.write(buff, t + 1, len);
                }
                if (tlen != 0) {
                    System.out.write(buff, 0, tlen);
                }
            }
        } else {
            if (wrap && (len = off - p) != 0) {
                System.out.write(buff, p, len);
            }
            len = p - 0;
            if (len != 0) {
                System.out.write(buff, 0, len);
            }
        }
    }
}
