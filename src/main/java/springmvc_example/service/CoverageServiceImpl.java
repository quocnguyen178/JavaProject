package springmvc_example.service;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springmvc_example.dao.CoverageDAO;

@Service
public class CoverageServiceImpl implements CoverageService {
	@Autowired
	CoverageDAO coverageDao;

	@Override
	public ArrayList listCoverage(String id) {
		// TODO Auto-generated method stub
		return coverageDao.getCoverages(id);
	}

	@Override
	public JSONObject getCoverageById(String mongoId, String coverage_id) throws JSONException {
		// TODO Auto-generated method stub
		return coverageDao.getCoverageById(mongoId, coverage_id);
	}

	@Override
	public JSONObject update(String mongoID, String coverage_id, JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return coverageDao.update(mongoID, coverage_id, jsonObject);
	}

}
