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
        writer.startBody();
        writer.printlnHeader("Product with max price: ");
        while (rs.next()) {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            writer.printlnProduct(name, price);
        }
        writer.finishBody();
    }
}
