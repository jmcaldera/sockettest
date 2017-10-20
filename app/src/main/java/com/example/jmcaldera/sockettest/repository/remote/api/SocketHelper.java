package com.example.jmcaldera.sockettest.repository.remote.api;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.jmcaldera.sockettest.repository.model.Order;
//import com.github.nkzawa.emitter.Emitter;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jmcaldera on 20/10/2017.
 */

public class SocketHelper extends IntentService {

    private static final String TAG = SocketHelper.class.getSimpleName();

//    private OpenSocketConnectionCallback mOpenConnectionCallback = null;
//    private CloseSocketConnectionCallback mCloseConnectionCallback = null;
//    private LoadOrderSocketCallback mOrderCallback = null;

    // Socket.io
    private Socket mSocket;

    public SocketHelper() {
        super("SocketIntentService");
    }

    {
        try {
            mSocket = IO.socket(ApiConstants.SOCKET_SERVER);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.d(TAG, "Error creando el socket: " + e.getMessage());
        }
    }

    public void openConnection(@NonNull OpenSocketConnectionCallback connectionCallback) {
        Log.d(TAG, "Entro a openConn en Socket");
//        mOpenConnectionCallback = checkNotNull(connectionCallback);
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(ApiConstants.ORDER_EVENT, onOrderReceived);
        mSocket.connect();
    }

    public void closeConnection(@NonNull CloseSocketConnectionCallback callback) {
//        mCloseConnectionCallback = checkNotNull(callback);
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(ApiConstants.ORDER_EVENT, onOrderReceived);
    }

    public void setLoadOrderCallback(@NonNull LoadOrderSocketCallback orderCallback) {
//        mOrderCallback = checkNotNull(orderCallback);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onConnect Success SocketHelper");
//                    mOpenConnectionCallback.onSuccess();
//                    mCloseConnectionCallback = null;
                    Intent localIntent = new Intent(ApiConstants.ACTION_CONNECT_SERVER);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
                }
            }).start();
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onDisconnect success SocketHelper");
//                    mCloseConnectionCallback.onSuccess();
//                    mOpenConnectionCallback = null;
//                    mOrderCallback = null;
                    Intent localIntent = new Intent(ApiConstants.ACTION_DISCONNECT_SERVER);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
                }
            }).start();
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onConnect Error SocketHelper");
//                    mOpenConnectionCallback.onError();
//                    mOpenConnectionCallback = null;
//                    mCloseConnectionCallback = null;
                    Intent localIntent = new Intent(ApiConstants.ACTION_CONNECT_ERROR);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
                }
            }).start();
        }
    };


    private Emitter.Listener onOrderReceived = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onSuccess orderReceived SocketHelper");
                    Log.d(TAG, "Json Socket: " + args[0].toString());
//                    Gson gson = new Gson();
//                    Order order = gson.fromJson((String) args[0], Order.class);
//                    mOrderCallback.onSuccess(order);
                    Intent localIntent = new Intent(ApiConstants.ACTION_ORDER_RECEIVED);
                    localIntent.putExtra(ApiConstants.EXTRA_ORDER_RECEIVED, args[0].toString());
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);

                }
            }).start();
        }
    };

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            handleAction(action);
        }
    }

    private void handleAction(String action) {
        switch (action) {
            case ApiConstants.ACTION_CONNECT_SERVER:
                handleConnectServer();
                break;
            case ApiConstants.ACTION_DISCONNECT_SERVER:
                handleDisconnectServer();
                break;
            case ApiConstants.ACTION_ORDER_RECEIVED:
                handleOrderReceived();
                break;
        }
    }

    private void handleConnectServer() {
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(ApiConstants.ORDER_EVENT, onOrderReceived);
        mSocket.connect();
    }

    private void handleDisconnectServer() {
        Log.d(TAG, "Entro a Disconnect Server");
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(ApiConstants.ORDER_EVENT, onOrderReceived);
        Intent localIntent = new Intent(ApiConstants.ACTION_DISCONNECT_SERVER);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
    }

    private void handleOrderReceived() {

    }

    public interface OpenSocketConnectionCallback {
        void onSuccess();
        void onError();
    }

    public interface CloseSocketConnectionCallback {
        void onSuccess();
        void onError();
    }

    public interface LoadOrderSocketCallback {
        void onSuccess(Order order);
        void onError();
    }
}
