package ru.akirakozov.sd.refactoring;

import ru.akirakozov.sd.refactoring.request.Request;

import java.sql.*;

public class DbManager {
    public static void execute(Request r) {
        try (
                Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement stmt = c.createStatement()
        ) {
            if (r.isNotQuery) executeUpdate(r, stmt);
            else executeQuery(r, stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeUpdate(Request r, Statement stmt) throws SQLException {
        stmt.executeUpdate(r.sqlQuery);
        r.printResponse(null);
    }

    private static void executeQuery(Request r, Statement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery(r.sqlQuery)) {
            r.printResponse(rs);
        }
    }
}
