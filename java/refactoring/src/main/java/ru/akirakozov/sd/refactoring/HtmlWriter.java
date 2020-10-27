package ru.akirakozov.sd.refactoring;

import java.io.PrintWriter;

public class HtmlWriter {
    private final PrintWriter w;

    public HtmlWriter(PrintWriter w) {
        this.w = w;
    }

    public void startBody() {
        w.println("<html><body>");
    }

    public void finishBody() {
        w.println("</body></html>");
    }

    public void println(String str) {
        w.println(str);
    }

    public void println(int n) {
        w.println(n);
    }

    public void printlnHeader(String str) {
        println("<h1>" + str + "</h1>");
    }

    public void printlnProduct(String name, long price) {
        println(name + "\t" + price + "</br>");
    }
}
