package bace.controller;

import static bace.utils.Constants.APIS;
import static bace.utils.Constants.DEVOTEES;
import static bace.utils.Constants.DEVOTEE_ID;
import static bace.utils.Constants.FAILED;
import static bace.utils.Constants.ID2;
import static bace.utils.Constants.MAXIMUM_RESULTS;
import static bace.utils.Constants.PAGE_NUMBER;
import static bace.utils.Constants.SAVE;
import static bace.utils.Constants.SEARCH_QUERY;
import static bace.utils.Constants.SUCCESS;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import bace.dao.DevoteeDao;
import bace.pojo.Devotee;

@Controller
@RequestMapping(APIS)
public class AdminController {

	private static final String ORDER2 = "order";

	private static final String SORT_BY = "sortBy";

	private static final String RECORDS = "records";

	private static final String PAGES = "pages";

	private static final Logger LOG = LogManager.getLogger(AdminController.class);

	@Autowired
	DevoteeDao devoteeDao;

	private static Gson gson;

	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

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
		gson = gsonBuilder.setDateFormat("yyyy-mm-dd").create();
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
		long numberOfPages;
		if (maximumResults == null || maximumResults.isEmpty()) {
			numberOfPages = 1;
		} else {
			long totalResults = devoteeDao.getTotalResults(searchQuery);
			long maxResults = Long.parseLong(maximumResults);
			numberOfPages = totalResults / maxResults;
			if (totalResults % maxResults > 0) {
				numberOfPages ++;
			}
		}
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
	public String saveOrUpdate(@RequestBody String json) {
		try {
			devoteeDao.save(gson.fromJson(json, Devotee.class));
			return SUCCESS;
		} catch (Exception e) {
			LOG.error("Unable to save record, json = " + json, e);
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
			LOG.error("Unable to delete record, id = " + id, e);
			return FAILED;
		}
	}

}
