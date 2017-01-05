package bace.controller;

import static bace.utils.Constants.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import bace.dao.DevoteeDao;
import bace.pojo.Devotee;
import bace.utils.ExcelSheet;

@Controller
@RequestMapping(APIS)
public class AdminController {

	private static final String UPLOAD = "/upload";

	private static final Logger LOG = 
			LogManager.getLogger(AdminController.class);

	@Autowired
	DevoteeDao devoteeDao;

	private static Gson gson;

	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

			@Override
			public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context)
					throws JsonParseException {
				try {
					return dateFormat.parse(json.getAsString());
				} catch (Exception e) {
					return null;
				}
			}
		});
		gson = gsonBuilder.setDateFormat(DATE_FORMAT).create();
	}

	@ResponseBody
	@RequestMapping(value = DEVOTEES, method = RequestMethod.GET)
	public String list(HttpServletRequest request) {
		String searchQuery = request.getParameter(SEARCH_QUERY);
		String pageNumber = request.getParameter(PAGE_NUMBER);
		String maximumResults = request.getParameter(MAXIMUM_RESULTS);
		String sortBy = request.getParameter(SORT_BY);
		String order = request.getParameter(ORDER2);
		List<Devotee> list = devoteeDao.list(searchQuery, pageNumber, maximumResults, sortBy, order);
		Map<String, Object> result = new HashMap<String, Object>(2);
		result.put(TOTAL_RESULTS, devoteeDao.getTotalResults(searchQuery));
		result.put(RECORDS, list);
		return gson.toJson(result);
	}

	@RequestMapping(value = DEVOTEES_DOWNLOAD, method = RequestMethod.GET)
	public void downloadExcel(HttpServletRequest request, HttpServletResponse response) {
		String searchQuery = request.getParameter(SEARCH_QUERY);
		String sortBy = request.getParameter(SORT_BY);
		String order = request.getParameter(ORDER2);
		String sColumnsList = request.getParameter(SELECTED_COLUMNS);
		try {
			List<String> selectedColumns;
			if (sColumnsList == null || sColumnsList.isEmpty()) {
				selectedColumns = new ArrayList<String>(10);
				selectedColumns.add("Name");
				selectedColumns.add("Mobile Number");
				selectedColumns.add("Email");
				selectedColumns.add("Father's Name");
				selectedColumns.add("Emergency Number");
				selectedColumns.add("Current Address");
				selectedColumns.add("Permanent Address");
				selectedColumns.add("Date of Birth");
				selectedColumns.add("Bace Join Date");
				selectedColumns.add("Bace Left Date");
			} else {
				Type type = new TypeToken<List<String>>() {
				}.getType();
				selectedColumns = gson.fromJson(sColumnsList, type);
			}
			List<Devotee> list = devoteeDao.list(searchQuery, null, null, sortBy, order);
			ExcelSheet sheet = new ExcelSheet();
			Workbook workbook = sheet.create(list, selectedColumns);

			OutputStream out = response.getOutputStream();
			response.setHeader("Content-Disposition", "attachment; filename=Devotee.xlsx");
			response.setContentType("application/xlsx");
			workbook.write(out);
			out.flush();
			out.close();
			// return "success";
		} catch (Exception e) {
			LOG.error("Error while downloading file", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			// return e.getMessage();
		}
	}

	@ResponseBody
	@RequestMapping(value = DEVOTEE_ID, method = RequestMethod.GET)
	public String get(@PathVariable(ID2) int id) {
		return gson.toJson(devoteeDao.get(id));
	}

	@ResponseBody
	@RequestMapping(value = SAVE, method = RequestMethod.POST)
	public String saveOrUpdate(@RequestBody String json, HttpServletResponse response) {
		try {
			devoteeDao.save(gson.fromJson(json, Devotee.class));
			return SUCCESS;
		} catch (Exception e) {
			LOG.error("Unable to save record, json = " + json, e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return e.getMessage();
		}
	}

	@ResponseBody
	@RequestMapping(value = DEVOTEE_ID, method = RequestMethod.DELETE)
	public String delete(@PathVariable(ID2) int id, HttpServletResponse response) {
		try {
			devoteeDao.delete(id);
			return SUCCESS;
		} catch (Exception e) {
			LOG.error("Unable to delete record, id = " + id, e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return e.getMessage();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = UPLOAD, method = RequestMethod.POST)
	public String upload(MultipartHttpServletRequest request, HttpServletResponse response) {
		
		try {
			MultipartFile file = request.getFile("file");
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			new ExcelSheet().extractData(workbook);
			return "success";
		} catch (Exception e) {
			LOG.error("Error while uploading file", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return e.getMessage();
		}
	}

}
