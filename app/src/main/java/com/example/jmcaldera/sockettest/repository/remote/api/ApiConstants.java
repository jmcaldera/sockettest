package com.example.jmcaldera.sockettest.repository.remote.api;

/**
 * Created by jmcaldera on 20/10/2017.
 */

public class ApiConstants {

    public static final String SOCKET_SERVER = "http://192.168.0.6/";

    public static final String ORDER_EVENT = "order_received";

    public static final String ACTION_CONNECT_SERVER = "com.example.jmcaldera.sockettest.action.CONNECT_SERVER";
    public static final String ACTION_DISCONNECT_SERVER = "com.example.jmcaldera.sockettest.action.DISCONNECT_SERVER";
    public static final String ACTION_CONNECT_ERROR = "com.example.jmcaldera.sockettest.action.CONNECT_ERROR";
    public static final String ACTION_ORDER_RECEIVED = "com.example.jmcaldera.sockettest.action.ORDER_RECEIVED";
    public static final String EXTRA_ORDER_RECEIVED = "com.example.jmcaldera.sockettest.action.ORDER";
}
