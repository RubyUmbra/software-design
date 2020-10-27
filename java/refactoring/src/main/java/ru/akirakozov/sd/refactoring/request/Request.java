package ru.akirakozov.sd.refactoring.request;

import ru.akirakozov.sd.refactoring.HtmlWriter;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Request {
    public String sqlQuery;
    protected HtmlWriter writer;
    protected boolean isGoodHtml;
    public boolean isNotQuery;

    Request(PrintWriter printWriter) {
        sqlQuery = "";
        writer = new HtmlWriter(printWriter);
        isGoodHtml = true;
        isNotQuery = false;
    }

    public abstract void printResponse(ResultSet rs) throws SQLException;
}
