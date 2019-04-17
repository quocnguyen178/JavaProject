package springmvc_example.service;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public interface CoverageService {
	public ArrayList listCoverage(String id);
	
	public JSONObject getCoverageById(String mongoId,String coId) throws JSONException;
	public void update(String mongoID,String coverage_id, JSONObject jsonObject) throws JSONException;
	
}
