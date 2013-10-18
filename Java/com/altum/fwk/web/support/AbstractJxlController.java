/*
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Suite 101, Reston, VA 20190 USA
 * Copyright (c) 2000-2010 Altum, Inc.
 * All rights reserved.
 */
package com.altum.fwk.web.support;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.altum.coding.web.support.UtilityController;

import jxl.CellReferenceHelper;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Blank;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public abstract class AbstractJxlController extends UtilityController {
	protected String fileName;
	private EnumMap<CellFormatEnum,WritableCellFormat> cellFormats;
	private WritableWorkbook workbook;
	private int sheetIndex;
	private SimpleDateFormat mDateFormatter;

	private final static int MAX_COLUMN_WIDTH=40;

	/**
	 * (DOES IT ALL)
	 * Creates the xls file in the Exports dir. Sends the session id file name back, expecting this to be an AJAX call.
	 * @param response	-Unused
	 */
	synchronized public ModelAndView generateSpreadsheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			privateInit(request);
			init(request);
			createWorkbook();
			populateWorkbook(request);//Subclass must fill, can use createSheet for each sheet.
			workbook.write();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fileName", fileName);//Name of fileName Key is ignored
			return new ModelAndView(JSON_VIEW,map);
		}finally{
			close();
			privateClose();
		}
	}
//To Override
	/**
	 * Use the helper methods below to create at least one sheet and add cells to it.
	 * @throws Exception
	 */
	abstract protected void populateWorkbook(HttpServletRequest request) throws Exception;//Subclass must fill
	/**
	 * Overrideable Stub
	 * @param request  
	 * @throws Exception 
	 */
	protected void init(HttpServletRequest request) throws Exception {
		//Empty
	}
	/** Overrideable Stub */
	protected void close(){
		//Empty
	}
	/** Set workbook as Protected */
	protected boolean protectWorkbook(){
		return false;
	}
	/** Default all created worksheets as Protected */
	protected boolean protectSheets(){
		return false;
	}
	
//Generic Helper section:
	protected WritableSheet createSheet(String sheetName){
		WritableSheet sheet = workbook.createSheet(sheetName, ++sheetIndex);
		sheet.getSettings().setProtected(protectSheets());
		return sheet;
	}
	/**
	 * Should be used to create all Header cells that need to dynamically set columnWidths.
	 */
	protected WritableCell createHeaderCell(WritableSheet sheet, CellFormatEnum cellFormat
			, int sheetRow, int[] columnDataTypes, int[] columnWidths, int column, String sCellValue, int columnDataType) throws WriteException {
		WritableCell cell = createLabelCell(sheet, sheetRow, column, sCellValue, cellFormat);
		int charCount = 0;
		for(String word : sCellValue.split(" ")){
			charCount=Math.max(charCount,word.length());
		}
		//The header is larger, and it appears the column width per character is based on the default style used in the rest of the column
		columnWidths[column] = Math.min(MAX_COLUMN_WIDTH, (int)(charCount*1.34)+2);
		columnDataTypes[column]=columnDataType;
		return cell;
	}
	/**
	 * also sets column comment.
	 */
	protected WritableCell createHeaderCell(WritableSheet sheet, CellFormatEnum cellFormat
			, int sheetRow, int[] columnDataTypes, int[] columnWidths, int column, String sCellValue, int columnDataType, String columnComment, double commentWidth, double commentHeight) throws WriteException {
		WritableCell cell = createHeaderCell(sheet, cellFormat, sheetRow, columnDataTypes, columnWidths, column, sCellValue, columnDataType);
		WritableCellFeatures wcf = new WritableCellFeatures();
		wcf.setComment( columnComment , commentWidth, commentHeight);
		cell.setCellFeatures(wcf);
		return cell;
	}
	/**
	 * Should be used to create all Data cells that need to dynamically set columnWidths.
	 */
	protected WritableCell createDataCell(WritableSheet sheet, int sheetRow, int[] columnDataTypes, int[] columnWidths
			, int column, String sCellValue, CellFormatEnum cellFormat,  int columnDataType) throws WriteException {
		WritableCell cell;
		if(sCellValue!=null && sCellValue.length()>0){
			switch(columnDataType!=-1?columnDataType:columnDataTypes[column]){
			case java.sql.Types.NUMERIC:
				sCellValue = sCellValue.replaceAll("<[aA][^>]*>(.*?)</[aA]>", "$1");
				cell = new jxl.write.Number(column, sheetRow, Double.valueOf(sCellValue), cellFormats.get(cellFormat));
				sheet.addCell(cell);
				break;
			case java.sql.Types.DATE: 
				try{
					cell = new DateTime(column, sheetRow, mDateFormatter.parse(sCellValue), cellFormats.get(CellFormatEnum.DATE));
					sheet.addCell(cell);
				}catch(ParseException pe) {
					cell = createLabelCell(sheet, sheetRow, column, sCellValue, cellFormat);
				}
				break;
			default:
				sCellValue = sCellValue.replaceAll("&[nN][bB][sS][pP];", " ").trim();
				sCellValue = sCellValue.replaceAll("<[aA][^>]*>(.*?)</[aA]>", "$1");
				cell = createLabelCell(sheet, sheetRow, column, sCellValue, cellFormat);
			}
			if(columnWidths[column]<MAX_COLUMN_WIDTH){
				columnWidths[column]=Math.min(MAX_COLUMN_WIDTH, Math.max(columnWidths[column],sCellValue.length()));
			}
		}else{
			cell = createBlankCell(sheet, sheetRow, column, cellFormat);
		}
		return cell;
	}
	/**
	 * Can be used to create all Totals Cells that Sum a range of rows in the same column.
	 * Note that cells in the range must already have been created for this to work properly.
	 */
	protected WritableCell createSumFormulaCell(WritableSheet sheet, int dataFirstRow, int dataLastRow, int totalsRow, int column, CellFormatEnum cellFormat) throws WriteException {
		StringBuffer sb = new StringBuffer("SUM(");
		CellReferenceHelper.getCellReference(column, dataFirstRow, sb);
		sb.append(":");
		CellReferenceHelper.getCellReference(column, dataLastRow, sb);
		sb.append(")");
		return createFormulaCell(sheet, totalsRow, column, sb.toString(), cellFormat);
	}
	protected String createHorizontalPreSumFormula(int sumColumn, int sheetRow, int count, int columnsToUseInterval){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<count; ){
			CellReferenceHelper.getCellReference(sumColumn+(++i*columnsToUseInterval), sheetRow, sb);
			if(i<count){//Note that i has already been incremented
				sb.append('+');
			}
		}
		return sb.toString();
	}
	/**
	 * Can be used to create all Formula cells.
	 * Note that cells (or at least one in their row?) in any cell reference must already have been created for this to work properly.
	 */
	protected WritableCell createFormulaCell(WritableSheet sheet, int sheetRow, int column, String sFormula, CellFormatEnum cellFormat) throws WriteException {
		WritableCell cell;
		if(sFormula!=null){
			cell=new Formula(column, sheetRow, sFormula, cellFormats.get(cellFormat));
			sheet.addCell(cell);
		}else{
			cell=createBlankCell(sheet, sheetRow, column, cellFormat);
		}
		return cell;
	}
	/**
	 * Can be used to create all groups of merged cells with a label.
	 */
	protected void createMergedLabelCell(WritableSheet sheet, int sheetRow, int columnFirst, int columnLast, String sCellValue, CellFormatEnum cellFormat) throws WriteException{
		createLabelCell(sheet, sheetRow, columnFirst, sCellValue, cellFormat);
		sheet.mergeCells(columnFirst, sheetRow, columnLast, sheetRow);
	}
	/**
	 * Can be used to create all Label cells.
	 */
	protected WritableCell createLabelCell(WritableSheet sheet, int sheetRow, int column, String sCellValue, CellFormatEnum cellFormat) throws WriteException {
		WritableCell cell = new Label(column, sheetRow, sCellValue, cellFormats.get(cellFormat));
		sheet.addCell(cell);
		return cell;
	}
	/**
	 * Can be used to create all Blank cells.
	 */
	protected WritableCell createBlankCell(WritableSheet sheet, int sheetRow, int column, CellFormatEnum cellFormat) throws WriteException {
		WritableCell cell = new Blank(column, sheetRow, cellFormats.get(cellFormat));
		sheet.addCell(cell);
		return cell;
	}
	/**
	 * Can be used to create all Hyperlink cells (href="http..." and href="mailto..."), not javascript).
	 * Its return value unfortunately does not implement WritableCell.
	 * Returns null if parsing was unsuccessful.
	 */
	protected WritableHyperlink createHyperLinkCell(WritableSheet sheet, int sheetRow, int[] columnWidths, int column, String sUrl, String desc) throws WriteException {
		try{
			WritableHyperlink wh = new WritableHyperlink(column, sheetRow, column, sheetRow, new URL(sUrl), desc);
			sheet.addHyperlink(wh);
			return wh;
		}catch(MalformedURLException mue) {
			mue.printStackTrace();
			return null;
		}
	}
	/**
	 * Adds a blank row where the latter half has a double line border between this blank row and the next, where the Sum cells are. 
	 * Set noBorderColumnCount to be where the TOTALS_CELL_NAME label is, the last column before the Sum cells.
	 */
	protected void populatePreTotalsRow(WritableSheet sheet, int sheetRow, int noBorderColumnCount, int totalColumnCount) throws WriteException {
		int column = 0;
		for(; column < noBorderColumnCount; ++column){
			createBlankCell(sheet, sheetRow, column, CellFormatEnum.NORMAL);
		}
		for(; column < totalColumnCount; ++column){
			createBlankCell(sheet, sheetRow, column, CellFormatEnum.BOTTOM_DOUBLE_BORDER);
		}
	}
	/**
	 * After the freezeRow (like excel, the first moving one, not last frozen) has been inserted
	 * and after all columnWidths are set...
	 * Set the sheet's freeze and set all column widths.
	 */
	protected void setVerticalFreezeAndColumnWidths(WritableSheet sheet, int freezeRow, int[] columnWidths, boolean setHeaderHeightForComments) throws Exception {
		if(setHeaderHeightForComments){
			//The header comments don't fit well on a normal sized row, because the freeze cells feature cuts off the bottom.
			CellView cv = new CellView();
			cv.setSize(420); //21pt - if smaller than 21, then the lowercase g in the comment for Obligated Dollars is cut off.
			sheet.setRowView(0, cv);
		}
		//sheet.getSettings().setVerticalFreeze(freezeRow);
		columnWidths[0] = Math.min(MAX_COLUMN_WIDTH, columnWidths[0]+2);//Project Num is generally a bit bigger
		for(int col=0; col<columnWidths.length; ++col){
			sheet.setColumnView(col, columnWidths[col]);
		}
	}
	

	
//PRIVATE
	private void privateInit(HttpServletRequest request) throws WriteException {
		fileName = "export.xls";
		setCellFormats();
		sheetIndex=-1;
		mDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
	}
	private void privateClose() throws WriteException, IOException {
		fileName=null;
		cellFormats=null;
		sheetIndex=-1;//not actually necessary
		mDateFormatter=null;
		if(workbook!=null){
			workbook.close();
		}
		workbook=null;
	}
	private void createWorkbook() throws Exception {
		StringBuffer sbExcelWriteFile = new StringBuffer();
		String sDir = getServletContext().getRealPath("/");
		char cTestVal = sDir.charAt(sDir.length()-1);
        if ('\\' != cTestVal) {
            sDir += "/";
        }
		sbExcelWriteFile.append(sDir);
		sbExcelWriteFile.append("exports/");
		sbExcelWriteFile.append(fileName);
		File file = new File(sbExcelWriteFile.toString());

		workbook = Workbook.createWorkbook(file);
		workbook.setProtected(protectWorkbook());
	}
	
	/**
	 * Reason (from the tutorial):
	 * "The long and the short of it is that if it is necessary to re-use formats across multiple workbooks, then the WritableCellFormat objects must be re-created and initialised along with the each Workbook instance, and NOT re-used from a previous workbook." 
	 */
	private void setCellFormats() throws WriteException {

		WritableFont titleFont	= new WritableFont(WritableFont.ARIAL,WritableFont.DEFAULT_POINT_SIZE+6,WritableFont.BOLD);
		WritableFont warningFont = new WritableFont(WritableFont.ARIAL,WritableFont.DEFAULT_POINT_SIZE+2,WritableFont.BOLD,
													false, UnderlineStyle.NO_UNDERLINE, Colour.DARK_RED);
		WritableFont headerFont = new WritableFont(WritableFont.ARIAL,WritableFont.DEFAULT_POINT_SIZE+2,WritableFont.BOLD);
		WritableFont boldFont = new WritableFont(WritableFont.ARIAL,WritableFont.DEFAULT_POINT_SIZE,WritableFont.BOLD);
		WritableFont headerFont2 = new WritableFont(WritableFont.ARIAL,WritableFont.DEFAULT_POINT_SIZE,WritableFont.BOLD,
			false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
		
		cellFormats = new EnumMap<CellFormatEnum,WritableCellFormat>(CellFormatEnum.class);
		
		WritableCellFormat wcf = new WritableCellFormat(titleFont);
		//wcf.setWrap(true); Wrapping would prevent the height from being adjusted properly.
		wcf.setAlignment(Alignment.CENTRE);
		cellFormats.put(CellFormatEnum.TITLE, wcf);
		
		wcf = new WritableCellFormat(titleFont);
		wcf.setShrinkToFit(true);
		wcf.setAlignment(Alignment.CENTRE);
		cellFormats.put(CellFormatEnum.SHRINK_TITLE, wcf);
				
		wcf = new WritableCellFormat(warningFont);
		wcf.setWrap(true);
		wcf.setAlignment(Alignment.CENTRE);
		cellFormats.put(CellFormatEnum.WARNING, wcf);
		
		wcf = new WritableCellFormat(headerFont);
		wcf.setWrap(true);
		wcf.setAlignment(Alignment.CENTRE);
		wcf.setBackground(Colour.PALE_BLUE);
		wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
		cellFormats.put(CellFormatEnum.HEADER_PALE_BLUE, wcf);
		
		wcf = new WritableCellFormat(headerFont);
		wcf.setWrap(true);
		wcf.setAlignment(Alignment.CENTRE);
		wcf.setBackground(Colour.GREY_80_PERCENT);
		wcf.setFont(headerFont2);
		wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
		cellFormats.put(CellFormatEnum.HEADER_GREY, wcf);
		
		wcf = new WritableCellFormat(boldFont);
		cellFormats.put(CellFormatEnum.BOLD, wcf);
		
		wcf = new WritableCellFormat(headerFont);
		wcf.setWrap(true);
		wcf.setAlignment(Alignment.CENTRE);
		wcf.setBackground(Colour.SKY_BLUE);
		wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
		cellFormats.put(CellFormatEnum.HEADER_SKY_BLUE, wcf);
		
		cellFormats.put(CellFormatEnum.NORMAL, new WritableCellFormat());
		
		wcf = new WritableCellFormat();
		wcf.setBackground(Colour.GRAY_50);
		wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
		cellFormats.put(CellFormatEnum.GRAY_BACKGROUND, wcf);
		
		wcf = new WritableCellFormat();
		wcf.setBackground(Colour.PALE_BLUE);
		wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
		cellFormats.put(CellFormatEnum.PALE_BLUE_BACKGROUND, wcf);
		
		wcf = new WritableCellFormat();
		wcf.setWrap(true);
		cellFormats.put(CellFormatEnum.WRAP, wcf);
		
		wcf = new WritableCellFormat();
		wcf.setLocked(false);
		cellFormats.put(CellFormatEnum.UNLOCKED, wcf);
		
		wcf = new WritableCellFormat();
		wcf.setBackground(Colour.PALE_BLUE);
		wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
		cellFormats.put(CellFormatEnum.BORDER_PALE_BLUE, wcf);
		
		wcf = new WritableCellFormat();
		wcf.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE);
		cellFormats.put(CellFormatEnum.BOTTOM_DOUBLE_BORDER, wcf);
		
		wcf = new WritableCellFormat();
		wcf.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
		cellFormats.put(CellFormatEnum.RIGHT_BORDER, wcf);

		cellFormats.put(CellFormatEnum.FLOAT, new WritableCellFormat(NumberFormats.FLOAT));

		wcf = new WritableCellFormat(NumberFormats.FLOAT);
		wcf.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
		cellFormats.put(CellFormatEnum.FLOAT_RIGHT_BORDER, wcf);

		wcf = new WritableCellFormat(NumberFormats.INTEGER);
		cellFormats.put(CellFormatEnum.NUMBER, wcf);

		cellFormats.put(CellFormatEnum.DOLLARS, new WritableCellFormat(NumberFormats.ACCOUNTING_INTEGER));
		
		cellFormats.put(CellFormatEnum.PERCENT, new WritableCellFormat(NumberFormats.PERCENT_FLOAT));

		wcf = new WritableCellFormat(NumberFormats.ACCOUNTING_INTEGER);
		wcf.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
		cellFormats.put(CellFormatEnum.DOLLARS_RIGHT_BORDER, wcf);

		wcf = new WritableCellFormat();
		wcf.setBackground(Colour.IVORY);
		cellFormats.put(CellFormatEnum.IVORY, wcf);

		wcf = new WritableCellFormat();
		wcf.setBackground(Colour.IVORY);
		wcf.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
		cellFormats.put(CellFormatEnum.IVORY_RIGHT_BORDER, wcf);

		cellFormats.put(CellFormatEnum.DATE, new WritableCellFormat(new DateFormat("MM/dd/yyyy")));
	}
	
	protected enum CellFormatEnum{TITLE,SHRINK_TITLE,WARNING,GRAY_BACKGROUND,PALE_BLUE_BACKGROUND,HEADER_PALE_BLUE,HEADER_GREY,BOLD,HEADER_SKY_BLUE,NORMAL,UNLOCKED,BORDER_PALE_BLUE,BOTTOM_DOUBLE_BORDER,RIGHT_BORDER,NUMBER,PERCENT,FLOAT,FLOAT_RIGHT_BORDER,DOLLARS,DOLLARS_RIGHT_BORDER,IVORY,IVORY_RIGHT_BORDER,DATE,WRAP}
}
