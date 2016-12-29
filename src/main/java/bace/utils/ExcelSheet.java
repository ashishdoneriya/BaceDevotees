package bace.utils;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import bace.pojo.Devotee;

public class ExcelSheet {

	public Workbook create(List<Devotee> devotees, List<String> columns) {
		Workbook workbook = new XSSFWorkbook();
		Sheet spreadsheet = workbook.createSheet("Devotees");
		Row row = spreadsheet.createRow(1);
		Cell cell;
		cell = row.createCell(1);
		int i = 0;
		for (String columnName : columns) {
			cell = row.createCell(++i);
			cell.setCellValue(columnName);
		}

		int rowNumber = 2;
		for (Devotee devotee : devotees) {
			row = spreadsheet.createRow(rowNumber++);
			int columnNumber = 0;
			for (String columnName : columns) {
				cell = row.createCell(++columnNumber);
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
						cell.setCellValue(devotee.getDob());
					}
					break;
				case "Bace Join Date": 
					if (devotee.getBaceJoinDate() != null) {
						cell.setCellValue(devotee.getBaceJoinDate());
					}
					break;
				case "Bace Left Date": 
					if (devotee.getBaceLeftDate() != null) {
						cell.setCellValue(devotee.getBaceLeftDate());
					}
					break;
				}
				
			}

		}
		return workbook;
	}

	private String checkNull(String object) {
		if (object == null) {
			return "";
		}
		return object;
	}
}
