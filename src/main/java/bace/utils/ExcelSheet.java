package bace.utils;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import bace.pojo.Devotee;

public class ExcelSheet {

	public Workbook create(List<Devotee> devotees, List<String> columns) {
		Workbook workbook = new XSSFWorkbook();
		Sheet spreadsheet = workbook.createSheet("Devotees");
		Row row = spreadsheet.createRow(0);
		Cell cell;
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight( (short) 12 );
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		int i = 0;
		for (String columnName : columns) {
			cell = row.createCell(i++);
			cell.setCellValue(columnName);
			cell.setCellStyle(style);
		}

		SimpleDateFormat formatter = new SimpleDateFormat(Constants.EXCEL_DATE_FORMAT);
		int rowNumber = 1;
		for (Devotee devotee : devotees) {
			row = spreadsheet.createRow(rowNumber++);
			int columnNumber = 0;
			for (String columnName : columns) {
				cell = row.createCell(columnNumber++);
				switch (columnName) {
				case "Name":
					cell.setCellValue(checkNull(devotee.getName()));
					break;
				case "Mobile Number":
					cell.setCellValue(checkNull(devotee.getMobileNumber()));
					break;
				case "Email":
					cell.setCellValue(checkNull(devotee.getEmail()));
					break;
				case "Father's Name":
					cell.setCellValue(checkNull(devotee.getFatherName()));
					break;
				case "Emergency Number":
					cell.setCellValue(checkNull(devotee.getEmergencyNumber()));
					break;
				case "Current Address":
					cell.setCellValue(checkNull(devotee.getCurrentAddress()));
					break;
				case "Permanent Address":
					cell.setCellValue(checkNull(devotee.getPermanentAddress()));
					break;
				case "Date of Birth":
					if (devotee.getDob() != null) {
						cell.setCellValue(formatter.format(devotee.getDob()));
					}
					break;
				case "Bace Join Date": 
					if (devotee.getBaceJoinDate() != null) {
						cell.setCellValue(formatter.format(devotee.getBaceJoinDate()));
					}
					break;
				case "Bace Left Date": 
					if (devotee.getBaceLeftDate() != null) {
						cell.setCellValue(formatter.format(devotee.getBaceLeftDate()));
					}
					break;
				}
			}
		}
		for (i = 0; i < columns.size(); i++) {
			spreadsheet.autoSizeColumn(i);
		}
		return workbook;
	}

	private String checkNull(String object) {
		if (object == null) {
			return Constants.EMPTY_STRING;
		}
		return object;
	}
}
