package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;

public class MinRequest extends ListRequest {
    public MinRequest(PrintWriter w) {
        super(w);
        sqlQuery = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
        header = "Product with min price: ";
    }
}
