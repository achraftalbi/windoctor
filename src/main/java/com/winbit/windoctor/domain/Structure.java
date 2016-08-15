package com.winbit.windoctor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Structure.
 */
@Entity
@Table(name = "STRUCTURE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="structure")
public class Structure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "structure")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> users = new HashSet<>();

    @NotNull
    @Min(value = 0)
    @Max(value = 1000)
    @Column(name = "MAX_EVENTS_PATIENT_CAN_ADD", precision=4, nullable = false)
    private BigDecimal maxEventsPatientCanAdd;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "creation_date", nullable = false)
    @JsonIgnore
    private DateTime creation_date;

    @ManyToOne
    private Type_structure type_structure;

    public Type_structure getType_structure() {
        return type_structure;
    }

    public void setType_structure(Type_structure type_structure) {
        this.type_structure = type_structure;
    }

    public DateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(DateTime creation_date) {
        this.creation_date = creation_date;
    }

    public BigDecimal getMaxEventsPatientCanAdd() {
        return maxEventsPatientCanAdd;
    }

    public void setMaxEventsPatientCanAdd(BigDecimal maxEventsPatientCanAdd) {
        this.maxEventsPatientCanAdd = maxEventsPatientCanAdd;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Structure structure = (Structure) o;

        if ( ! Objects.equals(id, structure.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Structure{" +
                "id=" + id +
                ", name='" + name + "'" +
                '}';
    }
}
