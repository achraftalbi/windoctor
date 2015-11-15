package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.MailSetting;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MailSetting entity.
 */
public interface MailSettingRepository extends JpaRepository<MailSetting,Long> {

}
