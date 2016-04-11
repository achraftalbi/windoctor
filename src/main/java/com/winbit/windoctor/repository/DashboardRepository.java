package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Dashboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dashboard entity.
 */
public interface DashboardRepository extends JpaRepository<Dashboard,Long> {
    @Query(value = "select 1 id,'January' description_en,'Janvier'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 0 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 1 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 2 id, 'February'description_en,'Février'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 1 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 2 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 3 id, 'March'description_en,'Mars'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 2 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 3 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 4 id, 'April'description_en,'Avril'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 3 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 4 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 5 id, 'May'description_en,'Mai'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 4 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 5 MONTH),INTERVAL -1 day)\n" +
        "union\t\n" +
        "select 6 id, 'June'description_en,'Juin'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 5 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 6 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 7 id, 'July'description_en,'Juillet'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 6 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 7 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 8 id, 'August'description_en,'Août'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 7 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 8 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 9 id, 'September'description_en,'Septembre'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 8 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 9 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 10 id, 'October'description_en,'Octobre'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 9 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 10 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 11 id, 'November'description_en,'Novembre'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 10 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 11 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 12 id, 'December'description_en,'Décembre'description_fr,sum(paid_price)value from TREATMENT,EVENT,JHI_USER where TREATMENT.event_id=EVENT.id and EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and treatment_date >=date_add(makedate(?1,1),INTERVAL 11 MONTH) and treatment_date <=date_add(date_add(makedate(?1,1),INTERVAL 12 MONTH),INTERVAL -1 day)",nativeQuery = true)
    List<Dashboard> findBudgetByYear(Long year, Long structureId);
    @Query(value = "select 1 id,'January' description_en,'Janvier'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 0 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 1 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 2 id, 'February'description_en,'Février'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 1 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 2 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 3 id, 'March'description_en,'Mars'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 2 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 3 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 4 id, 'April'description_en,'Avril'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 3 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 4 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 5 id, 'May'description_en,'Mai'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 4 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 5 MONTH),INTERVAL -1 day)\n" +
        "union\t\n" +
        "select 6 id, 'June'description_en,'Juin'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 5 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 6 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 7 id, 'July'description_en,'Juillet'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 6 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 7 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 8 id, 'August'description_en,'Août'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 7 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 8 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 9 id, 'September'description_en,'Septembre'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 8 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 9 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 10 id, 'October'description_en,'Octobre'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 9 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 10 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 11 id, 'November'description_en,'Novembre'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 10 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 11 MONTH),INTERVAL -1 day)\n" +
        "union\n" +
        "select 12 id, 'December'description_en,'Décembre'description_fr,count(distinct JHI_USER.id)value from JHI_USER,EVENT where EVENT.user_id=JHI_USER.id and JHI_USER.structure_id = ?2 and  event_date >=date_add(makedate(?1,1),INTERVAL 11 MONTH) and event_date <=date_add(date_add(makedate(?1,1),INTERVAL 12 MONTH),INTERVAL -1 day)",nativeQuery = true)
    List<Dashboard> findPatientsByYearMonths(Long year, Long structureId);
}
