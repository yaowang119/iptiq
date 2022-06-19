package yw.assignment.iptiq.provider;

import java.util.UUID;

import yw.assignment.iptiq.client.ClientRequest;
import yw.assignment.iptiq.loadbalancer.LoadBalancer;

public class Provider {
	
	private UUID uuid;
	private boolean active;
	private int health;
	private int numRequests;
	private LoadBalancer loadBalancer;
	public final static int maxRequests=10;
	
	public Provider(LoadBalancer loadBalancer) {
		this.uuid= UUID.randomUUID();
		this.loadBalancer = loadBalancer;
		//the provider is by default active
		setActive(true);
		//set health to 2 (maximumm)
		setHealth(2);
	}
	
	public String get(ClientRequest request) {
	    if (active && numRequests < Provider.maxRequests) {
	    	System.out.println("Provider "+this.uuid+" is processing client request "+request.getId());
	    	numRequests++;
	    	System.out.println("Provider "+this.uuid+" has now "+numRequests+" parallel request");
	    	if (numRequests == Provider.maxRequests) {
	    		informLoadBalancerIsFull();
	    		System.out.println("Provider "+this.uuid+" has reached its maximum capacity : 10 parallel request");
	    		}
	    	return this.uuid.toString();
	    }else if (active && numRequests== Provider.maxRequests) {
	    	
	    	return "full";
	    }
	    else 
	    	return null;
	}
	
	//inform load balancer that this provider has reached its maxium capacity, therefore not active by now
	//load balancer will exclude this provider for now
	public void informLoadBalancerIsFull() {
		this.loadBalancer.excludeProvider(this);
	}
	
	public void register() {
		this.loadBalancer.registerProvider(this);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxRequests() {
		return maxRequests;
	}

	public int getNumRequests() {
		return numRequests;
	}

	public void setNumRequests(int numRequests) {
		this.numRequests = numRequests;
	}
	
	public void addRequest() {
		this.numRequests++;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	

}
