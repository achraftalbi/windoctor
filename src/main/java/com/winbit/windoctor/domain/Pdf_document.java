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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PDF_DOCUMENT.
 */
@Entity
@Table(name = "PDF_DOCUMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="pdf_document")
public class Pdf_document implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 0, max = 500)
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @Size(min = 0, max = 250)
    @Column(name = "original_file_name", length = 250, nullable = false)
    private String original_file_name;

    @Size(max = 250000)
    @Lob
    @Column(name = "file_content")
    private byte[] file_content;

    @Size(min = 0, max = 250)
    @Column(name = "mime_type", length = 250, nullable = false)
    private String mime_type;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "creation_date", nullable = false)
    private DateTime creation_date;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "update_date", nullable = false)
    private DateTime update_date;

    @ManyToOne
    private Pdf_document_type pdf_document_type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(DateTime creation_date) {
        this.creation_date = creation_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFile_content() {
        return file_content;
    }

    public void setFile_content(byte[] file_content) {
        this.file_content = file_content;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public String getOriginal_file_name() {
        return original_file_name;
    }

    public void setOriginal_file_name(String original_file_name) {
        this.original_file_name = original_file_name;
    }

    public Pdf_document_type getPdf_document_type() {
        return pdf_document_type;
    }

    public void setPdf_document_type(Pdf_document_type pdf_document_type) {
        this.pdf_document_type = pdf_document_type;
    }

    public DateTime getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(DateTime update_date) {
        this.update_date = update_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pdf_document pdf_document = (Pdf_document) o;

        if ( ! Objects.equals(id, pdf_document.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "pdf_document_type{" +
                "id=" + id +
                ", description='" + description + "'" +
                ", file_content='" + file_content + "'" +
                ", original_file_name='" + original_file_name + "'" +
                ", creation_date='" + creation_date + "'" +
                ", update_date='" + update_date + "'" +
                '}';
    }
}
