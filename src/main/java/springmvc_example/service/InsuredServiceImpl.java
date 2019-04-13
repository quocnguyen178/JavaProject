package springmvc_example.service;

import java.util.ArrayList;

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

}
