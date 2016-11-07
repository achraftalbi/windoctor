package com.winbit.windoctor.web.rest.dto;

import com.winbit.windoctor.domain.Treatment;

import java.util.List;

/**
 * Created by achraftalbi on 09/10/2016.
 */
public class TreatmentDTO {

    public TreatmentDTO(){}
    public TreatmentDTO(List<Treatment> treatmentsList,List<Treatment> treatmentsPlanList){
        this.treatmentsList = treatmentsList;
        this.treatmentsPlanList = treatmentsPlanList;
    }

    private List<Treatment> treatmentsList;
    private List<Treatment> treatmentsPlanList;

    public List<Treatment> getTreatmentsList() {
        return treatmentsList;
    }

    public void setTreatmentsList(List<Treatment> treatmentsList) {
        this.treatmentsList = treatmentsList;
    }

    public List<Treatment> getTreatmentsPlanList() {
        return treatmentsPlanList;
    }

    public void setTreatmentsPlanList(List<Treatment> treatmentsPlanList) {
        this.treatmentsPlanList = treatmentsPlanList;
    }
}
