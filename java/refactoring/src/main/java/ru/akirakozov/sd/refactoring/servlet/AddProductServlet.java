package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DbManager;
import ru.akirakozov.sd.refactoring.request.AddRequest;
import ru.akirakozov.sd.refactoring.request.Request;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));
        Request r = new AddRequest(response.getWriter(), name, price);
        DbManager.execute(r);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
