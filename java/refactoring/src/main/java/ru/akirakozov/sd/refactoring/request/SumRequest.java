package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;

public class SumRequest extends NumberRequest {
    public SumRequest(PrintWriter w) {
        super(w);
        sqlQuery = "SELECT SUM(price) FROM PRODUCT";
        header = "Summary price: ";
    }
}
