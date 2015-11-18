package com.winbit.windoctor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.winbit.windoctor.domain.util.CustomDateTimeDeserializer;
import com.winbit.windoctor.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Event.
 */
@Entity
@Table(name = "EVENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="event")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "event_date", nullable = false)
    private DateTime event_date;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "creation_date", nullable = false)
    @JsonIgnore
    private DateTime creation_date;

    @Column(name = "creation_mail_sent")
    @JsonIgnore
    private Boolean creationMailSent;



    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne
    private Status eventStatus;

    @ManyToOne
    private Event_reason eventReason;

    @Column(name = "THIS_EVENT_IS_APPOINTMENT")
    private Boolean thisEventISAppointment;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Treatment> treatments = new HashSet<>();



    public Set<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(Set<Treatment> treatments) {
        this.treatments = treatments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getEvent_date() {
        return event_date;
    }

    public void setEvent_date(DateTime event_date) {
        this.event_date = event_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Status status) {
        this.eventStatus = status;
    }

    public Event_reason getEventReason() {
        return eventReason;
    }

    public void setEventReason(Event_reason event_reason) {
        this.eventReason = event_reason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event event = (Event) o;

        if ( ! Objects.equals(id, event.id)) return false;

        return true;
    }

    public Boolean getThisEventISAppointment() {
        return thisEventISAppointment;
    }

    public void setThisEventISAppointment(Boolean thisEventISAppointment) {
        this.thisEventISAppointment = thisEventISAppointment;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", event_date='" + event_date + "'" +
                ", description='" + description + "'" +
                ", thisEventISAppointment='" + thisEventISAppointment + "'" +
                '}';
    }
}
