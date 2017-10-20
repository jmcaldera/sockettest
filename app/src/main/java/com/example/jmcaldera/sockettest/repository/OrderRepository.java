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

    Order mCachedOrder;

    boolean mCacheIsOutdated = false;

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

        if (!mCacheIsOutdated) {
            Log.d(TAG, "Return cachedOrder");
            callback.onSuccess(mCachedOrder);
            return;
        } else {
            Log.d(TAG, "Loading from remote");
            loadOrderFromRemoteDataSource(callback);
        }
    }

    @Override
    public void refreshOrder() {
        mCacheIsOutdated = true;
    }

    private void loadOrderFromRemoteDataSource(@NonNull final LoadOrderCallback callback) {
        checkNotNull(callback);
        mRemoteDataSource.loadOrder(new LoadOrderCallback() {
            @Override
            public void onSuccess(Order order) {
                refreshCache(order);
                callback.onSuccess(mCachedOrder);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    private void refreshCache(Order order) {
        mCachedOrder = order;
        mCacheIsOutdated = false;
    }
}