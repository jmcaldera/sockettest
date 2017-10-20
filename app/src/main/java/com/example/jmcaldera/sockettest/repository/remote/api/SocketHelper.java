package com.example.jmcaldera.sockettest.repository.remote.api;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by jmcaldera on 20/10/2017.
 */

public class SocketHelper extends IntentService {

    private static final String TAG = SocketHelper.class.getSimpleName();

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

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onConnect Success SocketHelper");
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
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(ApiConstants.ORDER_EVENT, onOrderReceived);

        // No hace el llamado desde onDisconnect?
        Intent localIntent = new Intent(ApiConstants.ACTION_DISCONNECT_SERVER);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
    }

    private void handleOrderReceived() {

    }

}
