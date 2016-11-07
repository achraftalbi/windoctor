package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Event;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
public interface EventRepository extends JpaRepository<Event,Long> {

    @Query("select new Event(e) from Event e where e.user.structure.id = ?1 and e.eventStatus.id!=6 order by e.event_date asc")
    List<Event> findAllEvents(Long structure_id);

    @Query("select e from Event e where e.user.structure.id = ?1 and e.eventStatus.id in (7,10) order by e.event_date asc")
    Page<Event> getAllNotification(Long structure_id, Pageable var1);

    @Query("select e from Event e where e.event_date >= ?1 and e.event_date < ?2 and e.user.structure.id = ?3 and e.eventStatus.id!=8 and e.eventStatus.id!=6 order by e.event_date asc")
    List<Event> findAll(DateTime selectedDate, DateTime segondDate, Long structure_id);

    @Query("select e from Event e where e.user.structure.id = ?1 order by e.event_date asc")
    Page<Event> findAll(Long structure_id, Pageable var1);

    @Query("select e from Event e where (e.event_date >= ?1 and e.event_date < ?2) and e.user.structure.id = ?3 and e.eventStatus.id!=6 order by e.event_date asc")
    Page<Event> findAll(DateTime selectedDate, DateTime segondDate, Long structure_id, Pageable var1);

    @Query("select e from Event e where (e.event_date >= ?2 and e.event_date < ?3) and e.user.structure.id = ?4 and e.eventStatus.id!=6 " +
        " and ( lower(e.description) like lower(?1) or lower(e.user.firstName) like lower(?1) or lower(e.user.lastName) like lower(?1) " +
        "      or lower(e.eventStatus.description) like lower(?1) or lower(e.eventStatus.descriptionFr) like lower(?1)) order by e.event_date asc")
    Page<Event> search(String query,DateTime selectedDate, DateTime segondDate, Long structure_id, Pageable var1);

    @Query("select e from Event e where (e.event_date >= ?1 and e.event_date < ?2) and e.user.structure.id = ?3 and e.eventStatus.id in (1,7) order by e.event_date asc")
    List<Event> findEventsToBlock(DateTime firstDate, DateTime segondDate, Long structure_id);

    @Query("select e from Event e where (e.event_date >= ?1 and e.event_date < ?2) and e.eventStatus.id in (1) order by e.event_date asc")
    List<Event> findAppointmentsToRecall(DateTime firstDate, DateTime segondDate);

    @Query("select e from Event e where e.eventStatus.id = ?1 and e.user.structure.id = ?2 order by e.event_date desc")
    Page<Event> findAllEventsBlock(Long statusType, Long structure_id, Pageable var1);

    @Query("select e from Event e where e.user.id = ?1 and e.eventStatus.id = 7")
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
    /*@Query("select e from Event e, MailSetting ms where e.user.structure = ms.structure and ms.mail_type.id = ?1 " +
        "and ms.activated = true " +
        "and e.creationMailSent = false " +
        "and e.eventStatus.id = ?4 "+
        "and e.creation_date between ?2 and ?3")
    List<Event> getAllNewelyCreatedEvents(Long emailCode, DateTime start, DateTime end, Long es);*/

    /**
     * Get all canceled event on all structure that accept emailing after event cancelation
     *
     * @param emailCode - email code
     * @param es - event status code
     * @return event list
     */
    /*@Query("select e from Event e, MailSetting ms where e.user.structure = ms.structure and ms.mail_type.id = ?1 " +
        "and ms.activated = true " +
        "and e.canceledMailSent = false " +
        "and e.eventStatus.id = ?2")
    List<Event> getAllCanceledEvents(Long emailCode, Long es);*/

    /**
     * Get all event for before reminding
     *
     * @param emailCode - email code
     * @param es - event status code
     * @param date - reminding date
     * @return event list
     */
    /*@Query("select e from Event e, MailSetting ms where e.user.structure = ms.structure and ms.mail_type.id = ?1 " +
        "and ms.activated = true " +
        "and e.remindBeforeMail = false " +
        "and e.eventStatus.id = ?2 " +
        "and e.event_date < ?3")
    List<Event> getAllEventBeforeReminding(Long emailCode, Long es, DateTime date);*/

    /**
     * Get all event for after reminding
     *
     * @param emailCode - email code
     * @param es - event status code
     * @param date - reminding date
     * @return event list
     */
    /*@Query("select e from Event e, MailSetting ms where e.user.structure = ms.structure and ms.mail_type.id = ?1 " +
        "and ms.activated = true " +
        "and e.remindAfterMail = false " +
        "and e.eventStatus.id = ?2 " +
        "and e.event_date > ?3")
    List<Event> getAllEventAfterReminding(Long emailCode, Long es, DateTime date);*/

}
