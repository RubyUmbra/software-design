package ru.akirakozov.sd.refactoring.request;

public class CountRequest extends QueryRequest {
    @Override
    public String getSqlQuery() {
        return "SELECT COUNT(*) FROM PRODUCT";
    }
}
