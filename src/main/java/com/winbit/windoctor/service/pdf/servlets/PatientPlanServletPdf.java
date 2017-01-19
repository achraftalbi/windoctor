package com.winbit.windoctor.service.pdf.servlets;

import com.itextpdf.text.*;
import com.itextpdf.text.List;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.web.rest.util.FunctionsUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.context.MessageSource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by achraftalbi on 1/11/17.
 */
/*@WebServlet(name = "PatientPlanServlet", urlPatterns
    = {
    "/servlet/PatientPlanServletPdf"
})*/
public class PatientPlanServletPdf extends HttpServlet {

    public static final Logger logger = Logger.getLogger(PatientPlanServletPdf.class);
    public static final int numberColumns = 8;
    public static Font boldFontCourier = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);
    public static Font bigBoldFontCourier = new Font(Font.FontFamily.COURIER, 14, Font.BOLD);
    public static Font mediumBoldFontCourier = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);
    public static Font mediumFontCourier = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);
    public static Font normaleFontCourier = new Font(Font.FontFamily.COURIER, 7, Font.NORMAL);
    private Locale locale = Locale.forLanguageTag("fr");

    public void init(ServletConfig config) {
        try {
            super.init(config);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
            config.getServletContext());
    }

    @Inject
    private MessageSource messageSource;
    @Inject
    private UserRepository userRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        // Listing 2. Creation of PdfWriter object
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.open();
        Long patientId = Long.valueOf(request.getParameter("patientId"));

        //Object[] subjectVariables = {""+patientId};
        logger.info("patient id value " + patientId);
        logger.info("userRepository " + userRepository);
        Optional<User> optional = userRepository.findOneById(patientId);
        logger.info("optional "+optional);
        User patient = optional.get();
        if(patient!=null
            && SecurityUtils.getCurrerntStructure().equals(patient.getStructure().getId())){
        }
        Object[] subjectVariables = {patient.getStructure().getName()};


        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        headerTable.addCell(getCell(messageSource.getMessage("structure.name", subjectVariables, locale),
            PdfPCell.ALIGN_LEFT,Element.ALIGN_MIDDLE, boldFontCourier,null,true));
        subjectVariables[0]=(Object)patient.getFirstName()+" "+patient.getLastName();
        headerTable.addCell(getCell(messageSource.getMessage("plan.estimate.name", subjectVariables, locale),
            PdfPCell.ALIGN_CENTER,Element.ALIGN_MIDDLE, bigBoldFontCourier,null,true));
        java.util.List<CellStructure> cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("entreprise.name", null, locale),
            boldFontCourier, false, true));
        cellStructureList.add(new CellStructure(messageSource.getMessage("phone.name", null, locale),
            normaleFontCourier, false, true));
        cellStructureList.add(new CellStructure(messageSource.getMessage("entreprise.phone.name", null, locale),
            normaleFontCourier, false, true));
        cellStructureList.add(new CellStructure(messageSource.getMessage("entreprise.mobile.phone.name", null, locale),
            normaleFontCourier, false, true));
        cellStructureList.add(new CellStructure("",
            normaleFontCourier, false, true));
        PdfPCell pdfPCell = getCell(cellStructureList,
            PdfPCell.ALIGN_RIGHT,PdfPCell.ALIGN_CENTER,null,null,true);
        headerTable.addCell(pdfPCell);
        headerTable.setSpacingAfter(70);


        PdfPTable headerTable2 = new PdfPTable(5);
        try {
            headerTable2.setWidths(new float[] {25f, 3f, 25f, 15f,30f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        headerTable2.setWidthPercentage(100);
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("estimation.date", null, locale),
            mediumFontCourier,false,true));
        cellStructureList.add(new CellStructure(messageSource.getMessage("plan.number", null, locale),
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure(messageSource.getMessage("patient.number", null, locale),
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure("",
            normaleFontCourier, false, true));
        headerTable2.addCell(getCell(cellStructureList,
            PdfPCell.ALIGN_RIGHT,PdfPCell.ALIGN_CENTER, null, new BaseColor(243, 243, 243),true));
        headerTable2.addCell(getCell("",PdfPCell.ALIGN_LEFT,Element.ALIGN_MIDDLE, boldFontCourier,new BaseColor(243, 243, 243),true));
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(FunctionsUtil.convertDateToString(new Date(), Constants.GLOBAL_DATE_FORMAT),
            mediumFontCourier,false,true));
        cellStructureList.add(new CellStructure("3",
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure(patient.getNumber() + "",
            mediumFontCourier, false, true));
        headerTable2.addCell(getCell(cellStructureList,
            PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_CENTER, null, new BaseColor(243, 243, 243),true));
        headerTable2.addCell(getCell("",PdfPCell.ALIGN_LEFT,Element.ALIGN_MIDDLE, boldFontCourier,BaseColor.WHITE,true));
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("patient.name", null, locale),
            mediumBoldFontCourier,false,true));
        cellStructureList.add(new CellStructure(patient.getFirstName()+" "+patient.getLastName(),
            mediumFontCourier, false, true));
        if(patient.getPhoneNumber()!=null){
            cellStructureList.add(new CellStructure(messageSource.getMessage("indicative.name", null, locale)+patient.getPhoneNumber(),
                mediumFontCourier, false, true));
        }
        headerTable2.addCell(getCell(cellStructureList,
            PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_CENTER, null,new BaseColor(243, 243, 243),true));
        headerTable2.setSpacingAfter(40);
        PdfPTable headerTable3 = new PdfPTable(1);
        try {
            headerTable3.setWidths(new float[] {25f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        headerTable3.setWidthPercentage(100);
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("plan.name", null, locale)+" :",
            mediumBoldFontCourier,false,true));
        headerTable3.addCell(getCell(cellStructureList,
            PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_CENTER, null, BaseColor.WHITE,true));
        headerTable3.setSpacingAfter(10);
















        PdfPTable headerTable4 = new PdfPTable(6);
        try {
            headerTable4.setWidths(new float[] {20f, 20f, 20f, 20f,20f,20f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        headerTable4.setWidthPercentage(100);
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("date.name", null, locale),
            mediumBoldFontCourier, false, true));
        headerTable4.addCell(getCell(cellStructureList,
            PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_CENTER, null, new BaseColor(173, 207, 255), false));
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("act.name", null, locale),
            mediumBoldFontCourier,false,true));
        headerTable4.addCell(getCell(cellStructureList,
            PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_CENTER, null, new BaseColor(173, 207, 255), false));
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("act.category.name", null, locale),
            mediumBoldFontCourier,false,true));
        headerTable4.addCell(getCell(cellStructureList,
            PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_CENTER, null, new BaseColor(173, 207, 255), false));
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("act.description.name", null, locale),
            mediumBoldFontCourier, false, true));
        headerTable4.addCell(getCell(cellStructureList,
            PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_CENTER, null, new BaseColor(173, 207, 255), false));
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("act.teeth.name", null, locale),
            mediumBoldFontCourier,false,true));
        headerTable4.addCell(getCell(cellStructureList,
            PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_CENTER, null, new BaseColor(173, 207, 255), false));
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("act.price.name", null, locale),
            mediumBoldFontCourier,false,true));
        headerTable4.addCell(getCell(cellStructureList,
            PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_CENTER, null, new BaseColor(173, 207, 255),false));
        headerTable4.setSpacingAfter(40);










        try {
            document.add(headerTable);
            document.add(headerTable2);
            document.add(headerTable3);
            document.add(headerTable4);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();

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

    /*
        void constructTheHeader(List<RowStructure> rows, Structure structure, HttpServletRequest request, GenericDocumentGenrator doc) {
        }

        void constructTheBody(List<RowStructure> rows, Structure structure, HttpServletRequest request, GenericDocumentGenrator doc) {
        }
    */

    public PdfPCell getCell(String text, int horizontalAlignment, int verticalAlignment,Font font, BaseColor baseColor,boolean noBorder) {
        Phrase phrase = new Phrase();
        phrase.add(
            new Chunk(text, font)
        );
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(4f);
        cell.setPaddingTop(1f);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        cell.setBackgroundColor(baseColor);

        if(noBorder){
            cell.setBorder(PdfPCell.NO_BORDER);
        }
        return cell;
    }

    public void addChunk(Phrase phrase, String text, Font font) {
        phrase.add(
            new Chunk(text, font)
        );
    }

    public PdfPCell getCell(java.util.List<CellStructure> cellStructureList, int horizontalAlignment, int verticalAlignment,Font font,BaseColor baseColor, boolean noBorder) {
        Phrase phrase = new Phrase();
        for(CellStructure cellStructure:cellStructureList){
            if(cellStructure.isNewLineBefore()){
                phrase.add(Chunk.NEWLINE);
            }
            phrase.add(new Chunk(cellStructure.getCellValue(), font==null?cellStructure.getFont():font));
            if(cellStructure.isNewLineAfter()){
                phrase.add(Chunk.NEWLINE);
            }
        }
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(4f);
        cell.setPaddingTop(1f);
        cell.setBackgroundColor(baseColor);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        if(noBorder){
            cell.setBorder(PdfPCell.NO_BORDER);
        }
        return cell;
    }

    public static class CellStructure {
        private String cellValue;
        private Font font;
        private boolean newLineBefore;
        private boolean newLineAfter;

        public CellStructure(String cellValue, Font font,
                             boolean newLineBefore, boolean newLineAfter) {
            this.cellValue = cellValue;
            this.font = font;
            this.newLineBefore = newLineBefore;
            this.newLineAfter = newLineAfter;
        }

        public boolean isNewLineAfter() {
            return newLineAfter;
        }

        public void setNewLineAfter(boolean newLineAfter) {
            this.newLineAfter = newLineAfter;
        }

        public boolean isNewLineBefore() {
            return newLineBefore;
        }

        public void setNewLineBefore(boolean newLineBefore) {
            this.newLineBefore = newLineBefore;
        }

        public String getCellValue() {
            return cellValue;
        }

        public void setCellValue(String cellValue) {
            this.cellValue = cellValue;
        }

        public Font getFont() {
            return font;
        }

        public void setFont(Font font) {
            this.font = font;
        }

    }




}
