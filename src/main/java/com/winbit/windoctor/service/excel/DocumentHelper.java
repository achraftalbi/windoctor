package com.winbit.windoctor.service.excel;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by achraftalbi on 1/11/17.
 */
public abstract class DocumentHelper implements ExcelDocument {
    private String documentName;
    private HSSFWorkbook workbook;
    private HSSFSheet worksheet;

    public static final Logger logger = Logger.getLogger(DocumentHelper.class);

    @Override
    public String getDocumentName()
    {
        return documentName;
    }

    @Override
    public void setDocumentName(String pDocumentName)
    {
        if(pDocumentName != null && !pDocumentName.isEmpty())
            this.documentName = pDocumentName;
        else
            DocumentHelper.logger.error("An attempt was made to create a document with an empty name.");
    }

    @Override
    public HSSFWorkbook getWorkbook()
    {
        return workbook;
    }

    @Override
    public void setWorkbook(HSSFWorkbook workbook)
    {
        this.workbook = workbook;
    }

    @Override
    public HSSFSheet getWorksheet()
    {
        return worksheet;
    }

    @Override
    public void setWorksheet(HSSFSheet worksheet)
    {
        this.worksheet = worksheet;
    }


}
