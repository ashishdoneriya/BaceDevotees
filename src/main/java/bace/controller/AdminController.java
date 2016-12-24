package bace.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bace.dao.DevoteeDao;
import bace.pojo.Devotee;
import static bace.utils.Constants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(APIS)
public class AdminController {

	private static final String ORDER2 = "order";

	private static final String SORT_BY = "sortBy";

	private static final String RECORDS = "records";

	private static final String PAGES = "pages";

	@Autowired
	DevoteeDao devoteeDao;

	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();

	@ResponseBody
	@RequestMapping(value = DEVOTEES, method = RequestMethod.GET)
	public String list(HttpServletRequest request) {
		String searchQuery = request.getParameter(SEARCH_QUERY);
		String pageNumber = request.getParameter(PAGE_NUMBER);
		String maximumResults = request.getParameter(MAXIMUM_RESULTS);
		String sortBy = request.getParameter(SORT_BY);
		String order = request.getParameter(ORDER2);
		List<Devotee> list = devoteeDao.list(searchQuery, pageNumber, maximumResults,
				sortBy, order);
		long numberOfPages = devoteeDao.getNumberOfPages(searchQuery);
		Map<String, Object> result = new HashMap<String, Object>(2);
		result.put(PAGES, numberOfPages);
		result.put(RECORDS, list);
		return gson.toJson(result);
	}
	
	@ResponseBody
	@RequestMapping(value = DEVOTEE_ID, method = RequestMethod.GET)
	public String get(@PathVariable(ID2) int id) {
		return gson.toJson(devoteeDao.get(id));
	}
	
	@ResponseBody
	@RequestMapping(value = SAVE, method = RequestMethod.POST)
	public String saveOrUpdate(@RequestBody Devotee devotee) {
		try {
			devoteeDao.save(devotee);
			return SUCCESS;
		} catch (Exception e) {
			return FAILED;
		}
	}
	
	@ResponseBody
	@RequestMapping(value = DEVOTEE_ID, method = RequestMethod.DELETE)
	public String delete(@PathVariable(ID2) int id) {
		try {
			devoteeDao.delete(id);
			return SUCCESS;
		} catch (Exception e) {
			return FAILED;
		}
	}
	
	

}
