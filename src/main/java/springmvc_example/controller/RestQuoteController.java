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

	@RequestMapping(value = "/quotation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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
		String  list = quotationService.listQuotation();
		return list;
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

	@RequestMapping(value = "/quotation/{mongoId}/owner/{ownerId}", method = RequestMethod.PATCH)
	public String updateInsured(@RequestBody String body, @PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "ownerId") String ownerId) throws JSONException {
		// Create JSONOject from json request
		JSONObject json_request = new JSONObject(body);
		ownerService.updateOwner(mongoId, ownerId, json_request);
		//Chua biet id_insured lay o dau
		//insuredService.updateInsured(mongoId, insuredId, json_request);

		return quotationService.infoQuotation(mongoId).toString();
	}

	/*----------------------Call webservice function F3POAgeCaculation-----------------------------*/

	@RequestMapping(value = "/quotation/{mongoId}/owner/{ownerId}/f3poagecaculation", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public String f3POAgeCaculation(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "ownerId") String onwerId) throws JSONException {
		JSONObject owner = ownerService.infoOwner(mongoId, onwerId);
		//String ageCaculation = owner;
		JSONArray array_owner = new JSONArray();
		array_owner.put(0, owner);
		JSONObject quote = new JSONObject();
		quote.put("productcode", "income-protection");
		quote.put("quotation_owner", array_owner);
		System.out.println(quote);
		
		String new_owner = quote.toString().replaceAll("\"", "\\\\\"");
		System.out.println(new_owner);
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
			String f3 = buf.toString().replaceFirst("\\$", Matcher.quoteReplacement(new_owner));
			System.out.println(f3);
			JSONArray arr = new JSONArray(f3);
			try {
				json = xeServerController.callXEServerVpms(arr);
				System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
			}
			resourceInputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return json;
	}
	/*----------------------Call webservice function F3_ModalPremium_Calculation-----------------------------*/
	@RequestMapping(value = "/quotation/{mongoId}/f3modalpremiumcalculation", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public String f3ModalPremiumCalculation(@PathVariable(value = "mongoId") String mongoId
			) throws JSONException {
		JSONObject quote = quotationService.infoQuotation(mongoId);
		//String ageCaculation = owner;
		
		System.out.println(quote);
		
		String new_owner = quote.toString().replaceAll("\"", "\\\\\"");
		System.out.println(new_owner);
		Resource resource = new ClassPathResource("request_F3ModalPremium.json");
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
			String f3 = buf.toString().replaceFirst("\\$", Matcher.quoteReplacement(new_owner));
			System.out.println(f3);
			JSONArray arr = new JSONArray(f3);
			try {
				json = xeServerController.callXEServerVpms(arr);
				System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
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

		return ownerService.infoOwner(mongoId, insuredId).toString();
	}

	/*
	 * @RequestMapping(value = "/json/{id}", method = RequestMethod.GET) public
	 * String infoJson(@PathVariable(value = "id") String id) { BasicDBObject query
	 * = new BasicDBObject(); query.put("value", id); DBCollection table =
	 * mongoTemplate.getCollection("user"); DBCursor cursor = table.find(query); if
	 * (cursor.hasNext()) { return "1"; }else return "0"; //
	 * System.out.println(cursor.next()); // return cursor.next().toString(); }
	 */
	@RequestMapping(value = "/quotation/{id}/coverage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String listCoverage(HttpServletRequest request, @PathVariable(value = "id") String id) {
		ArrayList list = coverageService.listCoverage(id);
		return list.toString();
	}

	@RequestMapping(value = "/quotation/{mongoId}/coverage/{coverageId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCoverageById(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "coverageId") String coverage_id) throws JSONException {

		return coverageService.getCoverageById(mongoId, coverage_id).toString();
	}

	@RequestMapping(value = "/quotation/{mongoId}/coverage/{coverageId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	public String update(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "coverageId") String coverage_id, @RequestBody String body) throws JSONException {
		JSONObject aa = new JSONObject(body);

		return coverageService.update(mongoId, coverage_id, aa).toString();
	}

}
