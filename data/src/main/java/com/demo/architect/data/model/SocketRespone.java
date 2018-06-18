package com.demo.architect.data.model;

public class SocketRespone {
    private int result;
    private int connect;

    public SocketRespone() {
    }

    public SocketRespone(int result, int connect) {
        this.result = result;
        this.connect = connect;
    }

    public int getResult() {
        return result;
    }

    public int getConnect() {
        return connect;
    }
}
