package com.winbit.windoctor.service.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by achraftalbi on 1/21/17.
 */
public class PdfGenericDocumentGenrator {
    private Structure structure;

    public static final Logger logger = Logger.getLogger(PdfGenericDocumentGenrator.class);

    public void writeStructure(){
        if(structure.getTables() != null && !structure.getTables().isEmpty())
        {
            structure.getDocument().open();
            for(PdfPTable table : structure.getTables() )
            {
                try {
                    structure.getDocument().add(table);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            structure.getDocument().close();
        }
    }

    public PdfGenericDocumentGenrator(Structure structure){
        this.structure = structure;
    }

    public static class Structure {

        private List<PdfPTable> tables;
        private Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        public Structure(List<PdfPTable> tables) {
            this.tables = tables;
            try {
                PdfWriter.getInstance(document, baos);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        public PdfPTable getNewTable(Integer colomnsNumber, float[] colomnsWidths, Integer widthPercentage,
                           Integer spacingAfter, Integer spacingBefore) {
            PdfPTable pdfPTable = new PdfPTable(colomnsNumber   );
            if(colomnsWidths!=null){
                try {
                    pdfPTable.setWidths(colomnsWidths);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            if(widthPercentage!=null){
                pdfPTable.setWidthPercentage(widthPercentage);
            }
            if(spacingAfter!=null){
                pdfPTable.setSpacingAfter(spacingAfter);
            }
            if(spacingBefore!=null){
                pdfPTable.setSpacingBefore(spacingBefore);
            }
            tables.add(pdfPTable);
            return  pdfPTable;
        }

        public static PdfPCell getCell(String text, Integer horizontalAlignment, Integer verticalAlignment,Font font,
                                BaseColor baseColor,boolean noBorder,Float padding,Float paddingTop) {
            Phrase phrase = new Phrase();
            phrase.add(
                new Chunk(text, font)
            );
            PdfPCell cell = new PdfPCell(phrase);
            if(padding!=null){
                cell.setPadding(padding);
            }else{
                cell.setPadding(0l);
            }
            if(paddingTop!=null){
                cell.setPaddingTop(paddingTop);
            }else{
                cell.setPaddingTop(0l);
            }

            if(horizontalAlignment!=null){
                cell.setHorizontalAlignment(horizontalAlignment);
            }
            if(verticalAlignment!=null){
                cell.setVerticalAlignment(verticalAlignment);
            }
            cell.setBackgroundColor(baseColor);

            if(noBorder){
                cell.setBorder(PdfPCell.NO_BORDER);
            }
            return cell;
        }

        public static void addChunk(Phrase phrase, String text, Font font) {
            phrase.add(
                new Chunk(text, font)
            );
        }

        public static PdfPCell getCell(java.util.List<CellStructure> cellStructureList, Integer horizontalAlignment,
                                Integer verticalAlignment,Font font,BaseColor baseColor, boolean noBorder,Float padding,Float paddingTop) {
            Phrase phrase = new Phrase();
            for(CellStructure cellStructure:cellStructureList){
                if(cellStructure.isNewLineBefore()){
                    phrase.add(Chunk.NEWLINE);
                }
                /*logger.info("cellStructure "+cellStructure);
                logger.info("cellStructure.getCellValue() "+cellStructure.getCellValue());
                logger.info("cellStructure.getFont() "+cellStructure.getFont());
                logger.info("font "+font);*/
                phrase.add(new Chunk(cellStructure.getCellValue(), font==null?cellStructure.getFont():font));
                if(cellStructure.isNewLineAfter()){
                    phrase.add(Chunk.NEWLINE);
                }
            }
            PdfPCell cell = new PdfPCell(phrase);
            if(padding!=null){
                cell.setPadding(padding);
            }else{
                cell.setPadding(0l);
            }
            if(paddingTop!=null){
                cell.setPaddingTop(paddingTop);
            }else{
                cell.setPaddingTop(0l);
            }
            cell.setBackgroundColor(baseColor);
            if(horizontalAlignment!=null){
                cell.setHorizontalAlignment(horizontalAlignment);
            }
            if(verticalAlignment!=null){
                cell.setVerticalAlignment(verticalAlignment);
            }
            if(noBorder){
                cell.setBorder(PdfPCell.NO_BORDER);
            }
            return cell;
        }

        public List<PdfPTable> getTables() {
            return tables;
        }

        public void setTables(List<PdfPTable> tables) {
            this.tables = tables;
        }

        public Document getDocument() {
            return document;
        }

        public void setDocument(Document document) {
            this.document = document;
        }

        public ByteArrayOutputStream getBaos() {
            return baos;
        }

        public void setBaos(ByteArrayOutputStream baos) {
            this.baos = baos;
        }
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

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }


}
