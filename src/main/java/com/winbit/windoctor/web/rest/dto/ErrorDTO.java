package com.winbit.windoctor.web.rest.dto;

/**
 * Error DTO
 *
 * @author MBoufnichel
 */
public class ErrorDTO {
    private String code;

    public ErrorDTO(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
