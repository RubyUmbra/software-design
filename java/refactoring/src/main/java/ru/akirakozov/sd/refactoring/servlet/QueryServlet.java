package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.request.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        Request r = QueryRequest.getFromCommand(response.getWriter(), command);
        try {
            if (r instanceof BadRequest) {
                r.printResponse(null);
            } else {
                try (
                        Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
                        Statement stmt = c.createStatement();
                        ResultSet rs = stmt.executeQuery(r.sqlQuery)
                ) {
                    r.printResponse(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
