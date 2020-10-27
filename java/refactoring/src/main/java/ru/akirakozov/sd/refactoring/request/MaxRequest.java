package ru.akirakozov.sd.refactoring.request;

public class MaxRequest extends QueryRequest {
    @Override
    public String getSqlQuery() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
    }
}
