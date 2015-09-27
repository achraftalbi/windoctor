package com.winbit.windoctor.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Patient.
 */
@Entity
@Table(name = "PATIENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="patient")
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[a-z0-9]*$")        
    @Column(name = "login", length = 50, nullable = false)
    private String login;

    @NotNull
    @Size(min = 8, max = 100)        
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Size(max = 50)        
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)        
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Size(max = 100)        
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "activated")
    private Boolean activated;
    
    @Column(name = "blocked")
    private Boolean blocked;

    @Size(max = 1000000)        
    @Lob
    @Column(name = "picture")
    private byte[] picture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Patient patient = (Patient) o;

        if ( ! Objects.equals(id, patient.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", login='" + login + "'" +
                ", password='" + password + "'" +
                ", firstName='" + firstName + "'" +
                ", lastName='" + lastName + "'" +
                ", email='" + email + "'" +
                ", activated='" + activated + "'" +
                ", blocked='" + blocked + "'" +
                ", picture='" + picture + "'" +
                '}';
    }
}
