package com.rosreestr;

public interface RRConnection {

    String send(String message);
    void close();

}
