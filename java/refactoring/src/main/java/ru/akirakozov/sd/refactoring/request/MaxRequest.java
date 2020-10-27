package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaxRequest extends QueryRequest {
    public MaxRequest(PrintWriter w) {
        super(w);
        sqlQuery = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
    }

    @Override
    public void printResponse(ResultSet rs) throws SQLException {
        printResponseWithProducts(rs, "Product with max price: ");
    }
}
