package springmvc_example.service;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;

public interface OwnerService {
	public ArrayList listOwner(String id);
	
	public JSONObject infoOwner(String mongoId,String insuredId);
	
	public void updateOwner(String mongoId, String ownerId, JSONObject json_request) throws JSONException;
	//public JSONObject updateOwner(String id, String updateOwner);
	public void updateAgeOwner(String mongoId,String ownerId,String json_age) throws JSONException;
}
