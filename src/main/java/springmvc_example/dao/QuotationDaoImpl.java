package springmvc_example.dao;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.DBCollectionFindOptions;
import com.mongodb.client.model.Filters;
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
		mongoTemplate.insert(document.toString(), COLLECTION_NAME);
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase database = mongo.getDatabase("Blog");
		MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

		FindIterable<Document> findIterable = collection.find(Filters.eq("sub_id", sub_id))
				.projection(Projections.exclude("quotation_insured", "quotation_owner", "quotation_coverage"));

		MongoCursor<Document> cursorDocument = findIterable.iterator();
		findIterable.limit(1);
		try {
			while (cursorDocument.hasNext()) {
				return cursorDocument.next().toJson();

			}
		
			} finally {

				mongo.close();
			}
		

	return null;

	}

	@Override
	public String listQuotation() {
		
		ArrayList<JSONObject> listQuotesDocument = new ArrayList<>();
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase database = mongo.getDatabase("Blog");
		MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

		FindIterable<Document> findIterable = collection.find()
				.projection(Projections.exclude("quotation_insured", "quotation_owner", "quotation_coverage"));

		MongoCursor<Document> cursorDocument = findIterable.iterator();

		try {
			while (cursorDocument.hasNext()) {
				listQuotesDocument.add(new JSONObject(cursorDocument.next().toJson()));

			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {

			mongo.close();
		}

		return listQuotesDocument.toString();

	}

	@Override
	public JSONObject infoQuotation(String quotationId) throws JSONException {
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(quotationId));
		DBCollectionFindOptions options = new DBCollectionFindOptions();
        BasicDBObject projection = new BasicDBObject();
        projection.put("quotation_insured", 0);
        projection.put("quotation_owner", 0);
        projection.put("quotation_coverage", 0);
        options.projection(projection);
        DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find(query, options);
		return new JSONObject(cursor.next().toString());
	}

}
