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
 * A TestEntity3.
 */
@Entity
@Table(name = "TESTENTITY3")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="testentity3")
public class TestEntity3 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull        
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "TESTENTITY3_TESTENTITY2",
               joinColumns = @JoinColumn(name="testentity3s_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="testentity2s_id", referencedColumnName="ID"))
    private Set<TestEntity2> testEntity2s = new HashSet<>();

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

    public Set<TestEntity2> getTestEntity2s() {
        return testEntity2s;
    }

    public void setTestEntity2s(Set<TestEntity2> testEntity2s) {
        this.testEntity2s = testEntity2s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestEntity3 testEntity3 = (TestEntity3) o;

        if ( ! Objects.equals(id, testEntity3.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TestEntity3{" +
                "id=" + id +
                ", description='" + description + "'" +
                '}';
    }
}
