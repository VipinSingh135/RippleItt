package com.rippleitt.callback;


import com.rippleitt.modals.ContactTemplate;

import java.util.ArrayList;

/**
 * Created by manishautomatic on 29/01/16.
 */
public interface ItemSelectionCallback {

    public void onSelected(int position);
    public void onUnSelected(int position);
    public void setReferenceListContact(ArrayList<ContactTemplate> refrenceList);

}
