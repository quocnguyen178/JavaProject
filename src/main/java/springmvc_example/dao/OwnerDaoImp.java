package springmvc_example.dao;

import java.util.ArrayList;

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
