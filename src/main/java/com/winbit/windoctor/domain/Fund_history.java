package com.winbit.windoctor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Fund_history.
 */
@Entity
@Table(name = "FUND_HISTORY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="fund_history")
public class Fund_history implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Min(value = 0)
    @Max(value = 1000000000)
    @Column(name = "old_amount", precision=10, scale=2, nullable = false)
    private BigDecimal old_amount;

    @NotNull
    @Min(value = 0)
    @Max(value = 1000000000)
    @Column(name = "new_amount", precision=10, scale=2, nullable = false)
    private BigDecimal new_amount;

    @Column(name = "type_operation")
    private Boolean type_operation;

    @NotNull
    @Min(value = -1000000000)
    @Max(value = 1000000000)
    @Column(name = "amount_movement", precision=10, scale=2, nullable = false)
    private BigDecimal amount_movement;

    @NotNull
    @ManyToOne
    private Fund fund;

    @ManyToOne
    private Treatment treatment;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Supply_type supply_type;

    @ManyToOne
    private User created_by;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "creation_date", nullable = false)
    @JsonIgnore
    private DateTime creation_date;

    public User getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    public DateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(DateTime creation_date) {
        this.creation_date = creation_date;
    }

    public Supply_type getSupply_type() {
        return supply_type;
    }

    public void setSupply_type(Supply_type supply_type) {
        this.supply_type = supply_type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getOld_amount() {
        return old_amount;
    }

    public void setOld_amount(BigDecimal old_amount) {
        this.old_amount = old_amount;
    }

    public BigDecimal getNew_amount() {
        return new_amount;
    }

    public void setNew_amount(BigDecimal new_amount) {
        this.new_amount = new_amount;
    }

    public Boolean getType_operation() {
        return type_operation;
    }

    public void setType_operation(Boolean type_operation) {
        this.type_operation = type_operation;
    }

    public BigDecimal getAmount_movement() {
        return amount_movement;
    }

    public void setAmount_movement(BigDecimal amount_movement) {
        this.amount_movement = amount_movement;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Fund_history fund_history = (Fund_history) o;

        if ( ! Objects.equals(id, fund_history.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Fund_history{" +
                "id=" + id +
                ", old_amount='" + old_amount + "'" +
                ", new_amount='" + new_amount + "'" +
                ", type_operation='" + type_operation + "'" +
                ", amount_movement='" + amount_movement + "'" +
                '}';
    }
}
