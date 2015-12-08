package com.google.kozake.sh.tail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.*;

/**
 * ファイルのマッピングクラスです.
 */
public final class FileMap {

    /**
     * マップ長.
     */
    private static final int MAP_LENGTH = (4 << 20);

    /**
     * マップ対象ファイル.
     */
    private final File file;

    /**
     * ファイルの入力ストリーム.
     */
    private final FileInputStream fis;

    /**
     * マップの最大オフセット。ファイル長と同じ.
     */
    private final long maxOffset;

    /**
     * マップの現在のオフセット.
     */
    private long currOffset;

    /**
     * マップ長.
     */
    private int length;

    /**
     * バッファ.
     */
    private ByteBuffer buff;

    /**
     * コンストラクタ.
     * @param file ファイル
     * @param fis ファイル入力ストリーム
     */
    public FileMap(final File file, final FileInputStream fis) {

        this.file = file;
        this.fis = fis;
        this.buff = null;
        this.maxOffset = file.length();
        this.currOffset = maxOffset;
    }

    /**
     * マップの現在のオフセットを返します。.
     * @return マップの現在のオフセット
     */
    public long getCurrOffset() {
        return this.currOffset;
    }

    /**
     * マップの現在のオフセットを基準として、指定されたインデックスのバイト値を返します。.
     * @param index インデックス
     * @return 指定されたインデックスのバイト値
     */
    public byte read(final int index) {
        return buff.get(index);
    }

    /**
     * 指定されたオフセット付近にマップします。.
     * @param offset オフセット
     * @throws IOException 入出力例外が発生したい場合
     */
    public void around(final long offset) throws IOException {

        currOffset = offset & ~(MAP_LENGTH - 1);
        length = MAP_LENGTH;

        if (length > maxOffset - currOffset) {
            length = (int) (maxOffset - currOffset);
        }

        assert (length > 0);

        buff = ByteBuffer.allocate(length);

        fis.getChannel().read(buff, currOffset);
    }

    /**
     * 開始オフセットから指定長分のファイル値を、標準出力します。.
     * @param startOffset 開始オフセット
     * @param len 出力するバイト長
     * @throws IOException 入出力例外が発生したい場合
     */
    public void print(final long startOffset, final int len) throws IOException {
        int outputLen;
        int remaining = len;
        long offset = startOffset;

        while (remaining > 0) {
            if (offset < currOffset || offset >= currOffset + remaining) {
                // マップ範囲外の場合
                around(offset);
            }
            outputLen = (int) ((currOffset + remaining) - offset);
            if (outputLen > remaining) {
                outputLen = remaining;
            }
            System.out.write(buff.array(), (int) (offset - currOffset), outputLen);
            offset += outputLen;
            remaining -= outputLen;
        }
    }
}
