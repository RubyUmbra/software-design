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

    protected void printResponseWithProducts(ResultSet rs, String header) throws SQLException {
        writer.startBody();
        if (header != null) writer.printlnHeader(header);
        while (rs.next()) {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            writer.printlnProduct(name, price);
        }
        writer.finishBody();
    }

    protected void printResponseWithNumber(ResultSet rs, String header) throws SQLException {
        writer.startBody();
        if (header != null) writer.println(header);
        if (rs.next()) {
            writer.println(rs.getInt(1));
        }
        writer.finishBody();
    }
}
