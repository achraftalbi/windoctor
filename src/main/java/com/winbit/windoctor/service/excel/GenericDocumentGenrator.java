package com.winbit.windoctor.service.excel;

import java.util.List;

import com.winbit.windoctor.config.Constants;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * Created by achraftalbi on 1/11/17.
 */
public class GenericDocumentGenrator extends DocumentHelper {



    private Structure structure;
    public static final Logger logger = Logger.getLogger(GenericDocumentGenrator.class);
    public GenericDocumentGenrator(final String pDocumentName)
    {
        super.setDocumentName(pDocumentName);
        super.setWorkbook(new HSSFWorkbook());
        super.setWorksheet(super.getWorkbook().createSheet());
    }

    public GenericDocumentGenrator(final String pDocumentName, final boolean totalizeLastRow)
    {
        this(pDocumentName);
    }

    public void writeStructure(Long writeType){
        if(writeType==null){
            writeStructure();
        }else if(writeType.equals(0l)){
            writeStructureLimited();
        }
    }

    public void writeStructure(){
        if(structure.getRows() != null && !structure.getRows().isEmpty())
        {
            int i = 0;
            for(RowStructure row : structure.getRows() )
            {
                // https://jira.evision.ca/browse/CFIXD-11261 , some old excel format have limitiation up to 65535 row
                if(i < 65535) {
                    writeRow(row);
                    row.setRow(structure.getCurrentRow());
                    structure.setCurrentRow(structure.getCurrentRow()+1);
                }
                else{
                    break;
                }
                i++;
            }
        }
    }

    public void writeStructureLimited(){
        logger.info("structure "+structure);
        logger.info("structure.getRows() "+structure.getRows());
        logger.info("structure.getRows().isEmpty() "+structure.getRows().isEmpty());
        if(structure.getRows() != null && !structure.getRows().isEmpty())
        {
            int i = 0;

            for(RowStructure row : structure.getRows() )
            {
                // https://jira.evision.ca/browse/CFIXD-11261 , some old excel format have limitiation up to 65535 row
                if(i < 65535) {

                    writeRowLimited(row);
                    row.setRow(structure.getCurrentRow());
                    structure.setCurrentRow(structure.getCurrentRow()+1);

                }
                else{
                    break;
                }
                i++;
            }
        }
    }

    private void writeRow(RowStructure rowStructure){

        if(rowStructure != null && rowStructure.getColumns()!=null && !rowStructure.getColumns().isEmpty())
        {
            Row mockRow;
            mockRow = super.getWorksheet().createRow(structure.getCurrentRow().intValue());
            if(rowStructure.getRowHeight()!=null){
                mockRow.setHeight(rowStructure.getRowHeight().shortValue());
            }
            Cell mockCell;
            for(int i = 0; i < rowStructure.getColumns().size(); i++)
            {
                if(rowStructure.getColumns().get(i).getColumn()!=null && !rowStructure.getColumns().get(i).getColumn().equals(0l)){
                    mockCell = mockRow.createCell(rowStructure.getColumns().get(i).getColumn().shortValue());
                }else{
                    mockCell = mockRow.createCell(i);
                }


                //logger.info("rowStructure.getColumns().get(i).getCellValue() value "+rowStructure.getColumns().get(i).getCellValue());
                if(rowStructure.getColumns().get(i).getCellValue() != null
                    && !rowStructure.getColumns().get(i).getCellValue().equals("null")){
                    if(Constants.LONG.equals(rowStructure.getColumns().get(i).getTypeCell())){
                        try{
                            mockCell.setCellValue(new Long(rowStructure.getColumns().get(i).getCellValue()));
                        }catch(Exception e){
                            mockCell.setCellValue(rowStructure.getColumns().get(i).getCellValue());
                        }
                    }else if(Constants.DOUBLE.equals(rowStructure.getColumns().get(i).getTypeCell())){
                        try{
                            mockCell.setCellValue(new Double(rowStructure.getColumns().get(i).getCellValue()));
                        }catch(Exception e){
                            mockCell.setCellValue(rowStructure.getColumns().get(i).getCellValue());
                        }
                    }else{
                        mockCell.setCellValue(rowStructure.getColumns().get(i).getCellValue());
                    }
                }else{
                    mockCell.setCellValue("");
                }
                mockCell.setCellStyle(rowStructure.getColumns().get(i).getCellStyle());
                if(rowStructure.getColumns().get(i).isMerged()){
                    super.getWorksheet().addMergedRegion(new CellRangeAddress(structure.getCurrentRow().intValue(),
                        rowStructure.getColumns().get(i).getNumberRowToMerge()==null?
                            structure.getCurrentRow().intValue():structure.getCurrentRow().intValue()
                            +rowStructure.getColumns().get(i).getNumberRowToMerge().intValue(),
                        rowStructure.getColumns().get(i).getFirstCellMerged().intValue(),
                        rowStructure.getColumns().get(i).getLastCellMerged().intValue()));
                }
                if(rowStructure.getColumns().get(i).isAutosize()){
                    super.getWorksheet().autoSizeColumn(i);
                }else if(rowStructure.getColumns().get(i).getSize()!=null){
                    super.getWorksheet().setColumnWidth(i, rowStructure.getColumns().get(i).
                        getSize().intValue());
                }

            }
        }
    }

    private void writeRowLimited(RowStructure rowStructure){
        if(rowStructure != null && rowStructure.getColumns()!=null && !rowStructure.getColumns().isEmpty())
        {
            Row mockRow;
            mockRow = super.getWorksheet().createRow(structure.getCurrentRow().intValue());
            if(rowStructure.getRowHeight()!=null){
                mockRow.setHeight(rowStructure.getRowHeight().shortValue());
            }
            Cell mockCell;
            CellStyle cellStyle = null;
            for(int i = 0; i < rowStructure.getColumns().size(); i++)
            {
                if(rowStructure.getColumns().get(i).getColumn()!=null && !rowStructure.getColumns().get(i).getColumn().equals(0l)){
                    mockCell = mockRow.createCell(rowStructure.getColumns().get(i).getColumn().shortValue());
                }else{
                    mockCell = mockRow.createCell(i);
                }

                //if(rowStructure.getColumns().get(i).getCellStyle()!=cellStyle){
                //cellStyle = rowStructure.getColumns().get(i).getCellStyle();
                //mockCell.setCellStyle(cellStyle);
                //}
                //logger.info("rowStructure.getColumns().get(i).getCellValue() value "+rowStructure.getColumns().get(i).getCellValue());
                if(rowStructure.getColumns().get(i).getCellValue() != null
                    && !rowStructure.getColumns().get(i).getCellValue().equals("null")){
                    mockCell.setCellValue(rowStructure.getColumns().get(i).getCellValue());
                }else{
                    mockCell.setCellValue("");
                }
                if(rowStructure.getColumns().get(i).isMerged()){
                    super.getWorksheet().addMergedRegion(new CellRangeAddress(structure.getCurrentRow().intValue(),
                        rowStructure.getColumns().get(i).getNumberRowToMerge()==null?
                            structure.getCurrentRow().intValue():structure.getCurrentRow().intValue()
                            +rowStructure.getColumns().get(i).getNumberRowToMerge().intValue(),
                        rowStructure.getColumns().get(i).getFirstCellMerged().intValue(),
                        rowStructure.getColumns().get(i).getLastCellMerged().intValue()));
                }
                if(rowStructure.getColumns().get(i).isAutosize()){
                    super.getWorksheet().autoSizeColumn(i);
                }else if(rowStructure.getColumns().get(i).getSize()!=null){
                    super.getWorksheet().setColumnWidth(i, rowStructure.getColumns().get(i).
                        getSize().intValue());
                }
            }
        }
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public static class Structure {

        private Long currentRow;

        private List<RowStructure> rows;

        public Structure(List<RowStructure> rows, Long currentRow) {
            this.rows = rows;
            this.currentRow = currentRow;
        }

        public List<RowStructure> getRows() {
            return rows;
        }

        public void setRows(List<RowStructure> rows) {
            this.rows = rows;
        }

        public Long getCurrentRow() {
            return currentRow;
        }

        public void setCurrentRow(Long currentRow) {
            this.currentRow = currentRow;
        }

    }

    public static class RowStructure {

        private List<CellStructure> columns;
        private Long row;
        private Long rowHeight;

        public RowStructure(List<CellStructure> columns, Long row, Long rowHeight) {
            this.columns = columns;
            this.row = row;
            this.rowHeight = rowHeight;
        }

        public List<CellStructure> getColumns() {
            return columns;
        }

        public void setColumns(List<CellStructure> columns) {
            this.columns = columns;
        }

        public Long getRow() {
            return row;
        }

        public void setRow(Long row) {
            this.row = row;
        }

        public Long getRowHeight() {
            return rowHeight;
        }

        public void setRowHeight(Long rowHeight) {
            this.rowHeight = rowHeight;
        }

    }

    public static class CellStructure {
        private String cellValue;
        private CellStyle cellStyle;
        private boolean autosize = false;
        private boolean merged = false;

        private Long firstCellMerged;
        private Long lastCellMerged;
        private Long numberRowToMerge=null;
        private Long column;
        private Long size;
        private Long typeCell=Constants.OTHER;


        public CellStructure(String cellValue, CellStyle cellStyle, boolean autosize, Long size) {
            this.cellValue = cellValue;
            this.cellStyle = cellStyle;
            this.autosize = autosize;
            this.size = size;
        }

        public CellStructure(String cellValue, CellStyle cellStyle, boolean autosize, boolean merged,
                             Long firstCellMerged, Long lastCellMerged, Long column, Long size) {
            this.cellValue = cellValue;
            this.cellStyle = cellStyle;
            this.autosize = autosize;
            this.merged = merged;
            this.firstCellMerged = firstCellMerged;
            this.lastCellMerged = lastCellMerged;
            this.column = column;
            this.size = size;
        }

        public CellStructure(String cellValue, CellStyle cellStyle, boolean autosize, boolean merged,
                             Long firstCellMerged, Long lastCellMerged, Long column, Long size,Long typeCell) {
            this.cellValue = cellValue;
            this.cellStyle = cellStyle;
            this.autosize = autosize;
            this.merged = merged;
            this.firstCellMerged = firstCellMerged;
            this.lastCellMerged = lastCellMerged;
            this.column = column;
            this.size = size;
            this.typeCell = typeCell;
        }

        public CellStructure(String cellValue, CellStyle cellStyle, boolean autosize, boolean merged,
                             Long firstCellMerged, Long lastCellMerged, Long numberRowToMerge,Long column, Long size,Long typeCell) {
            this.cellValue = cellValue;
            this.cellStyle = cellStyle;
            this.autosize = autosize;
            this.merged = merged;
            this.firstCellMerged = firstCellMerged;
            this.lastCellMerged = lastCellMerged;
            this.numberRowToMerge=numberRowToMerge;
            this.column = column;
            this.size = size;
            this.typeCell = typeCell;
        }

        public String getCellValue() {
            return cellValue;
        }

        public void setCellValue(String cellValue) {
            this.cellValue = cellValue;
        }

        public CellStyle getCellStyle() {
            return cellStyle;
        }

        public void setCellStyle(CellStyle cellStyle) {
            this.cellStyle = cellStyle;
        }

        public boolean isAutosize() {
            return autosize;
        }

        public void setAutosize(boolean autosize) {
            this.autosize = autosize;
        }

        public boolean isMerged() {
            return merged;
        }

        public void setMerged(boolean merged) {
            this.merged = merged;
        }

        public Long getFirstCellMerged() {
            return firstCellMerged;
        }

        public void setFirstCellMerged(Long firstCellMerged) {
            this.firstCellMerged = firstCellMerged;
        }

        public Long getLastCellMerged() {
            return lastCellMerged;
        }

        public void setLastCellMerged(Long lastCellMerged) {
            this.lastCellMerged = lastCellMerged;
        }

        public Long getColumn() {
            return column;
        }

        public void setColumn(Long column) {
            this.column = column;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public Long getTypeCell() {
            return typeCell;
        }

        public void setTypeCell(Long typeCell) {
            this.typeCell = typeCell;
        }

        public Long getNumberRowToMerge() {
            return numberRowToMerge;
        }

        public void setNumberRowToMerge(Long numberRowToMerge) {
            this.numberRowToMerge = numberRowToMerge;
        }




    }

    public CellStyle getCellStyle(boolean bold, Short alignType, Short verticalAlignment, String formatString){
        CellStyle cellStyle = super.getWorkbook().createCellStyle();
        HSSFFont font = super.getWorkbook().createFont();
        font.setBold(bold);
        cellStyle.setFont(font);
        if(formatString!=null){
            DataFormat format = super.getWorkbook().createDataFormat();
            cellStyle.setDataFormat(format.getFormat(formatString));
        }
        if(alignType!=null){
            cellStyle.setAlignment(alignType);
        }
        if(verticalAlignment!=null){
            cellStyle.setVerticalAlignment(verticalAlignment);
        }


        return cellStyle;
    }
    public CellStyle getCellStyle(boolean bold, Short alignType, Short verticalAlignment, String formatString,boolean wraptext){
        CellStyle cellStyle = super.getWorkbook().createCellStyle();
        HSSFFont font = super.getWorkbook().createFont();

        font.setBold(bold);

        cellStyle.setFont(font);
        if(formatString!=null){
            DataFormat format = super.getWorkbook().createDataFormat();
            cellStyle.setDataFormat(format.getFormat(formatString));
        }
        if(alignType!=null){
            cellStyle.setAlignment(alignType);
        }
        if(verticalAlignment!=null){
            cellStyle.setVerticalAlignment(verticalAlignment);
        }
        if(wraptext){
            cellStyle.setWrapText(wraptext);
        }

        return cellStyle;
    }

    public CellStyle getCellStyle(boolean bold,boolean underline,Short color,boolean withbackgroundColor,Short backgroundColor, Short alignType, Short verticalAlignment, String formatString,boolean wraptext,Short bordertype){
        CellStyle cellStyle = super.getWorkbook().createCellStyle();
        HSSFFont font = super.getWorkbook().createFont();

        font.setBold(bold);
        if(color!=null){
            font.setColor(color);
        }

        if(underline){
            font.setUnderline(HSSFFont.U_SINGLE);
        }
        cellStyle.setFont(font);
        if(formatString!=null){
            DataFormat format = super.getWorkbook().createDataFormat();
            cellStyle.setDataFormat(format.getFormat(formatString));
        }
        if(alignType!=null){
            cellStyle.setAlignment(alignType);
        }
        if(verticalAlignment!=null){
            cellStyle.setVerticalAlignment(verticalAlignment);
        }
        if(wraptext){
            cellStyle.setWrapText(wraptext);
        }
        if(bordertype !=null){
            cellStyle.setBorderBottom(bordertype);
            cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
            cellStyle.setBorderLeft(bordertype);
            cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
            cellStyle.setBorderRight(bordertype);
            cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
            cellStyle.setBorderTop(bordertype);
            cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
        }
        if(withbackgroundColor){

            cellStyle.setFillForegroundColor(backgroundColor);
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        }
        return cellStyle;
    }

    public CellStyle getCellStyle(boolean bold,Short color,boolean withbackgroundColor,Byte[] rgb, Short alignType, Short verticalAlignment, String formatString,boolean wraptext,Short bordertype){
        CellStyle cellStyle = super.getWorkbook().createCellStyle();
        HSSFFont font = super.getWorkbook().createFont();

        font.setBold(bold);
        if(color!=null){
            font.setColor(color);
        }


        cellStyle.setFont(font);
        if(formatString!=null){
            DataFormat format = super.getWorkbook().createDataFormat();
            cellStyle.setDataFormat(format.getFormat(formatString));
        }
        if(alignType!=null){
            cellStyle.setAlignment(alignType);
        }
        if(verticalAlignment!=null){
            cellStyle.setVerticalAlignment(verticalAlignment);
        }
        if(wraptext){
            cellStyle.setWrapText(wraptext);
        }
        if(bordertype !=null){
            cellStyle.setBorderBottom(bordertype);
            cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
            cellStyle.setBorderLeft(bordertype);
            cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
            cellStyle.setBorderRight(bordertype);
            cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
            cellStyle.setBorderTop(bordertype);
            cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
        }
        if(withbackgroundColor){

            HSSFPalette palette = this.getWorkbook().getCustomPalette();
            palette.setColorAtIndex(rgb[0],rgb[1],rgb[2],rgb[3]);
            cellStyle.setFillForegroundColor(palette.getColor(rgb[0]).getIndex());
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        }
        return cellStyle;
    }

    public CellStyle getComplexCellStyle(boolean bold, Short alignType, Short verticalAlignment, boolean wrap, String formatString){
        CellStyle cellStyle = getCellStyle(bold, alignType, verticalAlignment,formatString);
        cellStyle.setWrapText(wrap);
        return cellStyle;
    }

}
