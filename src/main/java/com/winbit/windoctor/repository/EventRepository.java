package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Event;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.Date;
import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
public interface EventRepository extends JpaRepository<Event,Long> {
    @Query("select e from Event e where e.event_date between ?1 and ?2 and e.user.structure.id = ?3")
    Page<Event> findAll(DateTime selectedDate, DateTime segondDate, Long structure_id, Pageable var1);
    @Query("select e from Event e where e.user.id = ?1")
    List<Event> findByPatient(Long patientId);

    /**
     * Get all created and validated event on all structure that accept emailing after event validation
     * between {@code start} and  {@code end}
     *
     * @param emailCode - email code
     * @param start - start date
     * @param end - end date
     * @param es - event status code
     * @return event list
     */
    @Query("select e from Event e, MailSetting ms where e.user.structure = ms.structure and ms.mail_type.id = ?1 " +
        "and ms.activated = true " +
        "and e.creationMailSent = false " +
        "and e.eventStatus.id = ?4 "+
        "and e.creation_date between ?2 and ?3")
    List<Event> getAllNewelyCreatedEvents(Long emailCode, DateTime start, DateTime end, Long es);

    /**
     * Get all canceled event on all structure that accept emailing after event cancelation
     *
     * @param emailCode - email code
     * @param es - event status code
     * @return event list
     */
    @Query("select e from Event e, MailSetting ms where e.user.structure = ms.structure and ms.mail_type.id = ?1 " +
        "and ms.activated = true " +
        "and e.canceledMailSent = false " +
        "and e.eventStatus.id = ?2")
    List<Event> getAllCanceledEvents(Long emailCode, Long es);

    /**
     * Get all event for before reminding
     *
     * @param emailCode - email code
     * @param es - event status code
     * @param date - reminding date
     * @return event list
     */
    @Query("select e from Event e, MailSetting ms where e.user.structure = ms.structure and ms.mail_type.id = ?1 " +
        "and ms.activated = true " +
        "and e.remindBeforeMail = false " +
        "and e.eventStatus.id = ?2 " +
        "and e.event_date < ?3")
    List<Event> getAllEventBeforeReminding(Long emailCode, Long es, DateTime date);

    /**
     * Get all event for after reminding
     *
     * @param emailCode - email code
     * @param es - event status code
     * @param date - reminding date
     * @return event list
     */
    @Query("select e from Event e, MailSetting ms where e.user.structure = ms.structure and ms.mail_type.id = ?1 " +
        "and ms.activated = true " +
        "and e.remindAfterMail = false " +
        "and e.eventStatus.id = ?2 " +
        "and e.event_date > ?3")
    List<Event> getAllEventAfterReminding(Long emailCode, Long es, DateTime date);

}
