package springmvc_example.helper;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Helper {
	public JSONObject generateId(JSONObject document) {
		try {
			ObjectId _id = new ObjectId();
			String sub_id = "id" + _id.getTimestamp();
			String primary_in = "in" + _id.getTimestamp();
			String primary_ow = "ow" + _id.getTimestamp();
			String primary_co = "co" + _id.getTimestamp();
			
			document.put("sub_id", sub_id);
			if (document.getJSONArray("quotation_insured") != null) {
				JSONObject array_insured_putid = document.getJSONArray("quotation_insured").getJSONObject(0).put("_id", primary_in);
			}
			if (document.getJSONArray("quotation_owner") != null) {
				JSONObject array_owner_putid = document.getJSONArray("quotation_owner").getJSONObject(0).put("_id", primary_ow);
			}
			if (document.getJSONArray("quotation_coverage") != null) {
				JSONObject array_coverage = document.getJSONArray("quotation_coverage").getJSONObject(0).put("_id", primary_co);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return document;
	}

}
