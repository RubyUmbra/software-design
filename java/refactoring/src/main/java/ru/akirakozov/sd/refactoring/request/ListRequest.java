package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ListRequest extends QueryRequest {
    String header;

    ListRequest(PrintWriter printWriter) {
        super(printWriter);
        header = null;
    }

    @Override
    public void printResponse(ResultSet rs) throws SQLException {
        writer.startBody();
        if (header != null) writer.printlnHeader(header);
        while (rs.next()) {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            writer.printlnProduct(name, price);
        }
        writer.finishBody();
    }
}
