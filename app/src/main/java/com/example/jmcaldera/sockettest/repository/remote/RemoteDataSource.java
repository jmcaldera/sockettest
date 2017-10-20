package com.example.jmcaldera.sockettest.repository.remote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.jmcaldera.sockettest.order.OrderActivity;
import com.example.jmcaldera.sockettest.repository.DataSource;
import com.example.jmcaldera.sockettest.repository.model.Order;
import com.example.jmcaldera.sockettest.repository.remote.api.ApiConstants;
import com.example.jmcaldera.sockettest.repository.remote.api.SocketHelper;
import com.google.gson.Gson;

import java.net.URISyntaxException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jmcaldera on 19/10/2017.
 */

public class RemoteDataSource implements DataSource {

    private static final String TAG = RemoteDataSource.class.getSimpleName();

    private Context context;

    private static RemoteDataSource INSTANCE;
    private OpenConnectionCallback mOpenConnectionCallback = null;
    private CloseConnectionCallback mCloseConnectionCallback = null;
    private LoadOrderCallback mLoadOrderCallback = null;

    private boolean isConnected = false;

    public static RemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(context);
        }
        return INSTANCE;
    }

    private RemoteDataSource(@NonNull Context context) {
        this.context = checkNotNull(context);

        // Filter para el BR y el IntentService
        IntentFilter filter = new IntentFilter(ApiConstants.ACTION_CONNECT_SERVER);
        filter.addAction(ApiConstants.ACTION_DISCONNECT_SERVER);
        filter.addAction(ApiConstants.ACTION_ORDER_RECEIVED);

        SocketReceiver receiver = new SocketReceiver(context);
        LocalBroadcastManager.getInstance(this.context).registerReceiver(receiver, filter);
    }

    @Override
    public void openConnection(@NonNull final OpenConnectionCallback callback) {
        mOpenConnectionCallback = checkNotNull(callback);

        Intent intent = new Intent(this.context, SocketHelper.class);
        intent.setAction(ApiConstants.ACTION_CONNECT_SERVER);
        context.startService(intent);

    }

    @Override
    public void closeConnection(@NonNull final CloseConnectionCallback callback) {

        mCloseConnectionCallback = checkNotNull(callback);

        Intent intent = new Intent(this.context, SocketHelper.class);
        intent.setAction(ApiConstants.ACTION_DISCONNECT_SERVER);
        context.startService(intent);
    }

    @Override
    public void loadOrder(@NonNull final LoadOrderCallback callback) {
        //TODO: Sustituir el fakedata por la data recibida del servidor y llamar al callback

        String json = "{\n" +
                "    \"id\": 1,\n" +
                "    \"documents\": [{\n" +
                "        \"id\": 2,\n" +
                "        \"name\": \"Docuento uno\",\n" +
                "        \"precio\": 2500\n" +
                "    }, {\n" +
                "        \"id\": 3,\n" +
                "        \"name\": \"Docuento dos\",\n" +
                "        \"precio \": 7500\n" +
                "    }],\n" +
                "    \"subsidiary\": {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Nombre\",\n" +
                "        \"address\": \"Las verbenas 8961\"\n" +
                "    }\n" +
                "}";
        Gson gson = new Gson();
        final Order order = gson.fromJson(json, Order.class);
        Log.d(TAG, order.toString());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "loadOrder Success RemoteDataSource");
                callback.onSuccess(order);

            }
        }, 2000);

    }

    @Override
    public void setLoadOrderCallback(@NonNull final LoadOrderCallback callback) {
        mLoadOrderCallback = checkNotNull(callback);
    }

    @Override
    public void refreshOrder() {
        // No se utiliza
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    private class SocketReceiver extends BroadcastReceiver {
        private Context mContext;
        private SocketReceiver(@NonNull Context context) {
            mContext = checkNotNull(context);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                switch (intent.getAction()) {
                    case ApiConstants.ACTION_CONNECT_SERVER:
                        // Notificar que se conecto al servidor
                        Log.d(TAG, "onSuccess Connect RemoteData");
                        mOpenConnectionCallback.onSuccess();
                        isConnected = true;
                        break;
                    case ApiConstants.ACTION_DISCONNECT_SERVER:
                        // Notificar que se desconecto del servidor
                        Log.d(TAG, "onSuccess Disconnect RemoteData");
                        mCloseConnectionCallback.onSuccess();
                        isConnected = false;
                        break;
                    case ApiConstants.ACTION_ORDER_RECEIVED:
                        // Enviar la data recibida al UI
                        Log.d(TAG, "loadOrder Success RemoteDataSource");
                        String json = intent.getStringExtra(ApiConstants.EXTRA_ORDER_RECEIVED);
                        Log.d(TAG, "Json RemoteDataSource: " + json);
                        // Parsear a Order y enviar
                        Gson gson = new Gson();
                        Order order = gson.fromJson(json, Order.class);
                        mLoadOrderCallback.onSuccess(order);
                        Intent localIntent = new Intent(mContext, OrderActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(localIntent);
                        break;
                }

            }
        }
    }
}
