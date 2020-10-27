package ru.akirakozov.sd.refactoring.request;

public class AddRequest implements Request {
    private final String name;
    private final long price;

    public AddRequest(String name, long price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getSqlQuery() {
        return "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
    }
}
