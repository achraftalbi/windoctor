package com.winbit.windoctor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.winbit.windoctor.domain.util.BooleanToStringConverter;
import com.winbit.windoctor.domain.util.CustomDateTimeDeserializer;
import com.winbit.windoctor.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import org.springframework.data.elasticsearch.annotations.Document;
import javax.persistence.*;
import org.hibernate.annotations.Type;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

/**
 * A user.
 */
@Entity
@Table(name = "JHI_USER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="user")
public class User extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(length = 60)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(max = 100)
    @Column(length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean activated = false;

    @Size(min = 2, max = 5)
    @Column(name = "lang_key", length = 5)
    private String langKey;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    private String resetKey;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "reset_date", nullable = true)
    private DateTime resetDate = null;

    @Column(name = "blocked")
    private Boolean blocked;

    @Size(max = 250000)
    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Min(value = 0)
    @Max(value = 25000000)
    @Column(name = "initial_balance", precision=10, scale=2, nullable = false)
    private BigDecimal initial_balance;

    @Column(nullable = false)
    private Boolean smoking = false;

    @Column(nullable = false)
    private Boolean bleedingWhileBrushing = false;

    @Column(nullable = false)
    private Boolean toothSensitivity = false;

    @Size(max = 200)
    @Column(name = "referral_by", length = 200)
    private String referralBy;

    @Size(max = 200)
    @Column(name = "doctor_treating", length = 200)
    private String doctorTreating;

    @Size(max = 500)
    @Column(name = "address", length = 500)
    private String address;

    @Size(max = 500)
    @Column(name = "workingHours", length = 500)
    private String workingHours;


    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "JHI_USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Authority> authorities = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PersistentToken> persistentTokens = new HashSet<>();

    @ManyToOne
    private Structure structure;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Treatment> doctorTreatments = new HashSet<>();

    @Column(name = "NO_EVENTS")
    private Boolean noEvents;

    @Column(name = "CURRENT_USER_PATIENT")
    private Boolean currentUserPatient;

    @Column(name = "MAX_EVENTS_REACHED")
    private Boolean maxEventsReached;

    @Size(min = 0, max = 9)
    @Pattern(regexp = "^[0-9]*$")
    @Column(name = "PHONE_NUMBER", length = 9, nullable = true)
    private String phoneNumber;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "birthDate", nullable = true)
    private DateTime birthDate;

    @Size(min = 0, max = 100)
    @Column(name = "FACEBOOK", length = 100, nullable = true)
    private String facebook;

    @Size(min = 0, max = 200)
    @Column(name = "DISEASES", length = 200, nullable = true)
    private String diseases;

    @Size(min = 0, max = 200)
    @Column(name = "ALLERGIES", length = 200, nullable = true)
    private String allergies;

    @Size(min = 0, max = 200)
    @Column(name = "MUTUALASSURANCE", length = 200, nullable = true)
    private String mutualAssurance;

    @Size(min = 0, max = 200)
    @Column(name = "PROFESSION", length = 200, nullable = true)
    private String profession;

    @Min(value = 1)
    @Max(value = 250000000)
    @Column(name = "number", precision=10, scale=2, nullable = false)
    private BigDecimal number;

    @OneToOne
    private Plan plan;

    @Size(min = 0, max = 9)
    @Pattern(regexp = "^[0-9]*$")
    @Column(name = "PHONE_NUMBER2", length = 9, nullable = true)
    private String phoneNumber2;

    @Size(min = 0, max = 9)
    @Pattern(regexp = "^[0-9]*$")
    @Column(name = "PHONE_NUMBER3", length = 9, nullable = true)
    private String phoneNumber3;

    @Size(min = 0, max = 250)
    @Column(name = "CONSULTATION_REASON", length = 250, nullable = true)
    private String consultationReason;

    @Size(min = 0, max = 250)
    @Column(name = "remark", length = 250, nullable = true)
    private String remark;

    public String getConsultationReason() {
        return consultationReason;
    }

    public void setConsultationReason(String consultationReason) {
        this.consultationReason = consultationReason;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getPhoneNumber3() {
        return phoneNumber3;
    }

    public void setPhoneNumber3(String phoneNumber3) {
        this.phoneNumber3 = phoneNumber3;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDoctorTreating() {
        return doctorTreating;
    }

    public void setDoctorTreating(String doctorTreating) {
        this.doctorTreating = doctorTreating;
    }

    public Boolean getBleedingWhileBrushing() {
        return bleedingWhileBrushing;
    }

    public void setBleedingWhileBrushing(Boolean bleedingWhileBrushing) {
        this.bleedingWhileBrushing = bleedingWhileBrushing;
    }

    public String getReferralBy() {
        return referralBy;
    }

    public void setReferralBy(String referralBy) {
        this.referralBy = referralBy;
    }

    public Boolean getSmoking() {
        return smoking;
    }

    public void setSmoking(Boolean smoking) {
        this.smoking = smoking;
    }

    public Boolean getToothSensitivity() {
        return toothSensitivity;
    }

    public void setToothSensitivity(Boolean toothSensitivity) {
        this.toothSensitivity = toothSensitivity;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getInitial_balance() {
        return initial_balance;
    }

    public void setInitial_balance(BigDecimal initial_balance) {
        this.initial_balance = initial_balance;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMutualAssurance() {
        return mutualAssurance;
    }

    public void setMutualAssurance(String mutualAssurance) {
        this.mutualAssurance = mutualAssurance;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public DateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(DateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getCurrentUserPatient() {
        return currentUserPatient;
    }

    public void setCurrentUserPatient(Boolean currentUserPatient) {
        this.currentUserPatient = currentUserPatient;
    }

    public Boolean getMaxEventsReached() {
        return maxEventsReached;
    }

    public void setMaxEventsReached(Boolean maxEventsReached) {
        this.maxEventsReached = maxEventsReached;
    }

    public Set<Treatment> getDoctorTreatments() {
        return doctorTreatments;
    }

    public void setDoctorTreatments(Set<Treatment> doctorTreatments) {
        this.doctorTreatments = doctorTreatments;
    }

    public Boolean getNoEvents() {
        return noEvents;
    }

    public void setNoEvents(Boolean noEvents) {
        this.noEvents = noEvents;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

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

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public DateTime getResetDate() {
       return resetDate;
    }

    public void setResetDate(DateTime resetDate) {
       this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<PersistentToken> getPersistentTokens() {
        return persistentTokens;
    }

    public void setPersistentTokens(Set<PersistentToken> persistentTokens) {
        this.persistentTokens = persistentTokens;
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

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (!login.equals(user.login)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", activated='" + activated + '\'' +
                ", langKey='" + langKey + '\'' +
                ", activationKey='" + activationKey + '\'' +
                "}";
    }
}
