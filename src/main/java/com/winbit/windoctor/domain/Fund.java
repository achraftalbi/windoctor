package com.winbit.windoctor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Fund.
 */
@Entity
@Table(name = "FUND")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="fund")
public class Fund implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @NotNull
    @Min(value = 0)
    @Max(value = 250000000)
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @OneToMany(mappedBy = "fund")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Fund_history> fund_historys = new HashSet<>();

    @ManyToOne
    private Structure structure;

    public Set<Fund_history> getFund_historys() {
        return fund_historys;
    }

    public void setFund_historys(Set<Fund_history> fund_historys) {
        this.fund_historys = fund_historys;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Fund fund = (Fund) o;

        if ( ! Objects.equals(id, fund.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Fund{" +
                "id=" + id +
                ", description='" + description + "'" +
                ", amount='" + amount + "'" +
                '}';
    }
}
