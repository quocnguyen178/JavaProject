package springmvc_example.dao;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

import springmvc_example.helper.Helper;

@Repository
public class QuotationDaoImpl implements QuotationDao {

	@Autowired
	MongoTemplate mongoTemplate;

	private static final String COLLECTION_NAME = "user";

	@Override
	public String createQuotation(JSONObject document) {
		Helper helper = new Helper();
		ObjectId doc_id = new ObjectId();
		String sub_id = "id" + doc_id.getTimestamp();
		try {
			document.put("sub_id", sub_id);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		document = helper.generateId(document);
		JSONObject js = new JSONObject();
		String quotationReturn = "";
		mongoTemplate.insert(document.toString(), COLLECTION_NAME);
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
		while (cursor.hasNext()) {
			try {
				js = new JSONObject(cursor.next().toString());
				String stringId = js.getString("sub_id");
				if(sub_id.equals(stringId)) {
					quotationReturn = js.toString();
					return quotationReturn;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return quotationReturn;
	}

	@Override
	public ArrayList listQuotation() {
		ArrayList<JSONObject> listQuotes = new ArrayList<>();
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
		while (cursor.hasNext()) {
			try {
				listQuotes.add(new JSONObject(cursor.next().toString()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listQuotes;
	}

}
