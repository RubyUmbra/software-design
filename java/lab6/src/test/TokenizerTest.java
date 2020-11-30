package test;

import org.junit.Assert;
import org.junit.Test;
import tokens.Token;
import tokens.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class TokenizerTest {
    private final Tokenizer tokenizer = new Tokenizer();

    @Test
    public void baseTest() {
        List<String> expected = new ArrayList<>();
        expected.add("LEFT");
        expected.add("NUMBER(2)");
        expected.add("PLUS");
        expected.add("NUMBER(3)");
        expected.add("RIGHT");
        expected.add("DIV");
        expected.add("LEFT");
        expected.add("NUMBER(7)");
        expected.add("MINUS");
        expected.add("NUMBER(9)");
        expected.add("RIGHT");
        expected.add("MUL");
        expected.add("NUMBER(10)");

        List<Token> actual = tokenizer.tokenize("(2 + 3) /   (7 -   9)  *   10");

        Assert.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++)
            Assert.assertEquals(expected.get(i), actual.get(i).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectMinusErrorTest() {
        tokenizer.tokenize("1 - -");
    }

    @Test(expected = IllegalArgumentException.class)
    public void badSymbolErrorTest() {
        tokenizer.tokenize("x");
    }
}
