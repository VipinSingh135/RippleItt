package com.rippleitt.callback;

/**
 * Created by pc on password/15/2018.
 */

public interface RemovePicCallback {


    public void removePic(String absPath, int type, int positin);
    public void onImageCicked(String absPath, int type,int position,int displayMode);
}
