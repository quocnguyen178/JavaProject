package springmvc_example.dao;

import java.util.ArrayList;

import org.bson.Document;
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
		document = helper.generateId(document);
		// System.out.println(document);
		JSONObject js = new JSONObject();
		String mongoReturn = "";
		mongoTemplate.insert(document.toString(), COLLECTION_NAME);
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
		while (cursor.hasNext()) {
			try {
				js = new JSONObject(cursor.next().toString());
				//JSONObject _id = js.getJSONObject("_id");
				//DBObject id = (DBObject) JSON.parse(_id.toString());
				String storeList = document.toString();
				MongoClient client = new MongoClient("localhost");
				MongoDatabase db = client.getDatabase("Blog");
				
				MongoCollection collection = db.getCollection(COLLECTION_NAME);
				//FindIterable<Document> myDocument = collection.findOneAndUpdate(filter, update)
				//.projection(Projections.exclude("quotation_owner","quotation_insured","quotation_owner")) ;
				//System.out.println(myDocument.toString());
				client.close();
				mongoReturn = storeList;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return mongoReturn;
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
