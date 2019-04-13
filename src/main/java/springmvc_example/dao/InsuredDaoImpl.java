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
				JSONArray storeList = js.getJSONArray("quotation_insured");
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
	public JSONObject updateInsured(JSONObject jsonupdate) {
		// TODO Auto-generated method stub
		return null;
	}
}
