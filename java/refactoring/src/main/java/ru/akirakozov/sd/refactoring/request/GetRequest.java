package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;

public class GetRequest extends ListRequest {
    public GetRequest(PrintWriter w) {
        super(w);
        sqlQuery = "SELECT * FROM PRODUCT";
    }
}
