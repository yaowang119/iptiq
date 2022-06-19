package yw.assignment.iptiq.client;

import java.util.ArrayList;
import java.util.List;

public class ClientRequestManager {
	
	private List<ClientRequest> requests;
	
	public ClientRequestManager() {
		this.requests = new ArrayList<ClientRequest>();
	}

	public List<ClientRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<ClientRequest> requests) {
		this.requests = requests;
	}
	
	public ClientRequest addNewRequest(int duration) {
		ClientRequest req = new ClientRequest(duration);
		this.requests.add(req);
		System.out.println("Add new client request "+req.getId());
		return req;
	}

}
