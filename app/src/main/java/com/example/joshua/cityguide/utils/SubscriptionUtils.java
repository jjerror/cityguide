package com.example.joshua.cityguide.utils;

import android.widget.TextView;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * rx subscription utilities
 * <p/>
 * Created by joshua on 1/30/15.
 */
public class SubscriptionUtils {
    private SubscriptionUtils() {
    }

    /**
     * Create a subscription on a string and update the text view when changed
     *
     * @param observable observable for the string
     * @param textView   text view
     * @return the Subscription
     */
    public static Subscription subscribeTextViewText(final Observable<String> observable,
                                                     final TextView textView) {
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        textView.setText(s);
                    }
                });
    }


}
