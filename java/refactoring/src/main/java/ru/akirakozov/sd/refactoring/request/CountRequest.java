package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountRequest extends QueryRequest {
    public CountRequest(PrintWriter w) {
        super(w);
        sqlQuery = "SELECT COUNT(*) FROM PRODUCT";
    }

    @Override
    public void printResponse(ResultSet rs) throws SQLException {
        writer.startBody();
        writer.println("Number of products: ");

        if (rs.next()) {
            writer.println(rs.getInt(1));
        }
        writer.finishBody();
    }
}
