package com.winbit.windoctor.web.rest.dto;

import com.winbit.windoctor.domain.Dashboard;
import com.winbit.windoctor.domain.Fund;
import com.winbit.windoctor.domain.Supply_type;

import java.util.List;

/**
 * Created by achraftalbi on 12/5/15.
 */
public class DashboardDTO {

    private List<Dashboard> dashboardList;

    private List<Integer> years;

    public List<Dashboard> getDashboardList() {
        return dashboardList;
    }

    public void setDashboardList(List<Dashboard> dashboardList) {
        this.dashboardList = dashboardList;
    }

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

}
