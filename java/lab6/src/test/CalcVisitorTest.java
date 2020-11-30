package test;

import org.junit.Assert;
import org.junit.Test;
import tokens.*;
import visitors.CalcVisitor;

import java.util.ArrayList;
import java.util.List;

public class CalcVisitorTest {
    @Test
    public void plusTest() {
        List<Token> expr = new ArrayList<>();
        expr.add(new NumberToken(42));
        expr.add(new NumberToken(-42));
        expr.add(new Plus());

        CalcVisitor calcVisitor = new CalcVisitor();
        expr.forEach(tok -> tok.accept(calcVisitor));
        Assert.assertEquals(0, calcVisitor.getResult());
    }

    @Test
    public void minusTest() {
        List<Token> expr = new ArrayList<>();
        expr.add(new NumberToken(42));
        expr.add(new NumberToken(42));
        expr.add(new Minus());

        CalcVisitor calcVisitor = new CalcVisitor();
        expr.forEach(tok -> tok.accept(calcVisitor));
        Assert.assertEquals(0, calcVisitor.getResult());
    }

    @Test
    public void mulTest() {
        List<Token> expr = new ArrayList<>();
        expr.add(new NumberToken(2));
        expr.add(new NumberToken(3));
        expr.add(new Mul());

        CalcVisitor calcVisitor = new CalcVisitor();
        expr.forEach(tok -> tok.accept(calcVisitor));
        Assert.assertEquals(6, calcVisitor.getResult());
    }

    @Test
    public void divTest() {
        List<Token> expr = new ArrayList<>();
        expr.add(new NumberToken(239 * 556));
        expr.add(new NumberToken(556));
        expr.add(new Div());

        CalcVisitor calcVisitor = new CalcVisitor();
        expr.forEach(tok -> tok.accept(calcVisitor));
        Assert.assertEquals(239, calcVisitor.getResult());
    }

    @Test
    public void test() {
        List<Token> expr = new ArrayList<>();
        expr.add(new NumberToken(2));
        expr.add(new NumberToken(3));
        expr.add(new Plus());
        expr.add(new NumberToken(7));
        expr.add(new NumberToken(9));
        expr.add(new Minus());
        expr.add(new Div());
        expr.add(new NumberToken(10));
        expr.add(new Mul());

        CalcVisitor calcVisitor = new CalcVisitor();
        expr.forEach(tok -> tok.accept(calcVisitor));
        Assert.assertEquals(-20, calcVisitor.getResult());
    }

    @Test
    public void divisionByZeroErrorTest() {
        List<Token> expr = new ArrayList<>();
        expr.add(new NumberToken(1));
        expr.add(new NumberToken(0));
        expr.add(new Div());

        try {
            CalcVisitor calcVisitor = new CalcVisitor();
            expr.forEach(tok -> tok.accept(calcVisitor));
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Division by zero error");
        }
    }

    @Test
    public void braceInRpnErrorTest() {
        List<Token> expr = new ArrayList<>();
        expr.add(new Left());

        try {
            CalcVisitor calcVisitor = new CalcVisitor();
            expr.forEach(tok -> tok.accept(calcVisitor));
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Brace in RPN error");
        }
    }

    @Test
    public void incorrectRpnErrorTest() {
        List<Token> expr = new ArrayList<>();
        expr.add(new NumberToken(1));
        expr.add(new Mul());

        try {
            CalcVisitor calcVisitor = new CalcVisitor();
            expr.forEach(tok -> tok.accept(calcVisitor));
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Bad number of operands error");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void uncompletedExpressionErrorTest() {
        List<Token> expr = new ArrayList<>();
        expr.add(new NumberToken(556));
        expr.add(new NumberToken(239));

        CalcVisitor calcVisitor = new CalcVisitor();
        expr.forEach(tok -> tok.accept(calcVisitor));
        calcVisitor.getResult();
    }
}
