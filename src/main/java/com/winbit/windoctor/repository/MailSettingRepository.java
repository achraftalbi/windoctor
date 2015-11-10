package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.MailSetting;

import com.winbit.windoctor.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MailSetting entity.
 */
public interface MailSettingRepository extends JpaRepository<MailSetting,Long> {

    @Query("select m from MailSetting m where m.structure.id= ?1")
    List<MailSetting> findAll(Long structureId);


}
