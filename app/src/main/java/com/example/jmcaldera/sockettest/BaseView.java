package com.example.jmcaldera.sockettest;

/**
 * Created by jmcaldera on 19/10/2017.
 */

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
