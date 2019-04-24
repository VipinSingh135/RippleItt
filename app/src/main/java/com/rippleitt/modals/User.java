package com.rippleitt.modals;

import android.net.Uri;

/**
 * Created by manishautomatic on 16/05/18.
 */

public class User {


    private final Uri picture;
    private final String name;
    private final String id;
    private final String email;
    private final String permissions;

    public User(Uri picture, String name,
                String id, String email, String permissions) {
        this.picture = picture;
        this.name = name;
        this.id = id;
        this.email = email;
        this.permissions = permissions;
    }

    public Uri getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPermissions() {
        return permissions;
    }
}
