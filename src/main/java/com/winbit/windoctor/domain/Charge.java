package com.winbit.windoctor.domain;

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
 * A Charge.
 */
@Entity
@Table(name = "CHARGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="charge")
public class Charge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull
    @Size(min = 1, max = 100)        
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(max = 250000)        
    @Lob
    @Column(name = "image")
    private byte[] image;

    @NotNull
    @Min(value = 0)        
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Min(value = 0)        
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @ManyToOne
    private Structure structure;

    @ManyToOne
    private CategoryCharge categoryCharge;

    @ManyToOne
    private Fund fund;

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public CategoryCharge getCategoryCharge() {
        return categoryCharge;
    }

    public void setCategoryCharge(CategoryCharge categoryCharge) {
        this.categoryCharge = categoryCharge;
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

        Charge charge = (Charge) o;

        if ( ! Objects.equals(id, charge.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Charge{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", image='" + image + "'" +
                ", price='" + price + "'" +
                ", amount='" + amount + "'" +
                '}';
    }
}
