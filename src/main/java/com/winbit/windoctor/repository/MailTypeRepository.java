package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.MailType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MailType entity.
 */
public interface MailTypeRepository extends JpaRepository<MailType,Long> {

}
