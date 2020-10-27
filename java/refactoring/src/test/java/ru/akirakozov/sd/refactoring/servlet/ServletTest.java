package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;

import static org.mockito.Mockito.when;

abstract class ServletTest {
    private final HttpServlet servlet;

    private Connection connection;
    private Statement statement;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private StringWriter responseWriter;
    private StringBuilder responseBuilder;

    private AutoCloseable mocks;

    ServletTest(HttpServlet servlet) {
        this.servlet = servlet;
    }


    void dbCreate() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    void dbDrop() throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS PRODUCT");
    }

    void dbInsert(String name, Integer price) throws SQLException {
        statement.executeUpdate("INSERT INTO PRODUCT(NAME, PRICE) VALUES ('" + name + "', " + price + ")");
        responseBuilder.append(name).append("\t").append(price).append("</br>\n");
    }

    void dbInsertByRequest(String name, Integer price) {
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("price")).thenReturn(price.toString());
        responseBuilder.append(name).append("\t").append(price).append("</br>\n");
    }

    String dbSelectAll() throws SQLException {
        StringBuilder sb = new StringBuilder().append("<html><body>\n");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT");
        while (resultSet.next()) {
            sb.append(resultSet.getString("name"))
                    .append("\t")
                    .append(resultSet.getString("price"))
                    .append("</br>\n");
        }
        sb.append("</body></html>\n");
        return sb.toString();
    }


    void setRequestCommand(String command) {
        when(request.getParameter("command")).thenReturn(command);
    }

    void servletService() throws ServletException, IOException {
        servlet.service(request, response);
    }

    String getExpected() {
        return responseBuilder.append("</body></html>\n").toString();
    }

    String getResponse() {
        return responseWriter.toString();
    }

    @BeforeEach
    void init() throws SQLException, IOException {
        connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        statement = connection.createStatement();
        dbCreate();
        mocks = MockitoAnnotations.openMocks(this);
        responseWriter = new StringWriter();
        when(request.getMethod()).thenReturn("GET");
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
        responseBuilder = new StringBuilder();
        responseBuilder.append("<html><body>\n");
    }

    @AfterEach
    void tearDown() throws Exception {
        responseWriter.close();
        mocks.close();
        dbDrop();
        statement.close();
        connection.close();
    }
}
