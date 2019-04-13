package springmvc_example.service;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public interface QuotationService {

	public String createQuotation(JSONObject document);

	public String listQuotation();
	
	public JSONObject infoQuotation(String quotationId) throws JSONException;
}
