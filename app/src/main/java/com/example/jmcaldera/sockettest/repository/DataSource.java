package com.example.jmcaldera.sockettest.repository;

import com.example.jmcaldera.sockettest.repository.model.Order;

/**
 * Created by jmcaldera on 19/10/2017.
 */

public interface DataSource {

    interface OpenConnectionCallback {
        void onSuccess();
        void onError();
    }

    interface CloseConnectionCallback {
        void onSuccess();
        void onError();
    }

    interface LoadOrderCallback {
        void onSuccess(Order order);
        void onError();
    }

    void openConnection(OpenConnectionCallback callback);

    void closeConnection(CloseConnectionCallback callback);

    void loadOrder(LoadOrderCallback callback);

    void refreshOrder();
}
