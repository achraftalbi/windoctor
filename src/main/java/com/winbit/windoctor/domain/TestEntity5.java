package com.winbit.windoctor.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A TestEntity5.
 */
@Entity
@Table(name = "TESTENTITY5")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="testentity5")
public class TestEntity5 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "description5")
    private String description5;

    @OneToOne
    private TestEntity4 testEntity4;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription5() {
        return description5;
    }

    public void setDescription5(String description5) {
        this.description5 = description5;
    }

    public TestEntity4 getTestEntity4() {
        return testEntity4;
    }

    public void setTestEntity4(TestEntity4 testEntity4) {
        this.testEntity4 = testEntity4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestEntity5 testEntity5 = (TestEntity5) o;

        if ( ! Objects.equals(id, testEntity5.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TestEntity5{" +
                "id=" + id +
                ", description5='" + description5 + "'" +
                '}';
    }
}
