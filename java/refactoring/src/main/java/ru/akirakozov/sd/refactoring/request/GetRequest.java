package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetRequest extends Request {
    public GetRequest(PrintWriter w) {
        super(w);
        sqlQuery = "SELECT * FROM PRODUCT";
    }

    @Override
    public void printResponse(ResultSet rs) throws SQLException {
        writer.startBody();
        while (rs.next()) {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            writer.printlnProduct(name, price);
        }
        writer.finishBody();
    }
}
