package com.example.jmcaldera.sockettest.order;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmcaldera.sockettest.repository.DataSource;
import com.example.jmcaldera.sockettest.repository.OrderRepository;
import com.example.jmcaldera.sockettest.repository.model.Order;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jmcaldera on 19/10/2017.
 */

public class OrderPresenter implements OrderContract.Presenter {

    private static final String TAG = OrderPresenter.class.getSimpleName();

    private OrderContract.View mView;
    private OrderRepository mRepository;


    public OrderPresenter(@NonNull OrderContract.View view, @NonNull OrderRepository repository) {
        this.mView = checkNotNull(view, "La vista no puede ser null");
        this.mRepository = checkNotNull(repository, "Repository no puede ser null");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // Hacer algo al iniciar
//        loadOrder(true, true);
        setOrderCallback();
    }

    private void setOrderCallback() {
        mRepository.setLoadOrderCallback(new DataSource.LoadOrderCallback() {
            @Override
            public void onSuccess(Order order) {
                Log.d(TAG, "onSuccess loadOrder Presenter");
                mView.showOrder(order);
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError loadOrder Presenter");
                mView.showErrorMessage("Error cargando order");
            }
        });
    }

    private void loadOrder(boolean forceUpdate, boolean showLoading) {
        if (showLoading) {
            mView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mRepository.refreshOrder();
        }
        mRepository.loadOrder(new DataSource.LoadOrderCallback() {
            @Override
            public void onSuccess(Order order) {
                Log.d(TAG, "onSuccess loadOrder Presenter");
                mView.showOrder(order);
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError loadOrder Presenter");
                mView.showErrorMessage("Error cargando order");
            }
        });
    }

    @Override
    public void openConnection() {
        // Comunicarse con el modelo e iniciar la conexion con el servidor
        mRepository.openConnection(new DataSource.OpenConnectionCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess openConn Presenter");
                mView.showConnectionEstablished();
                start();
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError openConn Presenter");
                mView.showErrorOpenConnection();
            }
        });
    }

    @Override
    public void closeConnection() {
        // Comunicarse con el modelo y cerrar la conexion con el servidor
        mRepository.closeConnection(new DataSource.CloseConnectionCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess closeConn Presenter");
                mView.showConnectionClosed();
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError closeConn Presenter");
                mView.showErrorCloseConnection();
            }
        });
    }

    @Override
    public void onResume() {
        if (mRepository.isConnected()) {
            mView.showConnectionEstablished();
            // Solicita ultimo estado.
            mRepository.loadOrder(new DataSource.LoadOrderCallback() {
                @Override
                public void onSuccess(Order order) {
                    Log.d(TAG, "onSuccess loadOrder Presenter");
                    mView.showOrder(order);
                }

                @Override
                public void onError() {
                    Log.d(TAG, "onError loadOrder Presenter");
                    mView.showErrorMessage("Error cargando order");
                }
            });
        }
    }
}
