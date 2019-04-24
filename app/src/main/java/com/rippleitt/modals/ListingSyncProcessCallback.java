package com.rippleitt.modals;

/**
 * Created by manishautomatic on 12/12/17.
 */
public interface ListingSyncProcessCallback {

    void onInit();
    void onError(ListingSyncResponseTemplate template);
    void onSuccess(ListingSyncResponseTemplate template);
}
