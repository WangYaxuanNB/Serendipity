package com.serendipity.demo.entity;

import javafx.beans.property.*;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.sql.Timestamp;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDateTime registrationTime;

    @Column
    private LocalDateTime lastLoginTime;

    private final LongProperty idProperty = new SimpleLongProperty(this, "id");
    private final StringProperty usernameProperty = new SimpleStringProperty(this, "username");
    private final StringProperty passwordProperty = new SimpleStringProperty(this, "password");
    private final StringProperty emailProperty = new SimpleStringProperty(this, "email");
    private final ObjectProperty<LocalDateTime> registrationTimeProperty = new SimpleObjectProperty<>(this, "registrationTime");
    private final ObjectProperty<LocalDateTime> lastLoginTimeProperty = new SimpleObjectProperty<>(this, "lastLoginTime");

    public LongProperty idProperty() { return idProperty; }
    public StringProperty usernameProperty() { return usernameProperty; }
    public StringProperty passwordProperty() { return passwordProperty; }
    public StringProperty emailProperty() { return emailProperty; }
    public ObjectProperty<LocalDateTime> registrationTimeProperty() { return registrationTimeProperty; }
    public ObjectProperty<LocalDateTime> lastLoginTimeProperty() { return lastLoginTimeProperty; }

    public Long getId() { return idProperty.get(); }
    public void setId(Long id) { this.idProperty.set(id); }

    public String getUsername() { return usernameProperty.get(); }
    public void setUsername(String username) { this.usernameProperty.set(username); }

    public String getPassword() { return passwordProperty.get(); }
    public void setPassword(String password) { this.passwordProperty.set(password); }

    public String getEmail() { return emailProperty.get(); }
    public void setEmail(String email) { this.emailProperty.set(email); }

    public LocalDateTime getRegistrationTime() { return registrationTimeProperty.get(); }
    public void setRegistrationTime(LocalDateTime registrationTime) { this.registrationTimeProperty.set(registrationTime); }

    public LocalDateTime getLastLoginTime() { return lastLoginTimeProperty.get(); }
    public void setLastLoginTime(LocalDateTime lastLoginTime) { this.lastLoginTimeProperty.set(lastLoginTime); }

    public User() {}
}
