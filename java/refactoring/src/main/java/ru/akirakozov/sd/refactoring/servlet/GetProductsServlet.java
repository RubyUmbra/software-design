package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DbManager;
import ru.akirakozov.sd.refactoring.request.GetRequest;
import ru.akirakozov.sd.refactoring.request.Request;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Request r = new GetRequest(response.getWriter());
        DbManager.execute(r);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
