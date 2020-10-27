package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;

public abstract class QueryRequest extends Request {
    QueryRequest(PrintWriter w) {
        super(w);
    }
}
