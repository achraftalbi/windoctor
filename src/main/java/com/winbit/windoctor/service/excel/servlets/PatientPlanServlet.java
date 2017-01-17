package com.winbit.windoctor.service.excel.servlets;

import com.winbit.windoctor.service.excel.GenericDocumentGenrator;
import com.winbit.windoctor.service.excel.GenericDocumentGenrator.CellStructure;
import com.winbit.windoctor.service.excel.GenericDocumentGenrator.RowStructure;
import com.winbit.windoctor.service.excel.GenericDocumentGenrator.Structure;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;

import javax.print.Doc;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Iterator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
/**
 * Created by achraftalbi on 1/11/17.
 */
/*@WebServlet(name = "PatientPlanServlet", urlPatterns
    = {
    "/windocdor/excel/servlet/PatientPlanServlet"
})*/
public class PatientPlanServlet extends HttpServlet {

    public static final Logger logger = Logger.getLogger(PatientPlanServlet.class);
    public static final int numberColumns = 8;
    private Locale locale;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        //response.setContentType("application/vnd.ms-excel");
        GenericDocumentGenrator doc = new GenericDocumentGenrator("patientPlan");
        List<RowStructure> rows = new ArrayList<RowStructure>();
        Structure structure = new Structure(rows, 0l);
        doc.setStructure(structure);
        constructTheHeader(rows, structure, request, doc);
        constructTheBody(rows, structure, request, doc);
        doc.writeStructure();
        doc.setDocumentName(doc.getDocumentName().replace(':', '-'));
        doc.setDocumentName(doc.getDocumentName().replace('/', '-'));
        doc.setDocumentName(doc.getDocumentName().replace('\\', '-'));
        //response.setHeader("Content-Disposition", "attachment; filename=\"" + doc.getDocumentName() + ".pdf");
        //doc.getWorkbook().write(response.getOutputStream());
        //doc.getWorkbook().close();
        HSSFSheet my_worksheet = doc.getWorkbook().getSheetAt(0);
        // To iterate over the rows
        Iterator<Row> rowIterator = my_worksheet.iterator();
        //We will create output PDF document objects at this point
        Document iText_xls_2_pdf = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(iText_xls_2_pdf, baos);
        }catch (DocumentException ex){
            logger.error("exception occurred when creating pdf file - "+ex.getMessage());
        }
        iText_xls_2_pdf.open();
        //we have two columns in the Excel sheet, so we create a PDF table with two columns
        //Note: There are ways to make this dynamic in nature, if you want to.
        PdfPTable my_table = new PdfPTable(2);
        //We will use the object below to dynamically add new data to the table
        PdfPCell table_cell;
        //Loop through rows.
        while(rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while(cellIterator.hasNext()) {
                Cell cell = cellIterator.next(); //Fetch CELL
                switch(cell.getCellType()) { //Identify CELL type
                    //you need to add more code here based on
                    //your requirement / transformations
                    case Cell.CELL_TYPE_STRING:
                        //Push the data from Excel to PDF Cell
                        table_cell=new PdfPCell(new Phrase(cell.getStringCellValue()));
                        //feel free to move the code below to suit to your needs
                        my_table.addCell(table_cell);
                        break;
                }
                //next line
            }

        }
        //Finally add the table to PDF document
        try {
            iText_xls_2_pdf.add(my_table);
        }catch (DocumentException ex){
            logger.error("exception occurred when adding excel table in pdf file - "+ex.getMessage());
        }
        iText_xls_2_pdf.close();
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control",
            "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        // setting the content type
        response.setContentType("application/pdf");
        // the contentlength
        response.setContentLength(baos.size());
        // write ByteArrayOutputStream to the ServletOutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();
    }

    void constructTheHeader(List<RowStructure> rows, Structure structure, HttpServletRequest request, GenericDocumentGenrator doc) {
        List<CellStructure> columns = new ArrayList<CellStructure>();
        columns.add(new CellStructure("Test1",
            doc.getCellStyle(false, null, CellStyle.VERTICAL_CENTER,null), false, false, null, null, 0l, 8300l));
        columns.add(new CellStructure("Test2",
            doc.getComplexCellStyle(false, null, CellStyle.VERTICAL_CENTER, true,null), false, true, 1l, 5l, 1l, null));
        rows.add(new RowStructure(columns, structure.getCurrentRow(), 600l));
        columns = new ArrayList<CellStructure>();
        columns.add(new CellStructure("Test3",
            doc.getCellStyle(false, null, CellStyle.VERTICAL_CENTER,null), false, false, null, null, 0l, 8300l));
        columns.add(new CellStructure("Desc1",
            doc.getCellStyle(false, null, CellStyle.VERTICAL_CENTER,null), false, true, 1l, 5l, 1l, null));
        rows.add(new RowStructure(columns, structure.getCurrentRow(), 500l));
        columns = new ArrayList<CellStructure>();
        columns.add(new CellStructure("Test4",
                doc.getCellStyle(false, null, CellStyle.VERTICAL_CENTER,null), false, false, null, null, 0l, 8300l));
        columns.add(new CellStructure("DateVALUE",
                doc.getCellStyle(false, null, CellStyle.VERTICAL_CENTER,null), false, true, 1l, 5l, 1l, null));
        rows.add(new RowStructure(columns, structure.getCurrentRow(), 500l));

    }

    void constructTheBody(List<RowStructure> rows, Structure structure, HttpServletRequest request, GenericDocumentGenrator doc) {
        List<CellStructure> columns = new ArrayList<CellStructure>();
        columns.add(new CellStructure("Generated date value" + " " + "2017-09-15",
            doc.getCellStyle(false, null, CellStyle.VERTICAL_CENTER,null), false, true, 0l, 5l, 0l, 8300l));
        rows.add(new RowStructure(columns, structure.getCurrentRow(), 500l));
        logger.info("End       ########################################################################################################################################################################################");
    }

}
