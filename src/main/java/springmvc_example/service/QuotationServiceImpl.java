package springmvc_example.service;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springmvc_example.dao.QuotationDao;

@Service
public class QuotationServiceImpl implements QuotationService {

	@Autowired
	QuotationDao quotationDao;
	
	@Override
	public String createQuotation(JSONObject documet) {
		return quotationDao.createQuotation(documet);
		
	}

	@Override
	public ArrayList listQuotation() {	
		return quotationDao.listQuotation();
	}

	@Override
	public JSONObject infoQuotation(String quotationId) throws JSONException {
		return quotationDao.infoQuotation(quotationId);
	}

}
