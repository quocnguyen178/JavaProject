package springmvc_example.xeservervpms;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class XeServerController {
	static final String xeServerUrl = "http://integral-everest-724722050.ap-southeast-2.elb.amazonaws.com/vpms/v1/rest/exec";

//	public ResponseEntity<?> doSomeThing(String body){              
//	    JSONObject request = new JSONObject(body);
//	}
	
	public String callXEServerVpms(JSONArray body) throws JSONException, Exception{
		List<?> response = exchange(xeServerUrl, HttpMethod.POST, null, body);
//		LOGGER.info("VPMS Response: " + response.toString());
		String status_code = response.get(0).toString();
		String result = null;
		
		if("200".equals(status_code)){
			JSONArray res = new JSONArray(response.get(2).toString());
			if (res.get((res.length() - 1)).toString().contains("ERROR")) {
				//	Logger.info("Full Error Response:" + res);
					Logger.getLogger("Full Error Response:" + res);
					throw new Exception(res.get(0).toString());
			} else {
					if(res.getJSONArray((res.length() - 2)).getInt(1) == 0){
						result = res.getJSONArray((res.length() - 2)).getString(0);
					} else{
						result = res.getJSONArray((res.length() - 2)).getString(2);
						String message = body + "Description: " + result + '\n';
						throw new Exception(message);
					}
				}
		}else{
				String log = response.get(2).toString().replace("\n", "").trim();
				int start = log.indexOf("<title>") + "<title>".length();
				int end = log.indexOf("</title>");
				String context = log.substring(start, end);
				String message = "Status code returned from XE server: " + status_code;
				throw new Exception(context + "Description: " + message);
		}
		return result;
	}

	protected List<?> exchange(String url, HttpMethod method, Map<String, String> headers, JSONArray body) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = createHeader(headers);
		HttpEntity<String> requestEntity = null;
		if (body != null) {
			requestEntity = new HttpEntity<String>(body.toString(), httpHeaders);
		} else {
			requestEntity = new HttpEntity<String>(httpHeaders);
		}
		ResponseEntity<?> response = restTemplate.exchange(url, method, requestEntity, String.class);
		try {
			return Arrays.asList(response.getStatusCode(), response.getHeaders().getLocation(), response.getBody());
		} catch (HttpStatusCodeException e) {
			return Arrays.asList(e.getStatusCode(), e.getResponseHeaders(), e.getResponseBodyAsString());
		} catch (RestClientException e) {
//			throw new Exception(e.getMessage());
		}
		return Arrays.asList(response.getStatusCode(), response.getHeaders().getLocation(), response.getBody());
	}

	private HttpHeaders createHeader(Map<String, String> headers) {
		HttpHeaders httpHeaders = new HttpHeaders();
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpHeaders.set(entry.getKey(), entry.getValue());
			}
		}
		return httpHeaders;
	}
}
