package de.slackspace.osiris.diagnosis;

import javax.ws.rs.core.Response;

public class ResultConverter {

	public String convertToString(Object result) {
		if(result == null) {
			return "";
		}
		
		if(result instanceof Response) {
			return convertResponseToString((Response) result);
		}
		
		return result.toString();
	}
	
	private String convertResponseToString(Response result) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("meta[status=%s, reason=%s]", result.getStatus(), result.getStatusInfo().getReasonPhrase()));
		
		if(result.hasEntity()) {
			sb.append(String.format(" entity[%s]", result.getEntity().toString()));
		}
		return sb.toString();
	}
}
