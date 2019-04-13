package springmvc_example.dao;

import java.util.ArrayList;

import org.json.JSONObject;

import com.mongodb.BasicDBObject;

public interface OwnerDao<T> {
	public ArrayList<T> listOwner(String id);

	public JSONObject infoOwner(String mongoId,String insuredId);
	
	//public JSONObject updateOwner(String id, String updateOwner);
}
