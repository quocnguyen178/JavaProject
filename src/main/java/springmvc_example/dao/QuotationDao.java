package springmvc_example.dao;

import java.util.ArrayList;

import org.json.JSONObject;

public interface QuotationDao<T> {
	public String createQuotation(JSONObject document);
	
	public ArrayList<T> listQuotation();
}
