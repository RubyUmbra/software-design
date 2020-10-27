package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SumRequest extends QueryRequest {

    public SumRequest(PrintWriter w) {
        super(w);
        sqlQuery = "SELECT SUM(price) FROM PRODUCT";
    }

    @Override
    public void printResponse(ResultSet rs) throws SQLException {
        writer.startBody();
        writer.println("Summary price: ");
        if (rs.next()) {
            writer.println(rs.getInt(1));
        }
        writer.finishBody();
    }
}
