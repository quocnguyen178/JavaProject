package springmvc_example.service;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springmvc_example.dao.InsuredDao;

@Service
public class InsuredServiceImpl implements InsuredService{

	@Autowired
	InsuredDao insuredDao;
	
	@Override
	public ArrayList listInsured(String id) {
		return insuredDao.listInsured(id);
	}

	@Override
	public JSONObject infoInsured(String mongoId, String insuredId) {
		return insuredDao.infoInsured(mongoId,insuredId);
	}

	@Override
	public void updateInsured(String mongoId, String insuredId, JSONObject json_request) throws JSONException {
		insuredDao.updateInsured(mongoId, insuredId, json_request);
	}

}
