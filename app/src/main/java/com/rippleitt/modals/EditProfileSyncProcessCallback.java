package com.rippleitt.modals;

/**
 * Created by manishautomatic on 12/12/17.
 */
public interface EditProfileSyncProcessCallback {

    void onInit();
    void onError(EditProfileSyncResponseTemplate template);
    void onSuccess(EditProfileSyncResponseTemplate template);
}
