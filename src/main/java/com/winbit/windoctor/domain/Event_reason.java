package com.winbit.windoctor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Event_reason.
 */
@Entity
@Table(name = "EVENT_REASON")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="event_reason")
public class Event_reason implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @OneToMany(mappedBy = "eventReason")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> eventReasons = new HashSet<>();

    @OneToMany(mappedBy = "eventReason")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Treatment> treatments = new HashSet<>();

    @NotNull

    @Min(value = 0)
    @Max(value = 10000000)
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    /*@ManyToOne
    private Structure structure;

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }*/

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Event> getEventReasons() {
        return eventReasons;
    }

    public void setEventReasons(Set<Event> events) {
        this.eventReasons = events;
    }

    public Set<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(Set<Treatment> treatments) {
        this.treatments = treatments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event_reason event_reason = (Event_reason) o;

        if ( ! Objects.equals(id, event_reason.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event_reason{" +
                "id=" + id +
                ", description='" + description + "'" +
                '}';
    }
}
