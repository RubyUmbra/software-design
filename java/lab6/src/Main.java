import tokens.Token;
import tokens.Tokenizer;
import visitors.CalcVisitor;
import visitors.ParserVisitor;
import visitors.PrintVisitor;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Token> tokens;
        try (Scanner in = new Scanner(System.in)) {
            String str = in.nextLine();
            tokens = new Tokenizer().tokenize(str);
        }

        ParserVisitor parserVisitor = new ParserVisitor();
        tokens.forEach(tok -> tok.accept(parserVisitor));
        List<Token> rpn = parserVisitor.getRPN();

        PrintVisitor printVisitor = new PrintVisitor();
        rpn.forEach(tok -> {
            tok.accept(printVisitor);
            System.out.print(" ");
        });
        System.out.println();

        CalcVisitor calcVisitor = new CalcVisitor();
        rpn.forEach(tok -> tok.accept(calcVisitor));
        System.out.println(calcVisitor.getResult());
    }
}
