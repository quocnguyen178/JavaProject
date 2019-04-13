package springmvc_example.dao;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public interface QuotationDao<T> {
	public String createQuotation(JSONObject document);
	
	public ArrayList<T> listQuotation();
	
	public JSONObject infoQuotation(String quotationId) throws JSONException;
}
