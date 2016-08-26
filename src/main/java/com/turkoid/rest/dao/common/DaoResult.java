package com.turkoid.rest.dao.common;

import java.util.List;

/**
 * Created by turkoid on 8/26/2016.
 */
public class DaoResult<T> {
    public static enum Operation {
        CREATED,
        READ,
        UPDATED,
        DELETED,
        NONE
    }

    public static final DaoResult NULL = new DaoResult<>(null, Operation.NONE);

    private int created;
    private int read;
    private int updated;
    private int deleted;
    private Operation operation;
    private T object;
    private List<T> objects;
    private String errorMessage;


    private DaoResult() {
        this.operation = Operation.NONE;
        this.errorMessage = "";
    }

    public DaoResult(T object, Operation operation) {
        this();
        this.object = object;
        setOperation(operation, object == null ? 0 : 1);
    }

    public DaoResult(List<T> objects, Operation operation) {
        this();
        this.objects = objects;
        setOperation(operation, objects == null ? 0 : objects.size());
    }

    public DaoResult(Operation operation, int count) {
        this();
        setOperation(operation, count);
    }

    private void setOperation(Operation operation, int count) {
        this.operation = operation;
        switch (operation) {
            case CREATED:
                this.created = count;
                break;
            case READ:
                this.read = count;
                break;
            case UPDATED:
                this.updated = count;
                break;
            case DELETED:
                this.deleted = count;
                break;
            case NONE:
                this.object = null;
                this.objects = null;
        }
    }

    public int getCreated() {
        return created;
    }

    public int getRead() {
        return read;
    }

    public int getUpdated() {
        return updated;
    }

    public int getDeleted() {
        return deleted;
    }

    public T getObject() {
        return object;
    }

    public List<T> getObjects() {
        return objects;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
