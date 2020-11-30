package test;

import org.junit.Assert;
import org.junit.Test;
import tokens.*;
import visitors.ParserVisitor;

import java.util.ArrayList;
import java.util.List;

public class ParserVisitorTest {
    @Test
    public void baseTest() {
        List<Token> expr = new ArrayList<>();
        expr.add(new Left());
        expr.add(new NumberToken(2));
        expr.add(new Plus());
        expr.add(new NumberToken(3));
        expr.add(new Right());
        expr.add(new Div());
        expr.add(new Left());
        expr.add(new NumberToken(7));
        expr.add(new Minus());
        expr.add(new NumberToken(9));
        expr.add(new Right());
        expr.add(new Mul());
        expr.add(new NumberToken(10));

        List<Token> expected = new ArrayList<>();
        expected.add(new NumberToken(2));
        expected.add(new NumberToken(3));
        expected.add(new Plus());
        expected.add(new NumberToken(7));
        expected.add(new NumberToken(9));
        expected.add(new Minus());
        expected.add(new Div());
        expected.add(new NumberToken(10));
        expected.add(new Mul());

        ParserVisitor parserVisitor = new ParserVisitor();
        expr.forEach(tok -> tok.accept(parserVisitor));
        List<Token> actual = parserVisitor.getRPN();

        Assert.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++)
            Assert.assertEquals(expected.get(i).toString(), actual.get(i).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void notClosedBracketErrorTest() {
        List<Token> expr = new ArrayList<>();
        expr.add(new Right());

        ParserVisitor parserVisitor = new ParserVisitor();
        expr.forEach(tok -> tok.accept(parserVisitor));
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectBracketsErrorTest() {
        List<Token> expr = new ArrayList<>();
        expr.add(new Left());

        ParserVisitor parserVisitor = new ParserVisitor();
        expr.forEach(tok -> tok.accept(parserVisitor));
        parserVisitor.getRPN();
    }
}
