package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryServletTest extends ServletTest {
    QueryServletTest() {
        super(new QueryServlet());
    }

    @Test
    @DisplayName("Test max")
    protected void testMax() throws ServletException, IOException, SQLException {
        dbInsert("a", 30);
        dbInsert("bbb", 239);
        dbInsert("c4$", 556);

        setRequestCommand("max");

        servletService();

        assertEquals("<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                "c4$\t556</br>\n" +
                "</body></html>\n", getResponse());
    }

    @Test
    @DisplayName("Test min")
    protected void testMin() throws ServletException, IOException, SQLException {
        dbInsert("a", 30);
        dbInsert("bbb", 239);
        dbInsert("c4$", 556);

        setRequestCommand("min");

        servletService();

        assertEquals("<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                "a\t30</br>\n" +
                "</body></html>\n", getResponse());
    }

    @Test
    @DisplayName("Test sum")
    protected void testSum() throws ServletException, IOException, SQLException {
        dbInsert("a", 30);
        dbInsert("bbb", 239);
        dbInsert("c4$", 556);

        setRequestCommand("sum");

        servletService();

        assertEquals("<html><body>\n" +
                "Summary price: \n" +
                "825\n" +
                "</body></html>\n", getResponse());
    }

    @Test
    @DisplayName("Test count")
    protected void testCount() throws ServletException, IOException, SQLException {
        dbInsert("a", 30);
        dbInsert("bbb", 239);
        dbInsert("c4$", 556);

        setRequestCommand("count");

        servletService();

        assertEquals("<html><body>\n" +
                "Number of products: \n" +
                "3\n" +
                "</body></html>\n", getResponse());
    }

    @Test
    @DisplayName("Test unknown")
    protected void testUnknown() throws ServletException, IOException, SQLException {
        servletService();

        assertEquals("Unknown command: null\n", getResponse());
    }
}
