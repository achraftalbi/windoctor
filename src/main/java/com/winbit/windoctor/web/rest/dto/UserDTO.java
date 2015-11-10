package com.winbit.windoctor.web.rest.dto;

import org.hibernate.validator.constraints.Email;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private Long id;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(min = 2, max = 5)
    private String langKey;

    private List<String> roles;

    private Boolean blocked;

    private Boolean activated;

    @Size(max = 1000000)
    private byte[] picture;

    private Boolean currentUserPatient;

    private Boolean maxEventsReached;

    public UserDTO() {
    }

    public UserDTO(String login, String password, String firstName, String lastName, String email, String langKey,
                   List<String> roles, Boolean blocked, byte[] picture, Boolean activated) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.langKey = langKey;
        this.roles = roles;
        this.blocked = blocked;
        this.picture = picture;
        this.activated = activated;
    }

    public UserDTO(Long id, String login, String password, String firstName, String lastName, String email, String langKey,
                   List<String> roles, Boolean currentUserPatient, Boolean maxEventsReached) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.langKey = langKey;
        this.roles = roles;
        this.currentUserPatient = currentUserPatient;
        this.maxEventsReached = maxEventsReached;
    }

    public Long getId() {
        return id;
    }

    public Boolean getCurrentUserPatient() {
        return currentUserPatient;
    }

    public Boolean getMaxEventsReached() { return maxEventsReached;
    }

    public byte[] getPicture() {
        return picture;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getLangKey() {
        return langKey;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Boolean getActivated() {
        return activated;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", password='" + password + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", langKey='" + langKey + '\'' +
            ", roles=" + roles +
            '}';
    }
}
