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

		boolean isInsured = json_request.getJSONObject("quote_owner_same_as_insured").getBoolean("value");
		String gender = json_request.getJSONObject("quote_owner_gender").getString("value");
		String residenceState = json_request.getJSONObject("quote_owner_residence_state").getString("value");
		String dateOfBirth = json_request.getString("quote_owner_date_of_birth");
		String occupation = json_request.getJSONObject("quote_owner_occupation").getString("value");
		String annualIncome = json_request.getJSONObject("quote_owner_annual_income").getString("value");
		String annualIncomeMin = json_request.getJSONObject("quote_owner_annual_income").getString("minvalue");
		String annualIncomeMax = json_request.getJSONObject("quote_owner_annual_income").getString("maxvalue");
		String smokingStatus = json_request.getJSONObject("quote_owner_smoking_status").getString("value");
		String givenName = json_request.getString("quote_owner_given_name");
		String lastName = json_request.getString("quote_owner_last_name");
		String title = json_request.getJSONObject("quote_owner_title").getString("value");
		String nationality = json_request.getJSONObject("quote_owner_nationality").getString("value");
		String marialStatus = json_request.getJSONObject("quote_owner_marital_status").getString("value");
		
		// Update quotation_owner
		query.put("quotation_owner._id", ownerId);
		query.put("_id", new ObjectId(mongoId));
		set.put("quotation_owner.$.quote_owner_title.value", title);
		set.put("quotation_owner.$.quote_owner_given_name", givenName);
		set.put("quotation_owner.$.quote_owner_gender.value", gender);
		set.put("quotation_owner.$.quote_owner_annual_income.value", annualIncome);
		set.put("quotation_owner.$.quote_owner_annual_income.minvalue", annualIncomeMin);
		set.put("quotation_owner.$.quote_owner_annual_income.maxvalue", annualIncomeMax);
		set.put("quotation_owner.$.quote_owner_residence_state.value", residenceState);
		set.put("quotation_owner.$.quote_owner_date_of_birth", dateOfBirth);
		set.put("quotation_owner.$.quote_owner_last_name", lastName);
		set.put("quotation_owner.$.quote_owner_full_name", givenName + " " + lastName);
		set.put("quotation_owner.$.quote_owner_smoking_status.value", smokingStatus);
		set.put("quotation_owner.$.quote_owner_nationality.value", nationality);
		set.put("quotation_owner.$.quote_owner_marital_status.value", marialStatus);
		set.put("quotation_owner.$.quote_owner_occupation.value", occupation);
		
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
