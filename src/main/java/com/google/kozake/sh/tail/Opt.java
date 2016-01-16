package com.google.kozake.sh.tail;

/**
 * C言語ライブラリのgetoptをまねた実装クラスです。.
 */
public final class Opt {

    /**
     * 解析するオプション引数.
     */
    private final String[] args;

    /**
     * オプション要素.
     */
    private final String opts;

    /**
     * 現在のオプション位置.
     */
    private int index = 0;

    /**
     * オプション文字の現在位置.
     */
    private int argIndex = 1;

    /**
     * 現在のオプション文字.
     */
    private int opt;

    /**
     * 現在のオプション引数.
     */
    private String arg;

    /**
     * コンストラクタ.
     * @param args オプション引数
     * @param opts オプション要素
     */
    public Opt(final String[] args, final String opts) {
        this.args = args;
        this.opts = opts;
    }

    /**
     * 次のオプション文字を返します。.
     * @return 次のオプション文字
     */
    public int get() {

        char c;
        int optsIndex;

        if (index >= args.length
                || args[index].charAt(0) != '-'
                || args[index].length() == 1) {
            return -1;

        } else if (args[index].equals("--")) {

            index++;
            return -1;
        }

        opt = c = args[index].charAt(argIndex);

        if (c == ':' || (optsIndex = opts.indexOf(c)) == -1) {
            System.err.println(String.format(": illegal option -- %c", c));
            if (++argIndex >= args[index].length()) {
                index++;
                argIndex = 1;
            }
            return '?';
        }
        if (++optsIndex < opts.length() && opts.charAt(optsIndex) == ':') {
            if ((argIndex + 1 ) < args[index].length()) {
                arg = args[index++].substring(argIndex + 1);
            } else if(++index >= args.length) {
                System.err.println(String.format(": option requires an argument -- %c", c));
                argIndex = 1;
                return '?';
            } else {
                arg = args[index++];
            }
            argIndex = 1;
        } else {
            if (++argIndex >= args[index].length()) {
                argIndex = 1;
                index++;
            }
            arg = null;
        }

        return c;
    }

    /**
     * 現在のオプション文字を返します。.
     * @return 現在のオプション文字
     */
    public int getOpt() {
        return opt;
    }

    /**
     * 現在のオプション引数を返します。.
     * @return 現在のオプション引数
     */
    public String getArg() {
        return arg;
    }
}
