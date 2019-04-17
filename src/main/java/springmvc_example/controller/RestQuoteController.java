package springmvc_example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.KebabCaseStrategy;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

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
			JSONObject jsonObject1 = new JSONObject(quotationReturn);
			JSONObject temp1 = new JSONObject();
			temp1.put("method", "POST");
			temp1.put("rel", "Create");
			temp1.put("mediaType", "application/json");
			temp1.put("href", "http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation");
			JSONArray jsonArray = new JSONArray();
			JSONObject temp2 = new JSONObject();
			temp2.put("method", "GET");
			temp2.put("rel", "Search");
			temp2.put("mediaType", "application/json");
			temp2.put("href", "http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation");
			JSONObject temp3 = new JSONObject();
			JSONObject jsonOject2 = new JSONObject();
			jsonOject2.put("href", "http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/$oid?");
			temp3.put("_nextlink", jsonOject2);
			jsonArray.put(temp1).put(temp2).put(temp3);
			jsonObject1.put("_options",jsonArray);
			quotationReturn = jsonObject1.toString();
			resourceInputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return quotationReturn;
	}

	@RequestMapping(value = "/quotation", method = RequestMethod.GET)
	public String listQuotation(HttpServletRequest request)throws JSONException {
		String  list = quotationService.listQuotation();
		JSONArray jsonArray1 = new JSONArray(list);
		JSONObject temp1 = new JSONObject();
		temp1.put("method", "POST");
		temp1.put("rel", "Create");
		temp1.put("mediaType", "application/json");
		temp1.put("self",new JSONObject().put("href", "http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation"));
		JSONArray jsonArray2 = new JSONArray();
		JSONObject temp2 = new JSONObject();
		temp2.put("method", "GET");
		temp2.put("rel", "Search");
		temp2.put("mediaType", "application/json");
		temp2.put("self",new JSONObject().put("href", "http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation"));
		JSONObject temp3 = new JSONObject();
		JSONObject jsonOject2 = new JSONObject();
		jsonOject2.put("href", "http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/$oid?");
		temp3.put("_nextlink", jsonOject2);
		jsonArray2.put(temp1).put(temp2).put(temp3);
		JSONObject jsonObject3 = new JSONObject().put("_options",jsonArray2);
		jsonArray1.put(jsonObject3);
		list = jsonArray1.toString();
		return list;
	}
	@RequestMapping(value = "/quotation/{quotationId}", method = RequestMethod.GET)
	public String infoQuotation(HttpServletRequest request, @PathVariable(value = "quotationId") String quotationId) throws JSONException {
		String quotationReturn = quotationService.infoQuotation(quotationId).toString();
		JSONObject jsonObject1 = new JSONObject(quotationReturn);
		JSONObject temp1 = new JSONObject();
		temp1.put("method", "POST");
		temp1.put("rel", "Create");
		temp1.put("mediaType", "application/json");
		temp1.put("self",new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+quotationId));
		
		JSONArray jsonArray = new JSONArray();
		JSONArray temp3 = new JSONArray();
		JSONObject jsonObject2 = new JSONObject();
		JSONObject jsonObject3 = new JSONObject();
		JSONObject jsonObject4 = new JSONObject();
		JSONObject jsonObject5 = new JSONObject();
		jsonObject2.put("owner", new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+quotationId+"/owner"));
		jsonObject3.put("insured",new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+quotationId+"/insured"));
		jsonObject4.put("coverage", new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+quotationId+"/coverage"));
		
		temp3.put(jsonObject2).put(jsonObject3).put(jsonObject4);
		jsonObject5.put("_nextlink", temp3);
		jsonArray.put(temp1).put(jsonObject5);
		jsonObject1.put("_options",jsonArray);
		quotationReturn = jsonObject1.toString();
		
		return quotationReturn;
	}

	@RequestMapping(value = "/quotation/{id}/insured", method = RequestMethod.GET)
	public String listInsured(HttpServletRequest request, @PathVariable(value = "id") String id) throws JSONException{
		ArrayList arrayList = insuredService.listInsured(id);
		String stringList = arrayList.toString();
		JSONArray jsonArray1 = new JSONArray(stringList);
		JSONArray jsonArray2 = new JSONArray();
		JSONObject temp2 = new JSONObject();
		temp2.put("method", "GET");
		temp2.put("rel", "Search insured by Id");
		temp2.put("mediaType", "application/json");
		temp2.put("self",new JSONObject().put("href", "http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+id+"/insured"));
		JSONObject temp3 = new JSONObject();
		JSONObject jsonOject2 = new JSONObject();
		jsonOject2.put("href", "http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+id+"/insured/in?");
		temp3.put("_nextlink", jsonOject2);
		jsonArray2.put(temp2).put(temp3);
		JSONObject jsonObject3 = new JSONObject().put("_options",jsonArray2);
		jsonArray1.put(jsonObject3);
		return jsonArray1.toString();
	}

	@RequestMapping(value = "/quotation/{mongoId}/insured/{insuredId}", method = RequestMethod.GET)
	public String infoInsured(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "insuredId") String insuredId)throws JSONException {

		String insuredReturn = insuredService.infoInsured(mongoId, insuredId).toString();
		JSONObject jsonObject1 = new JSONObject(insuredReturn);
		JSONObject temp1 = new JSONObject();
		temp1.put("method", "GET");
		temp1.put("rel", "Search insured by Id");
		temp1.put("mediaType", "application/json");
		temp1.put("self",new JSONObject().put("href", "http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+mongoId+"/insured/"+insuredId));
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(temp1);
		jsonObject1.put("_options",jsonArray);
		insuredReturn = jsonObject1.toString();
		return insuredReturn;
	}

	@RequestMapping(value = "/quotation/{mongoId}/insured/{insuredId}/owner/{ownerId}", method = RequestMethod.PATCH)
	public String updateInsured(@RequestBody String body, @PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "insuredId") String insuredId,
			@PathVariable(value = "ownerId") String ownerId) throws JSONException {
		// Create JSONOject from json request
		JSONObject json_request = new JSONObject(body);
		ownerService.updateOwner(mongoId, ownerId, json_request);
		String json_age = f3POAgeCaculation(mongoId, ownerId);
		
		ownerService.updateAgeOwner(mongoId, ownerId, json_age);
		
		insuredService.updateInsured(mongoId, insuredId, json_request);

		String ownerReturn = ownerService.infoOwner(mongoId, ownerId).toString();
		JSONObject jsonObject1 = new JSONObject(ownerReturn);

		JSONObject temp1 = new JSONObject();
		temp1.put("method", "PATCH");
		temp1.put("rel", "Update owner by Id");
		temp1.put("mediaType", "application/json");
		temp1.put("self",new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+mongoId+"/insured/"+insuredId+"/owner/"+ownerId));
	
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(temp1);
		jsonObject1.put("_options",jsonArray);
		ownerReturn = jsonObject1.toString();
		return ownerReturn;
		
	}

	/*----------------------Call webservice function F3POAgeCaculation-----------------------------*/

	@RequestMapping(value = "/quotation/{mongoId}/owner/{ownerId}/f3poagecaculation", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public String f3POAgeCaculation(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "ownerId") String ownerId) throws JSONException {
		JSONObject owner = ownerService.infoOwner(mongoId, ownerId);
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
	public String listOwner(HttpServletRequest request, @PathVariable(value = "id") String id) throws JSONException{
		ArrayList arrayList = ownerService.listOwner(id);
		String stringList = arrayList.toString();
		JSONArray jsonArray1 = new JSONArray(stringList);
		JSONArray jsonArray2 = new JSONArray();
		JSONObject temp1 = new JSONObject();
		temp1.put("method", "GET");
		temp1.put("rel", "Search insured by Id");
		temp1.put("mediaType", "application/json");
		temp1.put("self",new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+id+"/owner"));
		JSONObject temp2 = new JSONObject();
		JSONObject jsonOject2 = new JSONObject();
		jsonOject2.put("href", "http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+id+"/owner/ow?");
		temp2.put("_nextlink", jsonOject2);
		jsonArray2.put(temp1).put(temp2);
		JSONObject jsonObject3 = new JSONObject().put("_options",jsonArray2);
		jsonArray1.put(jsonObject3);
		return jsonArray1.toString();	
	}

	@RequestMapping(value = "/quotation/{mongoId}/owner/{ownerId}", method = RequestMethod.GET)
	public String infoOwner(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "ownerId") String ownerId)throws JSONException {

		String ownerReturn = ownerService.infoOwner(mongoId, ownerId).toString();
		JSONObject jsonObject1 = new JSONObject(ownerReturn);
		JSONObject temp1 = new JSONObject();
		temp1.put("method", "GET");
		temp1.put("rel", "Search owner by Id");
		temp1.put("mediaType", "application/json");
		temp1.put("self",new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+mongoId+"/owner/"+ownerId));
		JSONObject temp2 = new JSONObject();
		temp2.put("method", "PATCH");
		temp2.put("rel", "Update owner by Id");
		temp2.put("mediaType", "application/json");
		temp2.put("self",new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+mongoId+"/owner/"+ownerId));
	
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(temp1).put(temp2);
		jsonObject1.put("_options",jsonArray);
		ownerReturn = jsonObject1.toString();
		return ownerReturn;
	}

		@RequestMapping(value = "/quotation/{id}/coverage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String listCoverage(HttpServletRequest request, @PathVariable(value = "id") String id) throws JSONException {
			ArrayList arrayList = coverageService.listCoverage(id);
			String stringList = arrayList.toString();
			JSONArray jsonArray1 = new JSONArray(stringList);
			JSONArray jsonArray2 = new JSONArray();
			JSONObject temp1 = new JSONObject();
			temp1.put("method", "GET");
			temp1.put("rel", "Search all coverage");
			temp1.put("mediaType", "application/json");
			temp1.put("self",new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+id+"/coverage"));
			JSONObject temp2 = new JSONObject();
			JSONObject jsonOject2 = new JSONObject();
			jsonOject2.put("href", "http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+id+"/owner/co?");
			temp2.put("_nextlink", jsonOject2);
			jsonArray2.put(temp1).put(temp2);
			JSONObject jsonObject3 = new JSONObject().put("_options",jsonArray2);
			jsonArray1.put(jsonObject3);
			return jsonArray1.toString();
	}

	@RequestMapping(value = "/quotation/{mongoId}/coverage/{coverageId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCoverageById(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "coverageId") String coverage_id) throws JSONException {
			String coverageReturn = coverageService.getCoverageById(mongoId, coverage_id).toString();
			JSONObject jsonObject1 = new JSONObject(coverageReturn);
			JSONObject temp1 = new JSONObject();
			temp1.put("method", "GET");
			temp1.put("rel", "Search coverage by Id");
			temp1.put("mediaType", "application/json");
			temp1.put("self",new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/$oid?/coverage/"+coverage_id));
			JSONObject temp2 = new JSONObject();
			temp2.put("method", "PATCH");
			temp2.put("rel", "Update coverage by Id");
			temp2.put("mediaType", "application/json");
			temp2.put("self",new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+mongoId+"/coverage/"+coverage_id));
		
			
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(temp1).put(temp2);
			jsonObject1.put("_options",jsonArray);
			coverageReturn = jsonObject1.toString();
			return coverageReturn;
	}

	@RequestMapping(value = "/quotation/{mongoId}/coverage/{coverageId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	public String update(@PathVariable(value = "mongoId") String mongoId,
			@PathVariable(value = "coverageId") String coverage_id, @RequestBody String body) throws JSONException {
		
		JSONObject rs = new JSONObject( getCoverageById(mongoId, coverage_id));
		JSONObject temp1 = new JSONObject();
		temp1.put("method", "GET");
		temp1.put("rel", "Search coverage by Id");
		temp1.put("mediaType", "application/json");
		temp1.put("self",new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+mongoId+"/coverage/"+coverage_id));
		JSONObject temp2 = new JSONObject();
		temp2.put("method", "PATCH");
		temp2.put("rel", "Update coverage by Id");
		temp2.put("mediaType", "application/json");
		temp2.put("self",new JSONObject().put("href","http://localhost:8080/SpringMvcMongodbExample/omni-new-business-services/omni/service/quotation/"+mongoId+"/coverage/"+coverage_id));

		JSONArray jsonArray = new JSONArray();
		jsonArray.put(temp1).put(temp2);
		rs.put("_options",jsonArray);
		
		JSONObject aa = new JSONObject(body);
		coverageService.update(mongoId, coverage_id, aa);
		
		String tamStr = f3ModalPremiumCalculation(mongoId);
		JSONObject joStr = new JSONObject(tamStr);
		//System.out.println("JOSTR_____"+joStr);
		String result = "";
		if(joStr.get("modal_premium") instanceof JSONObject) {
			JSONObject joStr1 = (JSONObject) joStr.get("modal_premium");
			if(joStr1.get("enum") instanceof JSONArray) {
				JSONArray jArrStr =(JSONArray) joStr1.get("enum");
				JSONObject joStr2 = jArrStr.getJSONObject(0);
				result = (String) joStr2.get("key");
			}
		}
		//System.out.println("KEY___"+result);
		
		JSONObject js;
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
		while(cursor.hasNext()) {
			js = new JSONObject(cursor.next().toString());
			JSONObject _id = js.getJSONObject("_id");
			String stringID = (String) _id.get("$oid");	
			if(stringID.equals(mongoId)) {
				String subId =(String) js.get("sub_id");
				if(js.get("quotation_coverage") instanceof JSONArray) {
					JSONArray jarray = (JSONArray) js.get("quotation_coverage");
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject jo = jarray.getJSONObject(i);
						if(jo.get("_id").equals(coverage_id)) {
							BasicDBObject query = new BasicDBObject("sub_id",subId);
							//query.put("quotation_coverage._id", coverage_id);
							BasicDBObject updateObject = new BasicDBObject();
							//updateObject.append("$set",new BasicDBObject("quotation_coverage.$.coverage_premium_type.value",result));
							updateObject.append("$set",new BasicDBObject("quote_premium_frequency.value",result));
							mongoTemplate.getCollection(COLLECTION_NAME).update(query, updateObject);
						}
					}
				}
			}
		}
		
		return rs.toString();
	}

}
