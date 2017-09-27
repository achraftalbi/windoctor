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
 * A Prescription_medication.
 */
@Entity
@Table(name = "PRESCRIPTION_MEDICATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="prescription_medication")
public class Prescription_medication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @Size(min = 1, max = 800)        
    @Column(name = "medication_name", length = 800)
    private String medication_name;

    @Size(min = 1, max = 800)        
    @Column(name = "man_description", length = 800)
    private String man_description;

    @Size(min = 1, max = 800)        
    @Column(name = "woman_description", length = 800)
    private String woman_description;

    @Size(min = 1, max = 800)        
    @Column(name = "child_description", length = 800)
    private String child_description;

    @ManyToOne
    private Prescription prescription_medication_r;

    @ManyToOne
    private Medication medication_prescription_r;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedication_name() {
        return medication_name;
    }

    public void setMedication_name(String medication_name) {
        this.medication_name = medication_name;
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

    public Prescription getPrescription_medication_r() {
        return prescription_medication_r;
    }

    public void setPrescription_medication_r(Prescription prescription) {
        this.prescription_medication_r = prescription;
    }

    public Medication getMedication_prescription_r() {
        return medication_prescription_r;
    }

    public void setMedication_prescription_r(Medication medication) {
        this.medication_prescription_r = medication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Prescription_medication prescription_medication = (Prescription_medication) o;

        if ( ! Objects.equals(id, prescription_medication.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Prescription_medication{" +
                "id=" + id +
                ", medication_name='" + medication_name + "'" +
                ", man_description='" + man_description + "'" +
                ", woman_description='" + woman_description + "'" +
                ", child_description='" + child_description + "'" +
                '}';
    }
}
