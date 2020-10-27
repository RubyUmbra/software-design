package ru.akirakozov.sd.refactoring.request;

public class SumRequest extends QueryRequest {
    @Override
    public String getSqlQuery() {
        return "SELECT SUM(price) FROM PRODUCT";
    }
}
