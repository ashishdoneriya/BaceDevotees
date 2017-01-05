package bace.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joestelmach.natty.Parser;

import bace.dao.DevoteeDao;
import bace.pojo.Devotee;

@Component
public class ExcelSheet {

	@Autowired
	DevoteeDao devoteeDao;

	public Workbook create(List<Devotee> devotees, List<String> columns) {
		Workbook workbook = new XSSFWorkbook();
		Sheet spreadsheet = workbook.createSheet("Devotees");
		Row row = spreadsheet.createRow(0);
		Cell cell;
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight((short) 12);
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

	public void extractData(Workbook workbook) {
		Map<Integer, String> map;
		Iterator<Sheet> sheetIterator = workbook.iterator();
		Iterator<Row> rowIterator;
		Iterator<Cell> cellIterator;
		Row row;
		Cell cell;
		String columnName;
		Devotee devotee;
		boolean toUpdate;
		Parser parser = new Parser();
		while (sheetIterator.hasNext()) {
			map = new HashMap<>();
			Sheet sheet = sheetIterator.next();
			rowIterator = sheet.iterator();
			if (!rowIterator.hasNext()) {
				continue;
			}
			row = rowIterator.next();
			cellIterator = row.iterator();
			int totalColumns = 0;
			while (cellIterator.hasNext()) {
				cell = cellIterator.next();
				map.put(totalColumns++, cell.getStringCellValue());
			}
			toUpdate = false;
			if (row.getCell(0).getStringCellValue().replaceAll("\\s+", "").equalsIgnoreCase("id")) {
				toUpdate = true;
			}
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				if (toUpdate) {
					devotee = devoteeDao.get((int) row.getCell(0).getNumericCellValue());
				} else {
					devotee = new Devotee();
				}

				for (int i = 0; i < totalColumns; i++) {
					columnName = map.get(i);
					cell = row.getCell(i);
					switch (columnName.toLowerCase().replaceAll("\\s+", "")) {
					case "name":
					case "fullname":
					case "devoteename":
					case "devotee'sname":
					case "completename":
						devotee.setName(cell.getStringCellValue());
						break;
					case "mobilenumber":
					case "mobile":
					case "phonenumber":
					case "phone":
					case "contactnumber":
					case "contact":
						devotee.setMobileNumber(cell.getStringCellValue());
						break;
					case "email":
					case "emailaddress":
					case "emailid":
						devotee.setEmail(cell.getStringCellValue());
						break;
					case "father'sname":
					case "fathername":
						devotee.setFatherName(cell.getStringCellValue());
						break;
					case "emergencynumber":
					case "emergencycontact":
					case "emergencycontactnumber":
						devotee.setEmergencyNumber(cell.getStringCellValue());
						break;
					case "currentaddress":
						devotee.setCurrentAddress(cell.getStringCellValue());
						break;
					case "permanentaddress":
					case "address":
					case "homeaddress":
					case "hometown":
						devotee.setPermanentAddress(cell.getStringCellValue());
						break;
					case "dateofbirth":
					case "dob":
					case "birthdate":
						devotee.setDob(parser.parse(cell.getStringCellValue()).get(0).getDates().get(0));
						break;
					case "bacejoindate":
					case "bacejoiningdate":
					case "joiningdate":
					case "joindate":
					case "join":
					case "bacejoin":
					case "bacejoining":
						devotee.setBaceJoinDate(parser.parse(cell.getStringCellValue()).get(0).getDates().get(0));
						break;
					case "baceleftdate":
					case "leftdate":
					case "left":
					case "baceleft":
						devotee.setBaceLeftDate(parser.parse(cell.getStringCellValue()).get(0).getDates().get(0));
						break;
					}
				}
				devoteeDao.save(devotee);
			}
		}
	}

	private String checkNull(String object) {
		if (object == null) {
			return Constants.EMPTY_STRING;
		}
		return object;
	}
}
