package yw.assignment.iptiq.loadbalancer;

import yw.assignment.iptiq.client.ClientRequest;
import yw.assignment.iptiq.provider.Provider;

public class RoundRobinLoadBalancer extends LoadBalancer{
	
	private int currentProviderIndex;
	
	public RoundRobinLoadBalancer() {
		super();
		this.currentProviderIndex=0;
	}

	@Override
	public String get(ClientRequest request){
			int maxNum = getActiveProviders().size();
			Provider currentProvider=null;
			currentProvider =getActiveProviders().get(currentProviderIndex);
			this.currentProviderIndex++;
			
			if (this.currentProviderIndex== maxNum) {
				this.currentProviderIndex=0;
			}
			
			this.currentProviderIndex++;
			return currentProvider.get(request);
	}
	
}
