package com.rippleitt.modals;

/**
 * Created by pc on password/22/2018.
 */

public interface IoCallback {

    void onInit();
    void onError(Object responseTemplate);
    void onSuccess(Object responseTemplate);
}
