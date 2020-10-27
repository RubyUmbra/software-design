package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DbManager;
import ru.akirakozov.sd.refactoring.request.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        Request r = QueryRequest.getFromCommand(response.getWriter(), command);
        if (r instanceof BadRequest)
            ((BadRequest) r).printResponse(null);
        else DbManager.execute(r);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
