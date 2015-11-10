package com.winbit.windoctor.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MailSetting.
 */
@Entity
@Table(name = "mail_setting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "mailsetting")
public class MailSetting implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "mail_on_event_creation")
    private Boolean mailOnEventCreation;

    @Column(name = "mail_on_event_cancelation")
    private Boolean mailOnEventCancelation;

    @Column(name = "mail_on_event_edition")
    private Boolean mailOnEventEdition;

    @Column(name = "remaiding_before_event_mail")
    private Boolean remaidingBeforeEventMail;

    @Column(name = "remaiding_after_event_mail")
    private Boolean remaidingAfterEventMail;

    @Column(name = "patient_creation_account_mail")
    private Boolean patientCreationAccountMail;

    @OneToOne
    @NotNull
    private Structure structure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getMailOnEventCreation() {
        return mailOnEventCreation;
    }

    public void setMailOnEventCreation(Boolean mailOnEventCreation) {
        this.mailOnEventCreation = mailOnEventCreation;
    }

    public Boolean getMailOnEventCancelation() {
        return mailOnEventCancelation;
    }

    public void setMailOnEventCancelation(Boolean mailOnEventCancelation) {
        this.mailOnEventCancelation = mailOnEventCancelation;
    }

    public Boolean getMailOnEventEdition() {
        return mailOnEventEdition;
    }

    public void setMailOnEventEdition(Boolean mailOnEventEdition) {
        this.mailOnEventEdition = mailOnEventEdition;
    }

    public Boolean getRemaidingBeforeEventMail() {
        return remaidingBeforeEventMail;
    }

    public void setRemaidingBeforeEventMail(Boolean remaidingBeforeEventMail) {
        this.remaidingBeforeEventMail = remaidingBeforeEventMail;
    }

    public Boolean getRemaidingAfterEventMail() {
        return remaidingAfterEventMail;
    }

    public void setRemaidingAfterEventMail(Boolean remaidingAfterEventMail) {
        this.remaidingAfterEventMail = remaidingAfterEventMail;
    }

    public Boolean getPatientCreationAccountMail() {
        return patientCreationAccountMail;
    }

    public void setPatientCreationAccountMail(Boolean patientCreationAccountMail) {
        this.patientCreationAccountMail = patientCreationAccountMail;
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

        MailSetting mailSetting = (MailSetting) o;

        if ( ! Objects.equals(id, mailSetting.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MailSetting{" +
            "id=" + id +
            ", mailOnEventCreation='" + mailOnEventCreation + "'" +
            ", mailOnEventCancelation='" + mailOnEventCancelation + "'" +
            ", mailOnEventEdition='" + mailOnEventEdition + "'" +
            ", remaidingBeforeEventMail='" + remaidingBeforeEventMail + "'" +
            ", remaidingAfterEventMail='" + remaidingAfterEventMail + "'" +
            ", patientCreationAccountMail='" + patientCreationAccountMail + "'" +
            '}';
    }
}
