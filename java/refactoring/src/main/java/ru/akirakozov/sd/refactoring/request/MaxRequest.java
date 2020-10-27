package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;

public class MaxRequest extends ListRequest {
    public MaxRequest(PrintWriter w) {
        super(w);
        sqlQuery = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
        header = "Product with max price: ";
    }
}
