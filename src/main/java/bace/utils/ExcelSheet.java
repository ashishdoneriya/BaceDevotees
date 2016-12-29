package bace.utils;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import bace.pojo.Devotee;

public class ExcelSheet {
	
	public void create(List<Devotee> devotee) {
		Workbook workbook = new XSSFWorkbook();
		Sheet spreadsheet =  workbook.createSheet("Devotees");
		Row row = spreadsheet.createRow(1);
		Cell cell = row.createCell(1);
		cell.setCellValue("Name");
	}
}
