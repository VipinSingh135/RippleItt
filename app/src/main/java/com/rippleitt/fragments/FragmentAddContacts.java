package com.rippleitt.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.rippleitt.R;
import com.rippleitt.activities.ActivityAddContacts;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddContacts extends Fragment {

    Button mbtnAddNewContact;

    public FragmentAddContacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contacts, container, false);

        mbtnAddNewContact=(Button)view.findViewById(R.id.btnAddNewContact);
        work();
        return view;
    }

    public void work(){
        mbtnAddNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), ActivityAddContacts.class));
            }
        });
    }
}
