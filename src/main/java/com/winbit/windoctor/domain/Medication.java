package com.winbit.windoctor.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Medication.
 */
@Entity
@Table(name = "MEDICATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="medication")
public class Medication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull
    @Size(min = 1, max = 300)        
    @Column(name = "name", length = 300, nullable = false)
    private String name;

    @Size(min = 1, max = 800)        
    @Column(name = "man_description", length = 800)
    private String man_description;

    @Size(min = 1, max = 800)        
    @Column(name = "woman_description", length = 800)
    private String woman_description;

    @Size(min = 1, max = 800)        
    @Column(name = "child_description", length = 800)
    private String child_description;

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

    public String getMan_description() {
        return man_description;
    }

    public void setMan_description(String man_description) {
        this.man_description = man_description;
    }

    public String getWoman_description() {
        return woman_description;
    }

    public void setWoman_description(String woman_description) {
        this.woman_description = woman_description;
    }

    public String getChild_description() {
        return child_description;
    }

    public void setChild_description(String child_description) {
        this.child_description = child_description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Medication medication = (Medication) o;

        if ( ! Objects.equals(id, medication.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Medication{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", man_description='" + man_description + "'" +
                ", woman_description='" + woman_description + "'" +
                ", child_description='" + child_description + "'" +
                '}';
    }
}
