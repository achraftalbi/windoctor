package com.winbit.windoctor.web.rest.dto;

import javax.validation.constraints.NotNull;

/**
 * Doctor DTO
 *
 * @author MBoufnichel
 */
public class DoctorDTO extends UserDTO {

    @NotNull
    private StructureDTO structure;

    public StructureDTO getStructure() {
        return structure;
    }

    public void setStructure(StructureDTO structure) {
        this.structure = structure;
    }
}
