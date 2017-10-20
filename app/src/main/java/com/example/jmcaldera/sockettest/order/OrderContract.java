package com.example.jmcaldera.sockettest.order;

import com.example.jmcaldera.sockettest.BasePresenter;
import com.example.jmcaldera.sockettest.BaseView;
import com.example.jmcaldera.sockettest.repository.model.Order;

/**
 * Created by jmcaldera on 19/10/2017.
 */

public class OrderContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showNoOrder();

        void showOrder(Order order);

        void showConnectionEstablished();

        void showErrorOpenConnection();

        void showConnectionClosed();

        void showErrorCloseConnection();

        void showErrorMessage(String msg);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void openConnection();

        void closeConnection();

        void onResume();
    }
}