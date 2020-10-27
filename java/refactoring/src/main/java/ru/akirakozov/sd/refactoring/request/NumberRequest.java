package ru.akirakozov.sd.refactoring.request;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class NumberRequest extends QueryRequest {
    String header;

    NumberRequest(PrintWriter w) {
        super(w);
        header = null;
    }

    @Override
    public void printResponse(ResultSet rs) throws SQLException {
        writer.startBody();
        if (header != null) writer.println(header);
        if (rs.next()) writer.println(rs.getInt(1));
        writer.finishBody();
    }
}
