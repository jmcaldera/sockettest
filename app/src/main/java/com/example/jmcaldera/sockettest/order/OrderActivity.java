package com.example.jmcaldera.sockettest.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmcaldera.sockettest.R;
import com.example.jmcaldera.sockettest.repository.OrderRepository;
import com.example.jmcaldera.sockettest.repository.model.Order;
import com.example.jmcaldera.sockettest.repository.remote.RemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class OrderActivity extends AppCompatActivity implements OrderContract.View{

    private static final String TAG = OrderActivity.class.getSimpleName();

    static boolean active = false;

    private OrderContract.Presenter mPresenter;

    private Button mBtnConnect;
    private Button mBtnDisconnect;
    private TextView mOrderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        new OrderPresenter(this, OrderRepository.getInstance(RemoteDataSource.getInstance(this)));

        mBtnConnect = (Button) findViewById(R.id.btn_connect);
        mBtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click en conectar");
                mPresenter.openConnection();
                mBtnConnect.setEnabled(false);
            }
        });

        mBtnDisconnect = (Button) findViewById(R.id.btn_disconnect);
        mBtnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click en desconectar");
                mPresenter.closeConnection();
                mBtnDisconnect.setEnabled(false);
            }
        });
        mOrderText = (TextView) findViewById(R.id.order_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void setPresenter(OrderContract.Presenter presenter) {
        this.mPresenter = checkNotNull(presenter, "El presenter no puede ser null");
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        Log.d(TAG, "Loading indicator is: " + active);
    }

    @Override
    public void showNoOrder() {
        Log.d(TAG," No hay orders");
    }

    @Override
    public void showOrder(Order order) {
        Log.d(TAG, "Orders recibidas");
        Log.d(TAG, order.toString());
        mOrderText.setText(order.toString());
        Toast.makeText(this, "Order recibida", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConnectionEstablished() {
        Log.d(TAG, "openConn Success View");

        mBtnConnect.setVisibility(View.GONE);
        mBtnDisconnect.setVisibility(View.VISIBLE);
        mBtnDisconnect.setEnabled(true);

        Toast.makeText(this, "Conectado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorOpenConnection() {
        Log.d(TAG, "openConn Error View");
        Toast.makeText(this, "Error al conectar", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConnectionClosed() {
        Log.d(TAG, "closeConn Success View");

        mBtnDisconnect.setVisibility(View.GONE);
        mBtnConnect.setVisibility(View.VISIBLE);
        mBtnConnect.setEnabled(true);

        mOrderText.setText(getString(R.string.no_order));
        Toast.makeText(this, "Desconectado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorCloseConnection() {
        Log.d(TAG, "closeConn Error View");
        Toast.makeText(this, "Error al desconectar", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String msg) {
        Log.d(TAG, "Error al cargar");
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
