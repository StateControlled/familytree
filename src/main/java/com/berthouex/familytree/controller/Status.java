package com.berthouex.familytree.controller;

public enum Status {
    READY("ready");

    private final String status;
    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
