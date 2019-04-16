package springmvc_example.dao;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

@Repository
public class InsuredDaoImpl implements InsuredDao {

	@Autowired
	MongoTemplate mongoTemplate;

	private static final String COLLECTION_NAME = "user";

	@Override
	public ArrayList listInsured(String id) {
		ArrayList<JSONObject> listInsured = new ArrayList<>();
		JSONObject js = new JSONObject();
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
		while (cursor.hasNext()) {
			try {
				js = new JSONObject(cursor.next().toString());
				JSONObject _id = js.getJSONObject("_id");
				String stringId = (String) _id.get("$oid");
				if(stringId.equals(id)) {
				JSONObject quote_insured = js.getJSONArray("quotation_insured").getJSONObject(0);
				listInsured.add(quote_insured);
				
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listInsured;
	}

	@Override
	public JSONObject infoInsured(String mongoId,String insuredId) {

		JSONObject js;
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", insuredId);
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
		JSONObject objectInfo = new JSONObject();
		while (cursor.hasNext()) {
			try {
				js = new JSONObject(cursor.next().toString());
				JSONObject id = js.getJSONObject("_id");
				String stringId = (String) id.get("$oid");
				//if vao dung quote thi moi duyet toi mang insured
				if(stringId.equals(mongoId)) {
					JSONArray storeList = js.getJSONArray("quotation_insured");
					String _id = "";
					for (int i = 0; i < storeList.length(); i++) {
						JSONObject info = storeList.getJSONObject(i);
						_id = info.getString("_id");
						if (insuredId.equals(_id))
							objectInfo = info;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return objectInfo;
	}

	@Override
	public void updateInsured(String mongoId, String insuredId, JSONObject json_request) throws JSONException {
		BasicDBObject query = new BasicDBObject();
		BasicDBObject set = new BasicDBObject();
		BasicDBObject update = new BasicDBObject();
		
		boolean isInsured = json_request.getBoolean("quote-owner-same-as-insured");
		String gender = json_request.getString("quote-owner-gender");
		String residenceState = json_request.getString("quote-owner-residence-state");
		String dateOfBirth = json_request.getString("quote-owner-birth-date");
		String occupation = json_request.getString("quote-owner-occupation");
		String annualIncome = json_request.getString("quote-owner-annual-income");
		String smokingStatus = json_request.getString("quote-owner-smoking-status");
		String givenName = json_request.getString("quote-owner-given-name");
		String lastName = json_request.getString("quote-owner-last-name");
		String title = json_request.getString("quote-owner-title");
		if (isInsured) {
			
			query.put("quotation_insured._id", insuredId);
			query.put("_id", new ObjectId(mongoId));
			
			// Update quotation_insured
			set.put("quotation_insured.$.insured_gender.value", gender);
			set.put("quotation_insured.$.insured_occupation.value", occupation);
			set.put("quotation_insured.$.c_resourceName", "def");
			set.put("quotation_insured.$.insured_full_name", givenName + " " + lastName);
			set.put("quotation_insured.$.insured_date_of_birth", dateOfBirth);
			set.put("quotation_insured.$.insured_last_name", lastName);
			set.put("quotation_insured.$.insured_smoking_status.value", smokingStatus);
			set.put("quotation_insured.$.insured_residence_state.value", residenceState);
			set.put("quotation_insured.$.insured_given_name", givenName);
			set.put("quotation_insured.$.insured_annual_income.value", annualIncome);
			set.put("quotation_insured.$.insured_title.value", title);
			update.put("$set", set);
			mongoTemplate.getCollection(COLLECTION_NAME).update(query, update);
		}
		
	}

}
