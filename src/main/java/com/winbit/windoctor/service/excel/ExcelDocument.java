package com.winbit.windoctor.service.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by achraftalbi on 1/11/17.
 */
public interface ExcelDocument {

    public String getDocumentName();
    public void setDocumentName(final String pDocumentName);

    public HSSFWorkbook getWorkbook();
    public void setWorkbook(final HSSFWorkbook pWorkbook);

    public HSSFSheet getWorksheet();
    public void setWorksheet(final HSSFSheet pWorksheet);
}
