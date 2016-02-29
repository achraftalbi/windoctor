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
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Treatment.
 */
@Entity
@Table(name = "TREATMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="treatment")
public class Treatment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Treatment(){
    }

    public Treatment(BigDecimal price, BigDecimal paid_price){
        this.price = price;
        this.paid_price = paid_price;
    }
    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "treatment_date", nullable = false)
    private DateTime treatment_date;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull
    @Min(value = 0)
    @Max(value = 10000000)
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Min(value = 0)
    @Max(value = 10000000)
    @Column(name = "paid_price", precision=10, scale=2, nullable = false)
    private BigDecimal paid_price;

    @OneToMany(mappedBy = "treatment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Attachment> attachments = new HashSet<>();

    @ManyToOne
    private Event_reason eventReason;

    @ManyToOne
    private User doctor;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Fund fund;

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getTreatment_date() {
        return treatment_date;
    }

    public void setTreatment_date(DateTime treatment_date) {
        this.treatment_date = treatment_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPaid_price() {
        return paid_price;
    }

    public void setPaid_price(BigDecimal paid_price) {
        this.paid_price = paid_price;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Event_reason getEventReason() {
        return eventReason;
    }

    public void setEventReason(Event_reason event_reason) {
        this.eventReason = event_reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Treatment treatment = (Treatment) o;

        if ( ! Objects.equals(id, treatment.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "id=" + id +
                ", treatment_date='" + treatment_date + "'" +
                ", description='" + description + "'" +
                ", price='" + price + "'" +
                ", paid_price='" + paid_price + "'" +
                '}';
    }
}
