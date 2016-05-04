package com.winbit.windoctor.domain;

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
 * A CategoryCharge.
 */
@Entity
@Table(name = "CATEGORYCHARGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="categorycharge")
public class CategoryCharge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull
    @Size(min = 1, max = 100)        
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToOne
    private Structure structure;

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

        CategoryCharge categoryCharge = (CategoryCharge) o;

        if ( ! Objects.equals(id, categoryCharge.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CategoryCharge{" +
                "id=" + id +
                ", name='" + name + "'" +
                '}';
    }
}
