package com.google.kozake.sh.tail;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OptTest {

    @Test
    public void testオプション0() {
        Opt opt = new Opt(new String[]{}, "Fb:c:fn:qr");
        assertThat(opt.get(), is(-1));
    }

    @Test
    public void testオプション1() {
        Opt opt = new Opt(new String[]{"-F"}, "Fb:c:fn:qr");
        assertThat(opt.get(), is((int) 'F'));
        assertThat(opt.getOpt(), is((int) 'F'));
    }

    @Test
    public void test値オプション() {
        Opt opt = new Opt(new String[]{"-b1"}, "Fb:c:fn:qr");
        assertThat(opt.get(), is((int) 'b'));
        assertThat(opt.getOpt(), is((int) 'b'));
        assertThat(opt.getArg(), is("1"));

        opt = new Opt(new String[]{"-b11"}, "Fb:c:fn:qr");
        assertThat(opt.get(), is((int) 'b'));
        assertThat(opt.getOpt(), is((int) 'b'));
        assertThat(opt.getArg(), is("11"));

        opt = new Opt(new String[]{"-b"}, "Fb:c:fn:qr");
        assertThat(opt.get(), is((int) '?'));
        assertThat(opt.getOpt(), is((int) 'b'));

    }

    @Test
    public void testオプションの連続記入() {
        Opt opt = new Opt(new String[]{"-Fb100"}, "Fb:c:fn:qr");
        assertThat(opt.get(), is((int) 'F'));
        assertThat(opt.getOpt(), is((int) 'F'));

        assertThat(opt.get(), is((int) 'b'));
        assertThat(opt.getOpt(), is((int) 'b'));
        assertThat(opt.getArg(), is("100"));
    }

    @Test
    public void testオプション全指定() {
        Opt opt = new Opt(new String[]{"-Fb100", "-c", "200", "-f", "-n300", "-qr"}, "Fb:c:fn:qr");
        assertThat(opt.get(), is((int) 'F'));
        assertThat(opt.getOpt(), is((int) 'F'));

        assertThat(opt.get(), is((int) 'b'));
        assertThat(opt.getOpt(), is((int) 'b'));
        assertThat(opt.getArg(), is("100"));

        assertThat(opt.get(), is((int) 'c'));
        assertThat(opt.getOpt(), is((int) 'c'));
        assertThat(opt.getArg(), is("200"));

        assertThat(opt.get(), is((int) 'f'));
        assertThat(opt.getOpt(), is((int) 'f'));

        assertThat(opt.get(), is((int) 'n'));
        assertThat(opt.getOpt(), is((int) 'n'));
        assertThat(opt.getArg(), is("300"));

        assertThat(opt.get(), is((int) 'q'));
        assertThat(opt.getOpt(), is((int) 'q'));

        assertThat(opt.get(), is((int) 'r'));
        assertThat(opt.getOpt(), is((int) 'r'));
    }
}
