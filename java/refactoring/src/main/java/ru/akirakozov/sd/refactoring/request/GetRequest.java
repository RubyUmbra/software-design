package ru.akirakozov.sd.refactoring.request;

public class GetRequest implements Request {
    @Override
    public String getSqlQuery() {
        return "SELECT * FROM PRODUCT";
    }
}
