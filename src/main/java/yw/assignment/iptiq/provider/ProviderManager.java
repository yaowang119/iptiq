package yw.assignment.iptiq.provider;

import java.util.ArrayList;
import java.util.List;

import yw.assignment.iptiq.client.ClientRequest;
import yw.assignment.iptiq.loadbalancer.LoadBalancer;

/**
 * Provider Manager can add or remove a provider, which simulates the server 
 * being switched or off in the real life
 * @author Yao
 *
 */
public class ProviderManager {
	
	private List<Provider> providers;
	
	public ProviderManager() {
		this.providers = new ArrayList<Provider>();
	}
	
	public void addProviderToLoadBalancer(LoadBalancer loadBalancer) {
		Provider p = new Provider(loadBalancer);
		this.providers.add(p);
		p.register();
		System.out.println("new Provider "+p.getUuid()+" added");
	}
	
	public void removeProviderFromLoadBalancer(LoadBalancer loadBalancer, int providerIndex) {
		Provider p = this.providers.get(providerIndex);
		loadBalancer.excludeProvider(p);
	}

}
