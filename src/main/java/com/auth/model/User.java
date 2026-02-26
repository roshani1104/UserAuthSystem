package com.auth.model;

import java.time.LocalDateTime;

public class User {

    private int           id;
    private String        firstName;
    private String        lastName;
    private String        email;
    private String        passwordHash;   
    private LocalDateTime createdAt;

    public User() {}

    public User(String firstName, String lastName, String email, String passwordHash) {
        this.firstName    = firstName;
        this.lastName     = lastName;
        this.email        = email;
        this.passwordHash = passwordHash;
    }

    public int           getId()           { return id; }
    public void          setId(int id)     { this.id = id; }

    public String        getFirstName()               { return firstName; }
    public void          setFirstName(String v)       { this.firstName = v; }

    public String        getLastName()                { return lastName; }
    public void          setLastName(String v)        { this.lastName = v; }

    public String        getEmail()                   { return email; }
    public void          setEmail(String v)           { this.email = v; }

    public String        getPasswordHash()            { return passwordHash; }
    public void          setPasswordHash(String v)    { this.passwordHash = v; }

    public LocalDateTime getCreatedAt()               { return createdAt; }
    public void          setCreatedAt(LocalDateTime v){ this.createdAt = v; }

    public String        getFullName()                { return firstName + " " + lastName; }
}
