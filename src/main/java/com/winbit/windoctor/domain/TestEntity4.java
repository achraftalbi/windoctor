package com.winbit.windoctor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A TestEntity4.
 */
@Entity
@Table(name = "TESTENTITY4")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="testentity4")
public class TestEntity4 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "testEntity4")
    @JsonIgnore
    private TestEntity5 testEntity5;

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

    public TestEntity5 getTestEntity5() {
        return testEntity5;
    }

    public void setTestEntity5(TestEntity5 testEntity5) {
        this.testEntity5 = testEntity5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestEntity4 testEntity4 = (TestEntity4) o;

        if ( ! Objects.equals(id, testEntity4.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TestEntity4{" +
                "id=" + id +
                ", description='" + description + "'" +
                '}';
    }
}
