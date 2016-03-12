package com.winbit.windoctor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Status.
 */
@Entity
@Table(name = "STATUS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="status")
public class Status implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "description", length = 100, nullable = false)
    private String description;


    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "description_fr", length = 100)
    private String descriptionFr;

    @OneToMany(mappedBy = "eventStatus")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> eventStatuss = new HashSet<>();

    public String getDescriptionFr() {
        return descriptionFr;
    }

    public void setDescriptionFr(String descriptionFr) {
        this.descriptionFr = descriptionFr;
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

    public Set<Event> getEventStatuss() {
        return eventStatuss;
    }

    public void setEventStatuss(Set<Event> events) {
        this.eventStatuss = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Status status = (Status) o;

        if ( ! Objects.equals(id, status.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", description='" + description + "'" +
                '}';
    }
}
