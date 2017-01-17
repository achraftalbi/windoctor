package com.winbit.windoctor.service.pdf.servlets;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.security.SecurityUtils;
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
import java.util.Locale;
import java.util.Optional;

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
        Paragraph title1 = new Paragraph(messageSource.getMessage("structure.name",subjectVariables,locale), FontFactory.getFont(
            FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(0,
                255, 255, 255)));
        Chunk glue = new Chunk(new VerticalPositionMark());
        title1.add(new Chunk(glue));
        title1.add("Devis");
        Paragraph title2 = new Paragraph("Devis", FontFactory.getFont(
            FontFactory.HELVETICA, 16, Font.BOLD, new CMYKColor(0, 255, 0,
                0)));
        Paragraph title3 = new Paragraph();
        title3.add(title1);
        title3.add(title2);
        //title2.setSpacingBefore(5000);
        Chapter chapter1 = new Chapter(title3, 0);
        //chapter1.add(title2);
        //chapter1.add(title2);
        chapter1.setNumberDepth(0);

        // Listing 5. Creation of section object
        Paragraph title11 = new Paragraph("This is Section 1 in Chapter 1",
            FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,
                new CMYKColor(0, 255, 255, 17)));
        Section section1 = chapter1.addSection(title11,0);
        Paragraph someSectionText = new Paragraph(
            "This text comes as part of section 1 of chapter 1.");
        section1.add(someSectionText);
        someSectionText = new Paragraph("Following is a 3 X 2 table.");
        section1.add(someSectionText);

        // Listing 6. Creation of table object
        PdfPTable t = new PdfPTable(3);

        t.setSpacingBefore(25);
        t.setSpacingAfter(25);
        PdfPCell c1 = new PdfPCell(new Phrase("Header1"));
        t.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase("Header2"));
        t.addCell(c2);
        PdfPCell c3 = new PdfPCell(new Phrase("Header3"));
        t.addCell(c3);
        t.addCell("1.1");
        t.addCell("1.2");
        t.addCell("1.3");
        section1.add(t);

        // Listing 7. Creation of list object
        List l = new List(false, false, 10);
        l.add(new ListItem("First item of list"));
        l.add(new ListItem("Second item of list"));
        section1.add(l);

        // Listing 8. Adding image to the main document

        /*Image image2 = null;
        try {
            image2 = Image.getInstance("IBMLogo.bmp");
        } catch (BadElementException e) {
            e.printStackTrace();
        }
        image2.scaleAbsolute(120f, 120f);
        section1.add(image2);*/

        // Listing 9. Adding Anchor to the main document.
        /*Paragraph title2 = new Paragraph("Using Anchor", FontFactory.getFont(
            FontFactory.HELVETICA, 16, Font.BOLD, new CMYKColor(0, 255, 0,
                0)));
        section1.add(title2);

        title2.setSpacingBefore(5000);
        Anchor anchor2 = new Anchor("Back To Top");
        anchor2.setReference("#BackToTop");

        section1.add(anchor2);*/


        // Listing 10. Addition of a chapter to the main document

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.addCell(getCell(messageSource.getMessage("structure.name", subjectVariables, locale),
            PdfPCell.ALIGN_LEFT, boldFontCourier));
        subjectVariables[0]=(Object)patient.getFirstName()+" "+patient.getLastName();
        table.addCell(getCell(messageSource.getMessage("plan.estimate.name", subjectVariables, locale),
            PdfPCell.ALIGN_CENTER, boldFontCourier));
        PdfPCell pdfPCell = getCell(messageSource.getMessage("entreprise.name", null, locale),
            PdfPCell.ALIGN_RIGHT,boldFontCourier);
        pdfPCell.getPhrase().add(Chunk.NEWLINE);
        addChunk(pdfPCell.getPhrase(), messageSource.getMessage("phone.name", null, locale),normaleFontCourier);
        pdfPCell.getPhrase().add(Chunk.NEWLINE);
        addChunk(pdfPCell.getPhrase(), messageSource.getMessage("entreprise.phone.name", null, locale), normaleFontCourier);
        pdfPCell.getPhrase().add(Chunk.NEWLINE);
        addChunk(pdfPCell.getPhrase(), messageSource.getMessage("entreprise.mobile.phone.name", null, locale),normaleFontCourier);
        table.addCell(pdfPCell);
        table.addCell(getCell("Text to the left",
            PdfPCell.ALIGN_LEFT,boldFontCourier));
        table.addCell(getCell("Text in the middle",
            PdfPCell.ALIGN_CENTER,boldFontCourier));
        table.addCell(getCell("Text to the right",
            PdfPCell.ALIGN_RIGHT,boldFontCourier));
        PdfPTable table2 = new PdfPTable(3);
        table2.setWidthPercentage(100);
        table2.addCell(getCell("Text to the Aleft",
            PdfPCell.ALIGN_LEFT,boldFontCourier));
        table2.addCell(getCell("Text in the Bmiddle",
            PdfPCell.ALIGN_CENTER,boldFontCourier));
        table2.addCell(getCell("Text to the Cright",
            PdfPCell.ALIGN_RIGHT,boldFontCourier));
        try {
            document.add(table);
            document.add(table2);
            document.add(chapter1);
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

    /*void constructTheHeader(List<RowStructure> rows, Structure structure, HttpServletRequest request, GenericDocumentGenrator doc) {
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
    }*/

    public PdfPCell getCell(String text, int alignment,Font font) {
        Phrase phrase = new Phrase();
        phrase.add(
            new Chunk(text, font)
        );
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
    public void addChunk(Phrase phrase, String text, Font font) {
        phrase.add(
            new Chunk(text, font)
        );
    }

    public static class CellStructure {
        private String cellValue;
        private Font font;

        public CellStructure(String cellValue, Font font) {
            this.cellValue = cellValue;
            this.font = font;
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
