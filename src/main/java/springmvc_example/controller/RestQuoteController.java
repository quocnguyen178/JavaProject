package springmvc_example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import springmvc_example.service.CoverageService;
import springmvc_example.service.InsuredService;
import springmvc_example.service.OwnerService;
import springmvc_example.service.QuotationService;
import springmvc_example.xeservervpms.XeServerController;

@RestController
@RequestMapping(value = "/omni-new-business-services/omni/service")
public class RestQuoteController {

	@Autowired
	QuotationService quotationService;
	@Autowired
	InsuredService insuredService;
	@Autowired
	CoverageService coverageService;
	@Autowired
	OwnerService ownerService;
	@Autowired
	MongoTemplate mongoTemplate;

	private static final String COLLECTION_NAME = "user";

	private static Logger logger = Logger.getLogger(RestQuoteController.class);

	XeServerController xeServerController = new XeServerController();

	/*
	 * // REST
	 * 
	 * @RequestMapping(value = "/list-user", method = RequestMethod.GET) public
	 * List<User> listUser(HttpServletRequest request) { List<User> user =
	 * userService.listUser(); return user; }
	 * 
	 * @RequestMapping(value = "/info/{id}", method = RequestMethod.GET) public User
	 * infoUser(@PathVariable(value = "id") String id) { return
	 * userService.findUserById(id); }
	 * 
	 * @RequestMapping(value = "/save-user", method = RequestMethod.POST)
	 * 
	 * @ResponseStatus(value = HttpStatus.CREATED) public void saveUser(@RequestBody
	 * User user) { userService.add(user);
	 * 
	 * }
	 */

	@RequestMapping(value = "/quotation", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public String createQuotation(@RequestBody String body) throws JSONException {
		JSONObject json_body = new JSONObject(body);
		Resource resource = new ClassPathResource("document.json");
		InputStream resourceInputStream = null;
		String quotationReturn = "";
		String str = "";
		StringBuffer buf = new StringBuffer();
		try {
			resourceInputStream = resource.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(resourceInputStream));
			if (resourceInputStream != null) {
				while ((str = reader.readLine()) != null) {
					buf.append(str);
				}
			}
			JSONObject document = new JSONObject(buf.toString());
			quotationReturn = quotationService.createQuotation(document);

			resourceInputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return quotationReturn;
	}

	@RequestMapping(value = "/list-quotation", method = RequestMethod.GET)
	public String listQuotation(HttpServletRequest request) {
		ArrayList list = quotationService.listQuotation();
		return list.toString();
	}

	@RequestMapping(value = "/quotation/{id}/insured", method = RequestMethod.GET)
	public String listInsured(HttpServletRequest request, @PathVariable(value = "id") String id) {
		ArrayList list = insuredService.listInsured(id);
		return list.toString();
	}

	@RequestMapping(value = "/quotation/{mongoId}/insured/{insuredId}", method = RequestMethod.GET)
	public String infoInsured(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "insuredId") String insuredId) {

		return insuredService.infoInsured(mongoId, insuredId).toString();
	}

	@RequestMapping(value = "/quotation/{mongoId}/insured/{insuredId}", method = RequestMethod.PATCH)
	public JSONObject updateInsured(@RequestBody String body, @PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "insuredId") String insuredId)
			throws JSONException {
		JSONObject json_update = insuredService.infoInsured(mongoId,insuredId);
		JSONObject json_request = new JSONObject(body);
		/************************************************/
		json_update.put("insured_gender", json_request.getJSONObject("insured_gender"));
		json_update.put("insured_residence_state", json_request.getJSONObject("insured_residence_state"));
		json_update.put("insured_date_of_birth", json_request.getString("insured_date_of_birth"));
		json_update.put("insured_occupation", json_request.getJSONObject("insured_occupation"));
		json_update.put("insured_annual_income", json_request.getJSONObject("insured_annual_income"));
		json_update.put("insured_given_name", json_request.getString("insured_given_name"));
		json_update.put("insured_last_name", json_request.getString("insured_last_name"));
		json_update.put("insured_annual_income", json_request.getJSONObject("insured_annual_income"));
		/******************************************************/
		System.out.println("json update");
		System.out.println(json_update.getJSONObject("insured_gender"));
		System.out.println(json_update.getJSONObject("insured_residence_state"));
		System.out.println(json_update.getString("insured_date_of_birth"));
		System.out.println(json_update.getJSONObject("insured_occupation"));
		System.out.println(json_update.getJSONObject("insured_annual_income"));
		System.out.println(json_update.getString("insured_given_name"));
		System.out.println(json_update.getString("insured_last_name"));
		System.out.println(json_update.getJSONObject("insured_annual_income"));
		/**********************************************************/
		JSONObject js;
		JSONObject js1;
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", insuredId);
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
		JSONObject objectInfo = new JSONObject();
		while (cursor.hasNext()) {
			try {
				js = new JSONObject(cursor.next().toString());
				JSONArray storeList = js.getJSONArray("quotation_insured");
				String _id = "";
				for (int i = 0; i < storeList.length(); i++) {
					JSONObject info = storeList.getJSONObject(i);

					_id = info.getString("_id");
					if (insuredId.equals(_id)) {
						objectInfo = info;
						js1 = js;
						js1.put("quotation_insured", json_update);
						BasicDBObject a = new BasicDBObject();
						a.put("business_case_id", "");
						DBObject s = (DBObject) JSON.parse(js1.toString());
						// mongoTemplate.getCollection(COLLECTION_NAME).findAndModify(query, update);
						System.out.println(js1.toString());
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/*----------------------Call webservice function F3POAgeCaculation-----------------------------*/

	@RequestMapping(value = "/quotation/{mongoId}/insured/{insuredId}/f3poagecaculation", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public String f3POAgeCaculation(@PathVariable(value = "mongoId") String mongoId,@PathVariable(value = "mongoId") String insuredId) throws JSONException {
		String insured = insuredService.infoInsured(mongoId,insuredId).toString();
		String new_insured = insured.replaceAll("\"", "\\\\\"");
		System.out.println(new_insured);
		Resource resource = new ClassPathResource("request_F3POAge.json");
		InputStream resourceInputStream = null;
		String str = "";
		String json = "";
		StringBuffer buf = new StringBuffer();
		try {
			resourceInputStream = resource.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(resourceInputStream));
			if (resourceInputStream != null) {
				while ((str = reader.readLine()) != null) {
					buf.append(str);
				}
			}
			String f3 = buf.toString().replaceFirst("\\$", Matcher.quoteReplacement(new_insured));
			System.out.println(f3);
			JSONArray arr = new JSONArray(f3);
			try {
				json = xeServerController.callXEServerVpms(arr);
				System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
//				logger.info("Thu" + e.getMessage());
			}
			resourceInputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/quotation/{id}/owner", method = RequestMethod.GET)
	public String listOwner(HttpServletRequest request, @PathVariable(value = "id") String id) {
		ArrayList list = ownerService.listOwner(id);
		return list.toString();
	}

	@RequestMapping(value = "/quotation/{mongoId}/owner/{insuredId}", method = RequestMethod.GET)
	public String infoOwner(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "insuredId") String insuredId) {

		return ownerService.infoOwner(mongoId,insuredId).toString();
	}

	/*
	 * @RequestMapping(value = "/json/{id}", method = RequestMethod.GET) public
	 * String infoJson(@PathVariable(value = "id") String id) { BasicDBObject query
	 * = new BasicDBObject(); query.put("value", id); DBCollection table =
	 * mongoTemplate.getCollection("user"); DBCursor cursor = table.find(query); if
	 * (cursor.hasNext()) { return "1"; }else return "0"; //
	 * System.out.println(cursor.next()); // return cursor.next().toString(); }
	 */
	@RequestMapping(value = "/quotation/{id}/coverage", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public String listCoverage(HttpServletRequest request, @PathVariable(value = "id") String id) {
		ArrayList list = coverageService.listCoverage(id);
		return list.toString();
	}
	
	@RequestMapping(value = "/quotation/{mongoId}/coverage/{coverageId}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCoverageById(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "coverageId") String coverage_id) throws JSONException {

		return coverageService.getCoverageById(mongoId,coverage_id).toString();
	}
	
	@RequestMapping(value = "/quotation/{mongoId}/coverage/{coverageId}", method = RequestMethod.PATCH,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public String update(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "coverageId") String coverage_id,
			@RequestBody String body) throws JSONException {
		JSONObject aa = new JSONObject(body);

		return coverageService.update(mongoId, coverage_id, aa).toString();
	}
	
}
