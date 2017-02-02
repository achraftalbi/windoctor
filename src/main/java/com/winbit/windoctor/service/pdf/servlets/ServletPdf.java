package com.winbit.windoctor.service.pdf.servlets;

import com.itextpdf.text.pdf.PdfPTable;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.Plan;
import com.winbit.windoctor.repository.Pdf_documentRepository;
import com.winbit.windoctor.repository.PlanRepository;
import com.winbit.windoctor.repository.TreatmentRepository;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.service.MailService;
import com.winbit.windoctor.service.pdf.PdfGenericDocumentGenrator;
import com.winbit.windoctor.service.pdf.PdfGenericDocumentGenrator.Structure;
import com.winbit.windoctor.service.pdf.servlets.treatments.PlanServletPdfTreatments;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by achraftalbi on 1/11/17.
 */
/*@WebServlet(name = "PatientPlanServlet", urlPatterns
    = {
    "/servlet/ServletPdf"
})*/
public class ServletPdf extends HttpServlet {

    public static final Logger logger = Logger.getLogger(ServletPdf.class);

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
    @Inject
    private PlanRepository planRepository;
    @Inject
    private TreatmentRepository treatmentRepository;
    @Inject
    private Pdf_documentRepository pdf_documentRepository;
    @Inject
    private MailService mailService;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Listing 2. Creation of PdfWriter object
        PdfGenericDocumentGenrator pdfGenericDocumentGenrator = new PdfGenericDocumentGenrator(new Structure(new ArrayList<PdfPTable>()));
        Long planId = Long.valueOf(request.getParameter("planId"));



        Boolean livePdf = false;
        try {
            livePdf = Boolean.valueOf(request.getParameter("livePdf"));
        } catch (Exception ex) {
            logger.error("livePdf variable error", ex);
            ex.getMessage();
            livePdf = false;
        }
        Boolean savePdf = false;
        try {
            savePdf = Boolean.valueOf(request.getParameter("savePdf"));
        } catch (Exception ex) {
            logger.error("livePdf variable error", ex);
            ex.getMessage();
            savePdf = false;
        }
        Boolean sendMail = false;
        try {
            sendMail = Boolean.valueOf(request.getParameter("savePdf"));
        } catch (Exception ex) {
            logger.error("livePdf variable error", ex);
            ex.getMessage();
            sendMail = false;
        }

        if(planId !=null){
            Plan planOptionnal = planRepository.findOne(planId);
            if(planOptionnal!=null
                && SecurityUtils.getCurrerntStructure().equals(planOptionnal.getStructure().getId())){
                logger.info("1 Boolean livePdf" + livePdf);
                logger.info("1 Boolean savePdf" + savePdf);
                logger.info("1 Boolean sendMail" + sendMail);
                PlanServletPdfTreatments planServletPdfTreatments = new PlanServletPdfTreatments(messageSource, userRepository,
                    planRepository, treatmentRepository,
                    pdf_documentRepository,mailService);
                planServletPdfTreatments.
                    makePdfTreatments
                        (pdfGenericDocumentGenrator, response, planOptionnal,
                            livePdf, savePdf,sendMail);
            }
        }

    }

}
