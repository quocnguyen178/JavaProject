package springmvc_example.dao;

import java.awt.JobAttributes;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
@Repository
public class CoverageDAOImpl implements CoverageDAO {
	@Autowired
	MongoTemplate mongoTemplate;
	private static final String COLLECTION_NAME = "user";
	
	@Override
	public JSONObject getCoverageById(String mongoId,String coverage_id){
		JSONObject js;
//		BasicDBObject searchQuery = new BasicDBObject();
//		searchQuery.put("_id", coverage_id);
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
		JSONObject objectInfo = new JSONObject();
		while (cursor.hasNext()) {
			try {
				js = new JSONObject(cursor.next().toString());
				JSONObject id = js.getJSONObject("_id");
				String stringId = (String) id.get("$oid");
				JSONArray storeList = js.getJSONArray("quotation_coverage");
				String _id = "";
				for (int i = 0; i < storeList.length(); i++) {
					JSONObject info = storeList.getJSONObject(i);
					_id = info.getString("_id");
					if (coverage_id.equals(_id)&& mongoId.equals(stringId))
						objectInfo = info;
				}
				
				/*if(js.get("sub_id").equals(mongoId)) {
					if(js.get("quotation_coverage") instanceof JSONArray) {
						JSONArray jarray = (JSONArray) js.get("quotation_coverage");
						for (int i = 0; i < jarray.length(); i++) {
							JSONObject jo = jarray.getJSONObject(i);
							if(jo.get("_id").equals(coverage_id)) {
								objectInfo = jo;
							}
						}
					}
				}*/
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return objectInfo;
		
	}

	@Override
	public ArrayList<?> getCoverages(String id) {
		// TODO Auto-generated method stub
		ArrayList<JSONObject> list = new ArrayList<>();
		JSONObject js = new JSONObject();
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
		while (cursor.hasNext()) {
			try {
				JSONObject tam = new JSONObject();
				js = new JSONObject(cursor.next().toString());
				JSONObject _id = js.getJSONObject("_id");
				String stringID = (String) _id.get("$oid");	
				if(stringID.equals(id)) {
				JSONObject quote_coverage = js.getJSONArray("quotation_coverage").getJSONObject(0);
				list.add(quote_coverage);
				}
				
				/*if(js.get("sub_id").equals(id)) {
					if(js.get("quotation_coverage") instanceof JSONArray) {
						JSONObject coverage = js.getJSONArray("quotation_coverage").getJSONObject(0);
						list.add(coverage);
					}
				}
				
				*/
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	
	public JSONObject updateCoverage(String mongoID,String coverage_id , JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		
		JSONObject joOld = getCoverageById(mongoID,coverage_id);
		JSONObject joNew = json;
		Iterator iteratorOld = joOld.keys();
		String keyOld = null;
		int cuont = 0;
		while (iteratorOld.hasNext()) {
			keyOld = (String) iteratorOld.next();
			Iterator iteratorNew = joNew.keys();
			String keyNew = null;
			 //System.out.println(joOld.optJSONObject(keyOld) + "____KEy___"+keyOld);
			while (iteratorNew.hasNext()) {
				keyNew = (String) iteratorNew.next();
				//System.out.println("Key New___"+keyNew);
				if (joNew.optJSONArray(keyNew) == null && joNew.optJSONObject(
						keyNew) == null /*
										 * && joOld.optJSONArray(keyOld) == null && joOld.optJSONObject(keyOld) == null
										 */ ) {
					// System.out.println("INSIDE");
					// System.out.println("OLD__ "+keyOld);
					// System.out.println("NEW__ "+keyNew);
					if (keyOld.equals(keyNew)) {
						joOld.put(keyOld, joNew.get(keyNew));
						 //System.out.println("Value No JARR&JO__"+joOld.get(keyOld));
					}
				}

				if (joNew.optJSONObject(keyNew) != null) {
					if (keyOld.equals(keyNew)) {
						joOld.put(keyOld, joNew.get(keyNew));
						 //System.out.println("Is Object___"+joOld.get(keyOld));
					}

				}

				if (joNew.optJSONArray(keyNew) != null) {
					if (keyOld.equals(keyNew)) {
						JSONArray jaNew = joNew.getJSONArray(keyNew);
						JSONArray jaOld = joOld.getJSONArray(keyOld);

						for (int i = 0; i < jaOld.length(); i++) {
							JSONObject old = jaOld.getJSONObject(i);
							for (int j = 0; j < jaNew.length(); j++) {
								JSONObject news = jaNew.getJSONObject(j);
								Iterator arrIteratoeOld = old.keys();
								String keyArrOld = null;
								while (arrIteratoeOld.hasNext()) {
									keyArrOld = (String) arrIteratoeOld.next();
									Iterator arrIteratorNew = news.keys();
									String keyArrNew = null;
									while (arrIteratorNew.hasNext()) {
										keyArrNew = (String) arrIteratorNew.next();
										if (news.optJSONObject(keyArrNew) == null
												&& news.optJSONArray(keyArrNew) == null) {
											if (keyArrOld.equals(keyArrNew)) {
												old.put(keyArrOld, news.get(keyArrNew));
												// System.out.println("Array String");
											}

										}

										if (news.optJSONObject(keyArrNew) != null) {
											if (keyArrOld.equals(keyArrNew)) {
												old.put(keyArrOld, news.get(keyArrNew));
												// System.out.println("Array Object");
											}
										}
									}
								}

								/*
								 * if (keyOld.equals(keyNew)) { System.out.println("Array___ OLD:_" + keyOld +
								 * "__New:_" + keyNew);
								 * 
								 * }
								 */
							}
						}
					}

				}
			}
			// System.out.println(keyOld);
			// cuont++;
		}
		// System.out.println(cuont);
		
		return joOld;
		
	}

	@Override
	public void update(String mongoId, String coverage_id, JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		DBObject dataNew = (DBObject)JSON.parse(updateCoverage(mongoId, coverage_id, jsonObject).toString()) ;
		JSONObject js;
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
		while(cursor.hasNext()) {
			js = new JSONObject(cursor.next().toString());
			JSONObject _id = js.getJSONObject("_id");
			String stringID = (String) _id.get("$oid");	
			
			if(stringID.equals(mongoId)) {
				String subId =(String) js.get("sub_id");
				//System.out.println("SUB_IS______"+subId);
				if(js.get("quotation_coverage") instanceof JSONArray) {
					//System.out.println("Inside ARRAY");
					JSONArray jarray = (JSONArray) js.get("quotation_coverage");
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject jo = jarray.getJSONObject(i);
						//System.out.println(jo.get("_id"));
						if(jo.get("_id").equals(coverage_id)) {
							//System.out.println("Inside Coverage_id");
							BasicDBObject query = new BasicDBObject("sub_id",subId);
							query.put("quotation_coverage._id", coverage_id);
							BasicDBObject updateObject = new BasicDBObject();
							updateObject.append("$set",new BasicDBObject("quotation_coverage.$",dataNew));
//							System.out.println("Query___"+query);
//							System.out.println("Update___"+updateObject);
//							System.out.println("dataNEW____"+dataNew.toString());
							mongoTemplate.getCollection(COLLECTION_NAME).update(query, updateObject);
						}
					}
				}
			}
			System.out.println(js.toString());
		}
		//return updateCoverage(mongoId, coverage_id, jsonObject);
	}
	

}
