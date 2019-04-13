package springmvc_example.service;

import java.util.ArrayList;

import org.json.JSONObject;

public interface InsuredService {
	public ArrayList listInsured(String id);
	
	public JSONObject infoInsured(String mongoId,String insuredId);
}
