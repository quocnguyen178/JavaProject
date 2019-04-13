package springmvc_example.dao;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public interface InsuredDao<T> {
	public ArrayList<T> listInsured(String id);
	
	public JSONObject infoInsured(String mongoId,String insuredId);
	public void updateInsured(String mongoId, String insuredId, JSONObject json_request) throws JSONException;
}
