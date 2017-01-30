package com.winbit.windoctor.repository;

import com.itextpdf.text.pdf.PdfDocument;
import com.winbit.windoctor.domain.Pdf_document_type;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Product entity.
 */
public interface Pdf_document_typeRepository extends JpaRepository<Pdf_document_type,Long> {

}
