package springmvc_example.service;

import java.util.ArrayList;

import org.json.JSONObject;

import com.mongodb.BasicDBObject;

public interface OwnerService {
	public ArrayList listOwner(String id);
	
	public JSONObject infoOwner(String mongoId,String insuredId);
	
	//public JSONObject updateOwner(String id, String updateOwner);
}
