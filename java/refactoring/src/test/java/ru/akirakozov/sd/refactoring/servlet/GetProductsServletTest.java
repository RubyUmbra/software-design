package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetProductsServletTest extends ServletTest {
    protected GetProductsServletTest() {
        super(new GetProductsServlet());
    }

    @Test
    @DisplayName("Get products from empty DB")
    protected void testEmptyDB() throws ServletException, IOException {
        servletService();

        assertEquals(getExpected(), getResponse());
    }

    @Test
    @DisplayName("Get products from non empty DB")
    protected void testNonEmptyDB() throws ServletException, IOException, SQLException {
        dbInsert("a", 30);
        dbInsert("bbb", 239);
        dbInsert("c4$", 556);

        servletService();

        assertEquals(getExpected(), getResponse());
    }
}
