package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.request.GetRequest;
import ru.akirakozov.sd.refactoring.request.Request;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Request r = new GetRequest(response.getWriter());
        try {
            try (
                    Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery(r.sqlQuery)
            ) {
                r.printResponse(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
