package com.winbit.windoctor.domain;

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
 * A Entry.
 */
@Entity
@Table(name = "ENTRY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="entry")
public class Entry implements Serializable {

    public Entry(){
    }

    public Entry(BigDecimal totalPrice, BigDecimal totalAmount){
        this.price = totalPrice;
        this.amount = totalAmount;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Min(value = 0)
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Min(value = 0)
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "creation_date", nullable = false)
    private DateTime creation_date;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "relative_date", nullable = false)
    private DateTime relative_date;

    @ManyToOne
    private Charge charge;

    @ManyToOne
    private Fund fund;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public DateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(DateTime creation_date) {
        this.creation_date = creation_date;
    }

    public DateTime getRelative_date() {
        return relative_date;
    }

    public void setRelative_date(DateTime relative_date) {
        this.relative_date = relative_date;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Entry entry = (Entry) o;

        if ( ! Objects.equals(id, entry.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", price='" + price + "'" +
                ", amount='" + amount + "'" +
                ", creation_date='" + creation_date + "'" +
                ", relative_date='" + relative_date + "'" +
                '}';
    }
}
