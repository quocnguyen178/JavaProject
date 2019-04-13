package springmvc_example.dao;

import java.util.ArrayList;

import org.json.JSONObject;

public interface InsuredDao<T> {
	public ArrayList<T> listInsured(String id);
	
	public JSONObject infoInsured(String mongoId,String insuredId);
	public JSONObject updateInsured(JSONObject jsonupdate);
}
