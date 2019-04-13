package springmvc_example.service;

import springmvc_example.dao.OwnerDao;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;

@Service
public class OwnerServiceImp implements OwnerService {
	@Autowired
	OwnerDao ownerDao;
	
	@Override
	public ArrayList listOwner(String id) {
		return ownerDao.listOwner(id);
	}
	
	@Override
	public JSONObject infoOwner(String mongoId,String insuredId) {
		return ownerDao.infoOwner(mongoId,insuredId);
	}
	
	/*
	 * @Override public JSONObject updateOwner(String id, String updateOwner) {
	 * return ownerDao.updateOwner(id, updateOwner); }
	 */
	
}
