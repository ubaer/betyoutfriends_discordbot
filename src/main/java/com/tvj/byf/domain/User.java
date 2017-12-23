package com.tvj.byf.domain;

public class User {
    private String id;
    private String username;
    private String discriminator;

    public User(String id, String username, String discriminator) {
        this.id = id;
        this.username = username;
        this.discriminator = discriminator;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDiscriminator() {
        return discriminator;
    }

}
