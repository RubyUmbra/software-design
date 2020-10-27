package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;
import java.sql.ResultSet;

public class AddRequest extends Request {
    public AddRequest(PrintWriter w, String name, long price) {
        super(w);
        sqlQuery = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
        isGoodHtml = false;
        isNotQuery = true;
    }

    @Override
    public void printResponse(ResultSet rs) {
        writer.println("OK");
    }
}
