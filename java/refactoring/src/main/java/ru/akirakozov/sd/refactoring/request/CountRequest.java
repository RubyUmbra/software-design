package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;

public class CountRequest extends NumberRequest {
    public CountRequest(PrintWriter w) {
        super(w);
        sqlQuery = "SELECT COUNT(*) FROM PRODUCT";
        header = "Number of products: ";
    }
}
