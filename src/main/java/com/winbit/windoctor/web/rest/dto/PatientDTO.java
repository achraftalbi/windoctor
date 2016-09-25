package com.winbit.windoctor.web.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.winbit.windoctor.domain.util.CustomDateTimeDeserializer;
import com.winbit.windoctor.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class PatientDTO {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    @Pattern(regexp = "^[A-Za-z0-9]*$")
    @Size(min = 1, max = 50)
    private String login;

    @Size(min = 0, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 0, max = 100)
    private String email;

    @Size(min = 2, max = 5)
    private String langKey;

    private List<String> roles;

    private Boolean blocked;

    private Boolean activated;

    @Size(max = 250000)
    private byte[] picture;

    private Boolean currentUserPatient;

    private Boolean maxEventsReached;

    @Size(min = 14, max = 14)
    @Pattern(regexp = "^00212[0-9]*$")
    private String phoneNumber;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime birthDate;

    @Size(min = 0, max = 100)
    private String facebook;

    @Size(min = 1, max = 200)
    private String diseases;

    @Size(min = 1, max = 200)
    private String allergies;

    @Size(min = 1, max = 200)
    private String mutualAssurance;

    @Size(min = 0, max = 200)
    private String profession;

    public String getDiseases() {
        return diseases;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getMutualAssurance() {
        return mutualAssurance;
    }

    public String getProfession() {
        return profession;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public DateTime getBirthDate() {
        return birthDate;
    }

    private Boolean smoking = false;

    private Boolean bleedingWhileBrushing = false;

    private Boolean toothSensitivity = false;

    @Size(max = 200)
    private String referralBy;

    @Size(max = 200)
    private String doctorTreating;

    @Size(max = 500)
    private String address;

    @Size(max = 500)
    private String workingHours;

    public PatientDTO() {
    }

    public PatientDTO(String login, String password, String firstName, String lastName, String email, String langKey,
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

    public PatientDTO(Long id, String login, String password, String firstName, String lastName, String email, String langKey,
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

    public String getAddress() {
        return address;
    }

    public Boolean getBleedingWhileBrushing() {
        return bleedingWhileBrushing;
    }

    public String getDoctorTreating() {
        return doctorTreating;
    }

    public String getReferralBy() {
        return referralBy;
    }

    public Boolean getSmoking() {
        return smoking;
    }

    public Boolean getToothSensitivity() {
        return toothSensitivity;
    }

    public String getWorkingHours() {
        return workingHours;
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
