package ru.akirakozov.sd.refactoring.request;

public class MinRequest extends QueryRequest {
    @Override
    public String getSqlQuery() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
    }
}
