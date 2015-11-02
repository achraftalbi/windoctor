package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Attachment entity.
 */
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
    @Query("select t from Attachment t where (t.treatment).id = ?1 ")
    Page<Attachment> findByTreatment(Long treatmentId, Pageable var1);
}
