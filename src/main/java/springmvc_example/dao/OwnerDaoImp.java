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
public class OwnerDaoImp implements OwnerDao {
	@Autowired
	MongoTemplate mongoTemplate;

	private static final String COLLECTION_NAME = "user";

	@Override
	public ArrayList listOwner(String id) {
		ArrayList<JSONObject> listOwner = new ArrayList<>();
		JSONObject js = new JSONObject();
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
		while (cursor.hasNext()) {
			try {
				js = new JSONObject(cursor.next().toString());
				JSONObject _id = js.getJSONObject("_id");
				String stringID = (String) _id.get("$oid");	
				if(stringID.equals(id)) {
				JSONObject quote_owner = js.getJSONArray("quotation_owner").getJSONObject(0);
				listOwner.add(quote_owner);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listOwner;
	}

	@Override
	public JSONObject infoOwner(String mongoId,String insuredId) {
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
				JSONArray storeList = js.getJSONArray("quotation_owner");
				String _id = "";
				for (int i = 0; i < storeList.length(); i++) {
					JSONObject info = storeList.getJSONObject(i);
					_id = info.getString("_id");
					if (insuredId.equals(_id)&& mongoId.equals(stringId))
						objectInfo = info;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return objectInfo;
	}

	@Override
	public void updateOwner(String mongoId, String ownerId, JSONObject json_request) throws JSONException {
		BasicDBObject query = new BasicDBObject();
		BasicDBObject set = new BasicDBObject();
		BasicDBObject update = new BasicDBObject();
		
		// Update quotation_owner
		query.put("quotation_owner._id", ownerId);
		query.put("_id", new ObjectId(mongoId));
		
		/*if(json_request.getBoolean("quote-owner-same-as-insured") == true) {
			boolean isInsured = json_request.getBoolean("quote-owner-same-as-insured");
			set.put("quotation_owner.$.quote_owner_same_as_insured.value", isInsured);
		}*/
		if(json_request.has("quote-owner-gender")) {
			String gender = json_request.getString("quote-owner-gender");
			set.put("quotation_owner.$.quote_owner_gender.value", gender);
		}
		if(json_request.has("quote-owner-residence-state")) {
			String residenceState = json_request.getString("quote-owner-residence-state");
			set.put("quotation_owner.$.quote_owner_residence_state.value", residenceState);
		}
		if(json_request.has("quote-owner-birth-date")) {
			String dateOfBirth = json_request.getString("quote-owner-birth-date");
			set.put("quotation_owner.$.quote_owner_date_of_birth", dateOfBirth);
		}
		if(json_request.has("quote-owner-occupation")) {
			String occupation = json_request.getString("quote-owner-occupation");
			set.put("quotation_owner.$.quote_owner_occupation.value", occupation);
		}
		if(json_request.has("quote-owner-annual-income")) {
			String annualIncome = json_request.getString("quote-owner-annual-income");
			set.put("quotation_owner.$.quote_owner_annual_income.value", annualIncome);
		}
		if(json_request.has("quote-owner-smoking-status")) {
			String smokingStatus = json_request.getString("quote-owner-smoking-status");
			set.put("quotation_owner.$.quote_owner_smoking_status.value", smokingStatus);
		}
		if(json_request.has("quote-owner-given-name")) {
			String givenName = json_request.getString("quote-owner-given-name");
			set.put("quotation_owner.$.quote_owner_given_name", givenName);
		}
		if(json_request.has("quote-owner-last-name")) {
			String lastName = json_request.getString("quote-owner-last-name");
			set.put("quotation_owner.$.quote_owner_last_name", lastName);
		}
		if(json_request.has("quote-owner-last-name") && json_request.has("quote-owner-given-name")) {
			String lastName = json_request.getString("quote-owner-last-name");
			String givenName = json_request.getString("quote-owner-given-name");
			set.put("quotation_owner.$.quote_owner_full_name", givenName + " " + lastName);
		}
		if(json_request.has("quote-owner-title")) {
			String title = json_request.getString("quote-owner-title");
			set.put("quotation_owner.$.quote_owner_title.value", title);
		}
		if(json_request.has("quote-owner-nationality")) {
			String nationality = json_request.getString("quote-owner-nationality");
			set.put("quotation_owner.$.quote_owner_nationality.value", nationality);
		}
		if(json_request.has("quote-owner-marital-status")) {
			String marialStatus = json_request.getString("quote-owner-marital-status");
			set.put("quotation_owner.$.quote_owner_marital_status.value", marialStatus);
		}
		update.put("$set", set);
		mongoTemplate.getCollection(COLLECTION_NAME).update(query, update);
	}

	@Override
	public void updateAgeOwner(String mongoId, String ownerId, String json_age) throws JSONException {
		JSONObject json_xe = new JSONObject(json_age);
		String next_birthday = json_xe.getJSONArray("quotation_owner").getJSONObject(0).get("owner_age").toString();
		
		BasicDBObject query = new BasicDBObject();
		BasicDBObject set = new BasicDBObject();
		BasicDBObject update = new BasicDBObject();
		
		// Update quotation_owner
		query.put("quotation_owner._id", ownerId);
		query.put("_id", new ObjectId(mongoId));
		set.put("quotation_owner.$.quote_owner_age_next_birthday", next_birthday);
		update.put("$set", set);
		mongoTemplate.getCollection(COLLECTION_NAME).update(query, update);
	}

	/*
	 * @Override public JSONObject updateOwner(String id, String updateOwner) {
	 * JSONObject js; BasicDBObject searchQuery = new BasicDBObject();
	 * searchQuery.put("_id", id); DBCursor cursor =
	 * mongoTemplate.getCollection(COLLECTION_NAME).find(); JSONObject objectInfo =
	 * new JSONObject();
	 * 
	 * while (cursor.hasNext()) { try { JSONObject objectInfo1 = new
	 * JSONObject(updateOwner); js = new JSONObject(cursor.next().toString());
	 * JSONArray storeList = js.getJSONArray("quotation_owner"); String _id = "";
	 * for (int i = 0; i < storeList.length(); i++) { JSONObject info =
	 * storeList.getJSONObject(i); _id = info.getString("_id"); if (id.equals(_id))
	 * info.put("quote_owner_given_name", objectInfo1); objectInfo = info; } } catch
	 * (JSONException e) { e.printStackTrace(); } } return objectInfo; }
	 */
}
