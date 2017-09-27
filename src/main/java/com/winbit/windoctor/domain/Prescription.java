package com.winbit.windoctor.domain;

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
import java.util.Objects;


/**
 * A Prescription.
 */
@Entity
@Table(name = "PRESCRIPTION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="prescription")
public class Prescription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull
    @Size(min = 1, max = 300)        
    @Column(name = "description", length = 300, nullable = false)
    private String description;
    
    @Column(name = "archived")
    private Boolean archived;
    
    @Column(name = "medication_persist")
    private Boolean medication_persist;

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "creation_date", nullable = false)
    private DateTime creation_date;

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "update_date", nullable = false)
    private DateTime update_date;

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

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Boolean getMedication_persist() {
        return medication_persist;
    }

    public void setMedication_persist(Boolean medication_persist) {
        this.medication_persist = medication_persist;
    }

    public DateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(DateTime creation_date) {
        this.creation_date = creation_date;
    }

    public DateTime getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(DateTime update_date) {
        this.update_date = update_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Prescription prescription = (Prescription) o;

        if ( ! Objects.equals(id, prescription.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", description='" + description + "'" +
                ", archived='" + archived + "'" +
                ", medication_persist='" + medication_persist + "'" +
                ", creation_date='" + creation_date + "'" +
                ", update_date='" + update_date + "'" +
                '}';
    }
}
