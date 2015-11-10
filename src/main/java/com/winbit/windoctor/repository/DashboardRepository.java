package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Dashboard;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dashboard entity.
 */
public interface DashboardRepository extends JpaRepository<Dashboard,Long> {

}
