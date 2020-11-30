package test;

import org.junit.Assert;
import org.junit.Test;
import tokens.*;

public class TokensStringReprTest {
    @Test
    public void test() {
        Assert.assertEquals("LEFT", new Left().toString());
        Assert.assertEquals("RIGHT", new Right().toString());
        Assert.assertEquals("PLUS", new Plus().toString());
        Assert.assertEquals("MINUS", new Minus().toString());
        Assert.assertEquals("MUL", new Mul().toString());
        Assert.assertEquals("DIV", new Div().toString());
        Assert.assertEquals("NUMBER(0)", new NumberToken(0).toString());
    }
}
