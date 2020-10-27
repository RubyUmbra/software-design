package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;

public abstract class QueryRequest extends Request {
    QueryRequest(PrintWriter w) {
        super(w);
    }

    public static QueryRequest getFromCommand(PrintWriter w, String command) {
        if ("max".equals(command))
            return new MaxRequest(w);
        else if ("min".equals(command))
            return new MinRequest(w);
        else if ("sum".equals(command))
            return new SumRequest(w);
        else if ("count".equals(command))
            return new CountRequest(w);
        else
            return new BadRequest(w, command);
    }
}
