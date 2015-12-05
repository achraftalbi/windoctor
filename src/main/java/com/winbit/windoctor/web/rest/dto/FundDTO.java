package com.winbit.windoctor.web.rest.dto;

import com.winbit.windoctor.domain.Fund;
import com.winbit.windoctor.domain.Supply_type;

/**
 * Created by achraftalbi on 12/5/15.
 */
public class FundDTO {
    private Fund fund;
    private Supply_type supply_type;

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public Supply_type getSupply_type() {
        return supply_type;
    }

    public void setSupply_type(Supply_type supply_type) {
        this.supply_type = supply_type;
    }
}
