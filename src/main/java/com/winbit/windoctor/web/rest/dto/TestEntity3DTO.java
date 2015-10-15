package com.winbit.windoctor.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the TestEntity3 entity.
 */
public class TestEntity3DTO implements Serializable {

    private Long id;

    @NotNull
    private String description;
    private Set<TestEntity2DTO> testEntity2s = new HashSet<>();

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

    public Set<TestEntity2DTO> getTestEntity2s() {
        return testEntity2s;
    }

    public void setTestEntity2s(Set<TestEntity2DTO> testEntity2s) {
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

        TestEntity3DTO testEntity3DTO = (TestEntity3DTO) o;

        if ( ! Objects.equals(id, testEntity3DTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TestEntity3DTO{" +
                "id=" + id +
                ", description='" + description + "'" +
                '}';
    }
}
