package springmvc_example.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public interface CoverageDAO {
	public JSONObject getCoverageById(String quote_id, String co_id) throws JSONException;
	public ArrayList<?> getCoverages(String id);
	public void update(String mongoID,String coverage_id, JSONObject jsonObject) throws JSONException;
}
