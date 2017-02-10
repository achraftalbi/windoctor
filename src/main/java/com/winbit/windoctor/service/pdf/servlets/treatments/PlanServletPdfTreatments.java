package com.winbit.windoctor.service.pdf.servlets.treatments;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.winbit.windoctor.common.WinDoctorConstants;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.*;
import com.winbit.windoctor.repository.Pdf_documentRepository;
import com.winbit.windoctor.repository.PlanRepository;
import com.winbit.windoctor.repository.TreatmentRepository;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.service.MailService;
import com.winbit.windoctor.service.pdf.PdfGenericDocumentGenrator;
import com.winbit.windoctor.service.pdf.PdfGenericDocumentGenrator.CellStructure;
import com.winbit.windoctor.service.pdf.PdfGenericDocumentGenrator.Structure;
import com.winbit.windoctor.web.rest.util.FunctionsUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.context.MessageSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by achraftalbi on 1/11/17.
 */
public class PlanServletPdfTreatments  {

    public static final Logger logger = Logger.getLogger(PlanServletPdfTreatments.class);
    public static Font boldFontCourier = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);
    public static Font bigBoldFontCourier = new Font(Font.FontFamily.COURIER, 14, Font.BOLD);
    public static Font mediumBoldFontCourier = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);
    public static Font mediumBoldFontCourierWhite = new Font(Font.FontFamily.COURIER, 10, Font.BOLD,new BaseColor(255, 255, 255));
    public static Font mediumFontCourier = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);
    public static Font normaleFontCourier = new Font(Font.FontFamily.COURIER, 7, Font.NORMAL);
    private Locale locale = Locale.forLanguageTag("fr");

    private MessageSource messageSource;
    private UserRepository userRepository;
    private PlanRepository planRepository;
    private TreatmentRepository treatmentRepository;
    private Pdf_documentRepository pdf_documentRepository;
    private MailService mailService;

    public PlanServletPdfTreatments(){
    }

    public PlanServletPdfTreatments(MessageSource messageSource, UserRepository userRepository,
                                    PlanRepository planRepository, TreatmentRepository treatmentRepository,
                                    Pdf_documentRepository pdf_documentRepository, MailService mailService){
        this.messageSource = messageSource;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.treatmentRepository = treatmentRepository;
        this.pdf_documentRepository = pdf_documentRepository;
        this.mailService = mailService;
    }

    public void makePdfTreatments(PdfGenericDocumentGenrator pdfGenericDocumentGenrator, HttpServletResponse response,
                                     Plan planOptionnal, Boolean livePdf, Boolean savePdf, Boolean sendMail)
        throws ServletException, IOException {

        // Listing 2. Creation of PdfWriter object

        //Object[] subjectVariables = {""+patientId};
        logger.info("userRepository " + userRepository);
        Long patientId = planOptionnal.getUser_id();
        logger.info("boolean - livePdf" + livePdf);
        logger.info("boolean savePdf" + savePdf);
        logger.info("boolean sendMail" + sendMail);

        Optional<User> userOptional = userRepository.findOneById(patientId);
        logger.info("patient id value " + patientId);
        logger.info("userOptional " + userOptional);
        User patient = userOptional.get();
        if(patient!=null
            && SecurityUtils.getCurrerntStructure().equals(patient.getStructure().getId())){
        }
        Object[] subjectVariables = {patient.getStructure().getName()};
        PdfPTable headerTable = null;
        java.util.List<CellStructure> cellStructureList = null;

        // All important treatments begin here
        if((livePdf!=null && livePdf==true) || (savePdf!=null && savePdf==true) || (sendMail!=null && sendMail==true)) {
            constructTheHeader(pdfGenericDocumentGenrator, patient, planOptionnal, subjectVariables, headerTable, cellStructureList);
            constructTheBody(pdfGenericDocumentGenrator, patient, planOptionnal, subjectVariables, headerTable, cellStructureList);
            pdfGenericDocumentGenrator.writeStructure();
            if (savePdf && planOptionnal != null && (planOptionnal.getArchive() == null || planOptionnal.getArchive() == false)) {
                try {
                    savePlanPdfDocument(pdfGenericDocumentGenrator, planOptionnal);
                } catch (Exception ex) {
                    logger.error("Error when saving plan document ", ex);
                    ex.printStackTrace();
                }
            }
            if (sendMail) {
                try {
                    mailService.sendEstimationToPatientEmail(patient, locale, pdfGenericDocumentGenrator.getStructure().getBaos());
                } catch (Exception ex) {
                    logger.error("Error when sending mail to patient ", ex);
                    ex.printStackTrace();
                }
            }
        }
        if (planOptionnal.getPdf_document()!=null && livePdf!=null && livePdf==false){
            pdfGenericDocumentGenrator.getStructure().getBaos().reset();
            pdfGenericDocumentGenrator.getStructure().getBaos().write(planOptionnal.getPdf_document().getFile_content());
        }




        if(livePdf!=null){
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            // setting the content type
            response.setContentType("application/pdf");
            // the contentlength
            response.setContentLength(pdfGenericDocumentGenrator.getStructure().getBaos().size());
            // write ByteArrayOutputStream to the ServletOutputStream
            OutputStream os = response.getOutputStream();
            pdfGenericDocumentGenrator.getStructure().getBaos()
                .writeTo(os);

            os.flush();
            os.close();
        }
    }

    private void constructTheHeader(PdfGenericDocumentGenrator pdfGenericDocumentGenrator, User patient,Plan planOptionnal, Object[] subjectVariables,
                            PdfPTable headerTable, java.util.List<CellStructure> cellStructureList) {
        headerTable = pdfGenericDocumentGenrator.getStructure().getNewTable(3,null
            ,100,70,null);
        headerTable.setWidthPercentage(100);
        headerTable.addCell(Structure.getCell(messageSource.getMessage("structure.name", subjectVariables, locale),
            PdfPCell.ALIGN_LEFT, null, boldFontCourier, null, true, 0f, 0f));
        subjectVariables[0]=(Object)patient.getFirstName()+" "+patient.getLastName();
        headerTable.addCell(Structure.getCell(messageSource.getMessage("plan.estimate.name", subjectVariables, locale),
            PdfPCell.ALIGN_CENTER, null, bigBoldFontCourier, null, true, 0f, 0f));
        cellStructureList = new ArrayList<CellStructure>();
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
        PdfPCell pdfPCell = Structure.getCell(cellStructureList,
            PdfPCell.ALIGN_RIGHT, null, null, null, true, 0f, 0f);
        headerTable.addCell(pdfPCell);

        headerTable = pdfGenericDocumentGenrator.getStructure().getNewTable(5,new float[] {25f, 3f, 25f, 15f,30f}
            ,100,40,null);
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("estimation.date", null, locale),
            mediumFontCourier,false,true));
        cellStructureList.add(new CellStructure(messageSource.getMessage("plan.number", null, locale),
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure(messageSource.getMessage("patient.number", null, locale),
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure("",
            normaleFontCourier, false, true));
        headerTable.addCell(Structure.getCell(cellStructureList,
            PdfPCell.ALIGN_RIGHT, PdfPCell.ALIGN_CENTER, null, new BaseColor(243, 243, 243), true, 0f, 0f));
        headerTable.addCell(Structure.getCell("", PdfPCell.ALIGN_LEFT, Element.ALIGN_MIDDLE, boldFontCourier, new BaseColor(243, 243, 243), true, 0f, 0f));
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(FunctionsUtil.convertDateToString(new Date(), WinDoctorConstants.WinDoctorPattern.DATE_PATTERN, locale),
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure((planOptionnal.getNumber() != null ? planOptionnal.getNumber().intValue() : "") + "",
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure((patient.getNumber() != null ? patient.getNumber().intValue() : "") + "",
            mediumFontCourier, false, true));
        headerTable.addCell(Structure.getCell(cellStructureList,
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, null, new BaseColor(243, 243, 243), true, 0f, 0f));
        headerTable.addCell(Structure.getCell("", PdfPCell.ALIGN_LEFT, Element.ALIGN_MIDDLE, boldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("patient.name", null, locale),
            mediumBoldFontCourier, false, true));
        cellStructureList.add(new CellStructure(patient.getFirstName() + " " + patient.getLastName(),
            mediumFontCourier, false, true));
        if(patient.getPhoneNumber()!=null){
            cellStructureList.add(new CellStructure(messageSource.getMessage("indicative.name", null, locale)+patient.getPhoneNumber(),
                mediumFontCourier, false, true));
        }
        headerTable.addCell(Structure.getCell(cellStructureList,
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, null, new BaseColor(255, 255, 255), true, 0f, 0f));




        headerTable = pdfGenericDocumentGenrator.getStructure().getNewTable(1,new float[] {25f}
            ,100,10,null);
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("plan.name", null, locale) + " :",
            mediumBoldFontCourier, false, true));
        headerTable.addCell(Structure.getCell(cellStructureList,
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, null, BaseColor.WHITE, true, 0f, 0f));

    }

    private void constructTheBody(PdfGenericDocumentGenrator pdfGenericDocumentGenrator,User patient,Plan planOptionnal, Object[] subjectVariables,
                          PdfPTable headerTable, java.util.List<CellStructure> cellStructureList) {
        headerTable = pdfGenericDocumentGenrator.getStructure().getNewTable(6,new float[] {20f, 20f, 20f,20f,20f,20f}
            ,100,20,null);

        java.util.List<Treatment> treatments = treatmentRepository.findByPatientPlan(patient.getId(),planOptionnal.getId(), null);
        Treatment totaleTreatment = treatmentRepository.findTotalPatientTreatmentsPlan(patient.getId(), planOptionnal.getId());
        if(treatments!=null){
            logger.info("treatments size first "+treatments.size());
        }
        Treatment descriptionTreatment = new Treatment();
        treatments.add(0,descriptionTreatment);
        if(treatments!=null){
            logger.info("treatments size segond "+treatments.size());
        }
        for(Treatment treatment : treatments) {
            logger.info("treatment id "+treatment.getId());
            cellStructureList = new ArrayList<CellStructure>();
            cellStructureList.add(new CellStructure(treatment.getId()==null?
                messageSource.getMessage("date.name", null, locale):
                (treatment.getTreatment_date()==null || treatment.getStatus().getId()==Constants.STATUS_IN_PROGRESS?"":FunctionsUtil.convertDateToString(treatment.getTreatment_date().toDate(), WinDoctorConstants.WinDoctorPattern.DATE_PATTERN, locale))+"",
                treatment.getId()==null?mediumBoldFontCourierWhite:mediumFontCourier, false, true));
            headerTable.addCell(Structure.getCell(cellStructureList,
                PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_CENTER, null,
                treatment.getId()==null?new BaseColor(0, 0, 0):
                    (treatment.getStatus().getId()==Constants.STATUS_IN_PROGRESS?new BaseColor(117, 244, 255):new BaseColor(148, 255, 117)),
                false, 4f, 1f));
            cellStructureList = new ArrayList<CellStructure>();
            cellStructureList.add(new CellStructure(treatment.getId()==null?
                messageSource.getMessage("act.category.name", null, locale):
                (treatment.getEventReason().getCategoryAct()==null?"":treatment.getEventReason().getCategoryAct().getName())+"",
                treatment.getId()==null?mediumBoldFontCourierWhite:mediumFontCourier, false, true));
            headerTable.addCell(Structure.getCell(cellStructureList,
                PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_CENTER, null,
                treatment.getId()==null?new BaseColor(0, 0, 0):
                    (treatment.getStatus().getId()==Constants.STATUS_IN_PROGRESS?new BaseColor(117, 244, 255):new BaseColor(148, 255, 117)),
                false, 4f, 1f));
            cellStructureList = new ArrayList<CellStructure>();
            cellStructureList.add(new CellStructure(treatment.getId()==null?
                messageSource.getMessage("act.name", null, locale):
                treatment.getEventReason().getDescription()+"",
                treatment.getId()==null?mediumBoldFontCourierWhite:mediumFontCourier, false, true));
            headerTable.addCell(Structure.getCell(cellStructureList,
                PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_CENTER, null,
                treatment.getId()==null?new BaseColor(0, 0, 0):
                    (treatment.getStatus().getId()==Constants.STATUS_IN_PROGRESS?new BaseColor(117, 244, 255):new BaseColor(148, 255, 117)),
                false, 4f, 1f));
            cellStructureList = new ArrayList<CellStructure>();
            cellStructureList.add(new CellStructure(treatment.getId()==null?
                messageSource.getMessage("act.description.name", null, locale):
                treatment.getDescription()==null?"":treatment.getDescription(),
                treatment.getId()==null?mediumBoldFontCourierWhite:mediumFontCourier, false, true));
            headerTable.addCell(Structure.getCell(cellStructureList,
                PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_CENTER, null,
                treatment.getId()==null?new BaseColor(0, 0, 0):
                    (treatment.getStatus().getId()==Constants.STATUS_IN_PROGRESS?new BaseColor(117, 244, 255):new BaseColor(148, 255, 117)),
                false, 4f, 1f));
            cellStructureList = new ArrayList<CellStructure>();
            cellStructureList.add(new CellStructure(treatment.getId()==null?
                messageSource.getMessage("act.teeth.name", null, locale):
                treatment.getElements()==null?"":treatment.getElements(),
                treatment.getId()==null?mediumBoldFontCourierWhite:mediumFontCourier, false, true));
            headerTable.addCell(Structure.getCell(cellStructureList,
                PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_CENTER, null,
                treatment.getId()==null?new BaseColor(0, 0, 0):
                    (treatment.getStatus().getId()==Constants.STATUS_IN_PROGRESS?new BaseColor(117, 244, 255):new BaseColor(148, 255, 117)),
                false, 4f, 1f));
            cellStructureList = new ArrayList<CellStructure>();
            cellStructureList.add(new CellStructure(treatment.getId()==null?
                messageSource.getMessage("act.price.name", null, locale):
                (treatment.getPrice()==null?"0":treatment.getPrice().intValue()+""),
                treatment.getId()==null?mediumBoldFontCourierWhite:mediumFontCourier, false, true));
            headerTable.addCell(Structure.getCell(cellStructureList,
                PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_CENTER, null,
                treatment.getId()==null?new BaseColor(0, 0, 0):
                    (treatment.getStatus().getId()==Constants.STATUS_IN_PROGRESS?new BaseColor(117, 244, 255):new BaseColor(148, 255, 117)),
                false, 4f, 1f));


        }



        headerTable = pdfGenericDocumentGenrator.getStructure().getNewTable(6,new float[] {5f, 2f, 53f,20f,20f,20f}
            ,100,10,null);

        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, new BaseColor(117, 244, 255), true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell(messageSource.getMessage("act.inprogress.name", null, locale),
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));

        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));


        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, new BaseColor(148, 255, 117), true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell(messageSource.getMessage("act.realised.name", null, locale),
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));



        headerTable = pdfGenericDocumentGenrator.getStructure().getNewTable(6,new float[] {20f, 20f, 20f,20f,20f,20f}
            ,100,30,null);
        headerTable.addCell(Structure.getCell(" ",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("",
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("act.total.name", null, locale) + "",
            mediumBoldFontCourier, false, true));
        headerTable.addCell(Structure.getCell(cellStructureList,
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, null, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell((totaleTreatment==null?"0":totaleTreatment.getPrice()+"")+" DH",
            PdfPCell.ALIGN_RIGHT, PdfPCell.ALIGN_CENTER, mediumBoldFontCourier, BaseColor.WHITE, true, 0f, 0f));





        headerTable = pdfGenericDocumentGenrator.getStructure().getNewTable(5,new float[] {25f, 5f, 20f, 10f,35f}
            ,100,10,null);
        headerTable.addCell(Structure.getCell("", PdfPCell.ALIGN_LEFT, Element.ALIGN_MIDDLE, boldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("", PdfPCell.ALIGN_LEFT, Element.ALIGN_MIDDLE, boldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("", PdfPCell.ALIGN_LEFT, Element.ALIGN_MIDDLE, boldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        headerTable.addCell(Structure.getCell("", PdfPCell.ALIGN_LEFT, Element.ALIGN_MIDDLE, boldFontCourier, BaseColor.WHITE, true, 0f, 0f));
        cellStructureList = new ArrayList<CellStructure>();
        cellStructureList.add(new CellStructure(messageSource.getMessage("signature.name", null, locale),
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure("",
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure("",
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure("",
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure("",
            mediumFontCourier, false, true));
        cellStructureList.add(new CellStructure("",
            normaleFontCourier, false, true));
        cellStructureList.add(new CellStructure("",
            normaleFontCourier, false, true));
        cellStructureList.add(new CellStructure("",
            normaleFontCourier, false, true));
        headerTable.addCell(Structure.getCell(cellStructureList,
            PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER, null, new BaseColor(243, 243, 243), true, 0f, 0f));

    }

    private void savePlanPdfDocument(PdfGenericDocumentGenrator pdfGenericDocumentGenrator,Plan planOptionnal){
        Pdf_document pdf_document =null;
        DateTime currentDateTime = new DateTime();
        if(planOptionnal.getPdf_document()!=null){
            pdf_document = planOptionnal.getPdf_document();
        }else{
            pdf_document = new Pdf_document();
            Pdf_document_type pdf_document_type = new Pdf_document_type(1l);
            pdf_document.setPdf_document_type(pdf_document_type);
            pdf_document.setCreation_date(currentDateTime);
        }
        Pdf_document_type pdf_document_type = new Pdf_document_type(1l);
        pdf_document.setUpdate_date(currentDateTime);
        pdf_document.setOriginal_file_name("fileName");
        pdf_document.setMime_type("application/pdf");
        pdf_document.setFile_content(pdfGenericDocumentGenrator.getStructure().getBaos().toByteArray());
        pdf_document =pdf_documentRepository.save(pdf_document);
        if(planOptionnal.getPdf_document()==null){
            planOptionnal.setPdf_document(pdf_document);
            planRepository.save(planOptionnal);
        }
    }


}
