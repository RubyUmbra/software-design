package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddProductServletTest extends ServletTest {
    protected AddProductServletTest() {
        super(new AddProductServlet());
    }

    @Test
    @DisplayName("Add product to empty DB")
    protected void testEmptyDB() throws ServletException, IOException, SQLException {
        dbInsertByRequest("a", 556);

        servletService();

        assertEquals("OK\n", getResponse());
        assertEquals(getExpected(), dbSelectAll());
    }

    @Test
    @DisplayName("Add three products to empty DB")
    protected void testEmptyDB3() throws ServletException, IOException, SQLException {
        dbInsertByRequest("a", 556);
        servletService();

        dbInsertByRequest("bbb", 239);
        servletService();

        dbInsertByRequest("c&7", 30);
        servletService();

        assertEquals("OK\nOK\nOK\n", getResponse());
        assertEquals(getExpected(), dbSelectAll());
    }
}
