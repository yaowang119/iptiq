package yw.assignment.iptiq.loadbalancer;

import java.util.ArrayList;
import java.util.List;

import yw.assignment.iptiq.client.ClientRequest;
import yw.assignment.iptiq.provider.Provider;

public abstract class LoadBalancer {

	private List<Provider> registerdProviders;
	private List<Provider> activeProviders;
	public final static int maxNum_Provider = 10;

	public LoadBalancer() {
		this.registerdProviders = new ArrayList<Provider>();
		this.activeProviders = new ArrayList<Provider>();
	}

	public List<Provider> getRegisterdProviders() {
		return registerdProviders;
	}

	public int getMaxNum_Provider() {
		return maxNum_Provider;
	}

	public void registerProvider(Provider p) {
		if (!this.registerdProviders.contains(p) && this.registerdProviders.size() < LoadBalancer.maxNum_Provider) {
			this.registerdProviders.add(p);
			this.activeProviders.add(p);
		}
	}

	public void excludeProvider(Provider p) {
		System.out.println("remove provider "+p.getUuid()+" from active provider list");
		p.setHealth(0);
		this.activeProviders.remove(p);
	}

	public void includeProvider(Provider p) {
		this.activeProviders.add(p);
	}

	public List<Provider> getActiveProviders() {
		return activeProviders;
	}

	public void setActiveProviders(List<Provider> activeProviders) {
		this.activeProviders = activeProviders;
	}
	
	//check if there is any active provider available to accept the request
	public String distribute(ClientRequest request) {
		boolean provider_available=false;
		for (Provider p : this.activeProviders) {
			if (p.getNumRequests()<Provider.maxRequests) {
				provider_available=true;
				break;
			}		
		}
		if (provider_available)
			return get(request);
		else
			return "Providers are all working at maximum capacity. "
					+ "Client Request "+request.getId()+" cannot be accepted";
	}

	public abstract String get(ClientRequest request);

	/**
	 * All the providers will be checked.
	 * If any active provider does not respond or respond 'full', then it will be set to inactive.
	 * If any inactive provider responds for 2 times in a row, then it will be set to active.
	 */
	public void heartBeatCheck() {
		for (Provider p : this.registerdProviders) {
			// check all active providers
			if (this.activeProviders.contains(p)) {
				// send a request to all active providers
				ClientRequest r = new ClientRequest(1);
				String response = p.get(r);
				// if any provider sends null or full back, then that provider will be
				// considered inactive
				if (response == null || response.equals("full")) {
					excludeProvider(p);
					p.setHealth(0);
				}
			}

			// check all inactive providers
			if (!this.activeProviders.contains(p)) {
				// send a request to all active providers
				ClientRequest r = new ClientRequest(1);
				String response = p.get(r);
				// if the loadbalancer receives the id from the provider
				// then increase the health of the provider by 1
				if (response != null && !response.equals("full")) {
					if (p.getHealth() == 0){
						p.setHealth(1);
						System.out.println("received response from inactive provider "+p.getUuid());
						break;
					}

					if (p.getHealth() == 1) {
						p.setHealth(2);
						includeProvider(p);
						System.out.println("received response from inactive provider "+p.getUuid()+" again. This provider is active now!");
					}
				}
				// if the load balancer receives null or full from that provider
				// then set the health of the provider to 0
				else {
					if (p.getHealth() > 0)
						p.setHealth(0);
				}
			}

		}

	}
}
