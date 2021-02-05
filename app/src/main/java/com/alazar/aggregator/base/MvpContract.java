package com.alazar.aggregator.base;

public interface MvpContract {

    interface Presenter<V extends View> {

        void attachView(V view);

        void detachView();

    }

    interface View {
    }
}