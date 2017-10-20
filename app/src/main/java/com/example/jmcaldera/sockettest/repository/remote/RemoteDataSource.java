package com.example.jmcaldera.sockettest.repository.remote;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

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

    //TODO: Implementar retrofit para consulta http
    //TODO: crear otro hilo para que la conexion se mantenga si se envia el app al background
    //TODO: crear evento para recibir la respuesta http y enviarlo a la app
    //TODO: arrancar la app nuevamente segun el evento y mostrar la respuesta

    private static final String TAG = RemoteDataSource.class.getSimpleName();

    Context context;

    // Socket.io
    private SocketHelper mSocket = new SocketHelper();

    private static RemoteDataSource INSTANCE;

    public static RemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(context);
        }
        return INSTANCE;
    }

    private RemoteDataSource(@NonNull Context context) {
        this.context = checkNotNull(context);
    }

    @Override
    public void openConnection(@NonNull final OpenConnectionCallback callback) {
        checkNotNull(callback);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "openConn Success en RemoteDataSource");
//                callback.onSuccess();
//            }
//        }, 2000);
        mSocket.openConnection(new SocketHelper.OpenSocketConnectionCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });

    }

    @Override
    public void closeConnection(@NonNull final CloseConnectionCallback callback) {
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                callback.onSuccess();
//                Log.d(TAG, "closeConn Success en RemoteDataSource");
//            }
//        }, 2000);

        mSocket.closeConnection(new SocketHelper.CloseSocketConnectionCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess CloseConn RemoteData");
                callback.onSuccess();
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError CloseConn RemoteData");
                callback.onError();
            }
        });
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
        checkNotNull(callback);
        mSocket.setLoadOrderCallback(new SocketHelper.LoadOrderSocketCallback() {
            @Override
            public void onSuccess(Order order) {
                Log.d(TAG, "loadOrder Success RemoteDataSource");
                callback.onSuccess(order);
            }

            @Override
            public void onError() {
                Log.d(TAG, "loadOrder Error RemoteDataSource");
                callback.onError();
            }
        });
    }

    @Override
    public void refreshOrder() {
        // No se utiliza
    }
}
