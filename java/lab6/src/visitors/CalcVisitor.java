package visitors;

import tokens.*;

import java.util.ArrayList;
import java.util.List;

public final class CalcVisitor implements TokenVisitor {
    private final List<Integer> stack = new ArrayList<>();

    @Override
    public void visit(NumberToken token) {
        stack.add(token.getValue());
    }

    @Override
    public void visit(Brace token) {
        throw new IllegalArgumentException("Brace in RPN error");
    }

    @Override
    public void visit(Operation token) {
        if (stack.size() < 2)
            throw new IllegalArgumentException("Bad number of operands error");
        int b = stack.remove(stack.size() - 1);
        int a = stack.remove(stack.size() - 1);
        stack.add(applyOperation(token, a, b));
    }

    public int getResult() {
        if (stack.size() != 1)
            throw new IllegalArgumentException("Incorrect expression error");
        return stack.remove(0);
    }

    private int applyOperation(Operation token, int a, int b) {
        if (token instanceof Plus) return a + b;
        else if (token instanceof Minus) return a - b;
        else if (token instanceof Mul) return a * b;
        else if (token instanceof Div) {
            if (b == 0)
                throw new IllegalArgumentException("Division by zero error");
            return a / b;
        } else throw new IllegalArgumentException("Illegal operation token");
    }
}
