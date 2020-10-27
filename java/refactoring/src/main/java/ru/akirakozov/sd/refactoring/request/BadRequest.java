package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;
import java.sql.ResultSet;

public class BadRequest extends QueryRequest {
    String command;

    public BadRequest(PrintWriter w, String command) {
        super(w);
        this.command = command;
    }

    @Override
    public void printResponse(ResultSet rs) {
        writer.println("Unknown command: " + command);
    }
}
