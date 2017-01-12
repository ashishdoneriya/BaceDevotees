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

	private static final String BACELEFT = "baceleft";
	private static final String LEFT = "left";
	private static final String LEFTDATE = "leftdate";
	private static final String BACELEFTDATE = "baceleftdate";
	private static final String BACEJOINING = "bacejoining";
	private static final String BACEJOIN = "bacejoin";
	private static final String JOIN = "join";
	private static final String JOINDATE = "joindate";
	private static final String JOININGDATE = "joiningdate";
	private static final String BACEJOININGDATE = "bacejoiningdate";
	private static final String BACEJOINDATE = "bacejoindate";
	private static final String BIRTHDATE = "birthdate";
	private static final String DOB = "dob";
	private static final String DATEOFBIRTH = "dateofbirth";
	private static final String HOMETOWN = "hometown";
	private static final String HOMEADDRESS = "homeaddress";
	private static final String ADDRESS = "address";
	private static final String PERMANENTADDRESS = "permanentaddress";
	private static final String CURRENTADDRESS = "currentaddress";
	private static final String EMERGENCYCONTACTNUMBER = "emergencycontactnumber";
	private static final String EMERGENCYCONTACT = "emergencycontact";
	private static final String EMERGENCYNUMBER = "emergencynumber";
	private static final String FATHERNAME = "fathername";
	private static final String FATHER_SNAME = "father'sname";
	private static final String EMAILID = "emailid";
	private static final String EMAILADDRESS = "emailaddress";
	private static final String EMAIL2 = "email";
	private static final String CONTACT = "contact";
	private static final String CONTACTNUMBER = "contactnumber";
	private static final String PHONE = "phone";
	private static final String PHONENUMBER = "phonenumber";
	private static final String MOBILE = "mobile";
	private static final String MOBILENUMBER = "mobilenumber";
	private static final String COMPLETENAME = "completename";
	private static final String DEVOTEE_SNAME = "devotee'sname";
	private static final String DEVOTEENAME = "devoteename";
	private static final String FULLNAME = "fullname";
	private static final String REPLACEMENT = "";
	private static final String ID = "id";
	private static final String S = "\\s+";
	private static final String NAME2 = "name";
	private static final String BACE_LEFT_DATE = "Bace Left Date";
	private static final String BACE_JOIN_DATE = "Bace Join Date";
	private static final String DATE_OF_BIRTH = "Date of Birth";
	private static final String PERMANENT_ADDRESS = "Permanent Address";
	private static final String CURRENT_ADDRESS = "Current Address";
	private static final String EMERGENCY_NUMBER = "Emergency Number";
	private static final String FATHER_S_NAME = "Father's Name";
	private static final String EMAIL = "Email";
	private static final String MOBILE_NUMBER = "Mobile Number";
	private static final String NAME = "Name";
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
				case NAME:
					cell.setCellValue(checkNull(devotee.getName()));
					break;
				case MOBILE_NUMBER:
					cell.setCellValue(checkNull(devotee.getMobileNumber()));
					break;
				case EMAIL:
					cell.setCellValue(checkNull(devotee.getEmail()));
					break;
				case FATHER_S_NAME:
					cell.setCellValue(checkNull(devotee.getFatherName()));
					break;
				case EMERGENCY_NUMBER:
					cell.setCellValue(checkNull(devotee.getEmergencyNumber()));
					break;
				case CURRENT_ADDRESS:
					cell.setCellValue(checkNull(devotee.getCurrentAddress()));
					break;
				case PERMANENT_ADDRESS:
					cell.setCellValue(checkNull(devotee.getPermanentAddress()));
					break;
				case DATE_OF_BIRTH:
					if (devotee.getDob() != null) {
						cell.setCellValue(formatter.format(devotee.getDob()));
					}
					break;
				case BACE_JOIN_DATE:
					if (devotee.getBaceJoinDate() != null) {
						cell.setCellValue(formatter.format(devotee.getBaceJoinDate()));
					}
					break;
				case BACE_LEFT_DATE:
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
			if (row.getCell(0).getStringCellValue().replaceAll(S, REPLACEMENT).equalsIgnoreCase(ID)) {
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
					switch (columnName.toLowerCase().replaceAll(S, REPLACEMENT)) {
					case NAME2:
					case FULLNAME:
					case DEVOTEENAME:
					case DEVOTEE_SNAME:
					case COMPLETENAME:
						devotee.setName(cell.getStringCellValue());
						break;
					case MOBILENUMBER:
					case MOBILE:
					case PHONENUMBER:
					case PHONE:
					case CONTACTNUMBER:
					case CONTACT:
						devotee.setMobileNumber(cell.getStringCellValue());
						break;
					case EMAIL2:
					case EMAILADDRESS:
					case EMAILID:
						devotee.setEmail(cell.getStringCellValue());
						break;
					case FATHER_SNAME:
					case FATHERNAME:
						devotee.setFatherName(cell.getStringCellValue());
						break;
					case EMERGENCYNUMBER:
					case EMERGENCYCONTACT:
					case EMERGENCYCONTACTNUMBER:
						devotee.setEmergencyNumber(cell.getStringCellValue());
						break;
					case CURRENTADDRESS:
						devotee.setCurrentAddress(cell.getStringCellValue());
						break;
					case PERMANENTADDRESS:
					case ADDRESS:
					case HOMEADDRESS:
					case HOMETOWN:
						devotee.setPermanentAddress(cell.getStringCellValue());
						break;
					case DATEOFBIRTH:
					case DOB:
					case BIRTHDATE:
						devotee.setDob(parser.parse(cell.getStringCellValue()).get(0).getDates().get(0));
						break;
					case BACEJOINDATE:
					case BACEJOININGDATE:
					case JOININGDATE:
					case JOINDATE:
					case JOIN:
					case BACEJOIN:
					case BACEJOINING:
						devotee.setBaceJoinDate(parser.parse(cell.getStringCellValue()).get(0).getDates().get(0));
						break;
					case BACELEFTDATE:
					case LEFTDATE:
					case LEFT:
					case BACELEFT:
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
