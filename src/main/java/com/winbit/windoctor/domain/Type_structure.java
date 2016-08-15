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
 * A Type_structure.
 */
@Entity
@Table(name = "TYPE_STRUCTURE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="type_structure")
public class Type_structure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull
    @Size(min = 1, max = 200)        
    @Column(name = "description_en", length = 200, nullable = false)
    private String description_en;

    @NotNull
    @Size(min = 1, max = 200)        
    @Column(name = "description_fr", length = 200, nullable = false)
    private String description_fr;

    @OneToMany(mappedBy = "type_structure")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Structure> structures = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getDescription_fr() {
        return description_fr;
    }

    public void setDescription_fr(String description_fr) {
        this.description_fr = description_fr;
    }

    public Set<Structure> getStructures() {
        return structures;
    }

    public void setStructures(Set<Structure> structures) {
        this.structures = structures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Type_structure type_structure = (Type_structure) o;

        if ( ! Objects.equals(id, type_structure.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Type_structure{" +
                "id=" + id +
                ", description_en='" + description_en + "'" +
                ", description_fr='" + description_fr + "'" +
                '}';
    }
}
