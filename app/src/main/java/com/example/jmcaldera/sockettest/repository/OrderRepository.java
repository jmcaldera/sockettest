package com.example.jmcaldera.sockettest.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmcaldera.sockettest.repository.model.Order;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jmcaldera on 19/10/2017.
 */

public class OrderRepository implements DataSource {

    private static final String TAG = OrderRepository.class.getSimpleName();

    private static OrderRepository INSTANCE = null;

    private DataSource mRemoteDataSource;

    private LoadOrderCallback mLoadOrderCallback = null;

    private Order mCachedOrder;

    private boolean mCacheIsOutdated = false;

    // Constructor privado
    private OrderRepository(@NonNull DataSource mRemoteDataSource) {
        this.mRemoteDataSource = checkNotNull(mRemoteDataSource);
    }

    /**
     * Retorna una unica instancia del repositorio
     * @param remoteDataSource el backend dataSource
     * @return la instancia de {@link OrderRepository}
     */
    public static OrderRepository getInstance(DataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new OrderRepository(remoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void openConnection(@NonNull final OpenConnectionCallback callback) {
        checkNotNull(callback);

        mRemoteDataSource.openConnection(new OpenConnectionCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess openConn en Repository");
                callback.onSuccess();
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError openConn en Repository");
                callback.onError();
            }
        });
    }

    @Override
    public void closeConnection(@NonNull final CloseConnectionCallback callback) {
        checkNotNull(callback);

        mRemoteDataSource.closeConnection(new CloseConnectionCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess closeConn en Repository");
                callback.onSuccess();
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError closeConn en Repository");
                callback.onError();
            }
        });
    }

    @Override
    public void loadOrder(@NonNull LoadOrderCallback callback) {
        checkNotNull(callback);

        if (mCachedOrder != null) {
            callback.onSuccess(mCachedOrder);
        } else {
            callback.onError();
        }
    }

    @Override
    public void setLoadOrderCallback(@NonNull final LoadOrderCallback callback) {
        mLoadOrderCallback = checkNotNull(callback);
        mRemoteDataSource.setLoadOrderCallback(new LoadOrderCallback() {
            @Override
            public void onSuccess(Order order) {
                refreshCache(order);
                callback.onSuccess(order);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void refreshOrder() {
        mCacheIsOutdated = true;
    }

    @Override
    public boolean isConnected() {
        return mRemoteDataSource.isConnected();
    }

    private void refreshCache(Order order) {
        mCachedOrder = order;
        mCacheIsOutdated = false;
    }
}
