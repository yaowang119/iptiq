package yw.assignment.iptiq.loadbalancer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import yw.assignment.iptiq.client.ClientRequest;
import yw.assignment.iptiq.provider.Provider;

public class RandomLoadBalancer extends LoadBalancer{
	
	public RandomLoadBalancer() {
		super();
	}
	/**
	 * Providers will be chosen here randomly
	 */
	@Override
	public String get(ClientRequest request) {
		List<Provider> list = getActiveProviders();
		//generate a random index of the list
		int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
		Provider randomProvider = list.get(randomNum);
		System.out.println("Radom Load Balancer will pass client request "+request.getId()+" to provider "+randomProvider.getUuid());
		return randomProvider.get(request);
	}
	
}
