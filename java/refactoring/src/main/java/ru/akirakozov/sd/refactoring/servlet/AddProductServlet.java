package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.request.AddRequest;
import ru.akirakozov.sd.refactoring.request.Request;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));
        Request r = new AddRequest(response.getWriter(), name, price);
        try {
            try (
                    Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
                    Statement stmt = c.createStatement()
            ) {
                stmt.executeUpdate(r.sqlQuery);
                r.printResponse(null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
