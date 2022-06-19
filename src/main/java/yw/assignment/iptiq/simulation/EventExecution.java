package yw.assignment.iptiq.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import yw.assignment.iptiq.client.ClientRequest;
import yw.assignment.iptiq.client.ClientRequestManager;
import yw.assignment.iptiq.loadbalancer.LoadBalancer;
import yw.assignment.iptiq.provider.Provider;
import yw.assignment.iptiq.provider.ProviderManager;

public class EventExecution {
	
	private LoadBalancer loadBalancer;
	private ProviderManager pm;
	private ClientRequestManager cm;
	private Queue<Event> eventQueue;
	private TreeMap<Integer, List<ClientRequest>> requestFinishedTime;
	
	public EventExecution(LoadBalancer loadBalancer, ProviderManager pm, 
			ClientRequestManager cm, Queue<Event> eventQueue) {
		this.loadBalancer = loadBalancer;
		this.pm = pm;
		this.cm = cm;
		this.eventQueue = eventQueue;
		this.requestFinishedTime = new TreeMap<Integer, List<ClientRequest>>();
	}
	
	public void execute() {
		//assume time is a integer and starts with 1
		int	startTime=1;
		
		while (!eventQueue.isEmpty()) {
			Event e = eventQueue.poll();
			 startTime= e.getStartTime();
			 
			//check if any request is finished
				if (requestFinishedTime.containsKey(startTime)) {
						finishRequest(startTime);
				}
				
			switch (e.getEvent()) {
			case 0 : {
				cm.addNewRequest((int)e.getParam());
				break;
			}
			case 1 : {
				ClientRequest req = cm.getRequests().get((int)e.getParam());
				req.sendRequest(loadBalancer);
				int finishTime = startTime+ req.getDuration();
				if (!requestFinishedTime.containsKey(finishTime)) {
					List<ClientRequest> idList = new ArrayList<ClientRequest>();
					idList.add(req);
					requestFinishedTime.put(finishTime, idList);
				}
				else {
					List<ClientRequest> idList = requestFinishedTime.get(finishTime);
					idList.add(req);
				}
				break;
			}
			case 2 : {
				pm.addProviderToLoadBalancer(loadBalancer);
				break;
			}
			case 3: {
				pm.removeProviderFromLoadBalancer(loadBalancer, (int)e.getParam());
				break;
			}
			case 4: {
				loadBalancer.heartBeatCheck();
				break;
			}
			}
		}
//		System.out.println("current time is "+startTime);
		//check if there is still any unfinished requests
		Set<Integer> keys = requestFinishedTime.keySet();
		for (Integer key : keys)
			if (key>startTime) {
				finishRequest(key);
			}
	}
		
	private void finishRequest(int time) {
		List<ClientRequest> reqList = requestFinishedTime.get(time);
		for (ClientRequest req : reqList) {
			System.out.println("********Client Request "+req.getId()+" get response from the provider "
						+req.getResponse()+"**********");
			System.out.println("");
		}
	}

	public LoadBalancer getLoadBalancer() {
		return loadBalancer;
	}

	public void setLoadBalancer(LoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
	}

	public ProviderManager getPm() {
		return pm;
	}

	public void setPm(ProviderManager pm) {
		this.pm = pm;
	}

	public Queue<Event> getEventQueue() {
		return eventQueue;
	}

	public void setEventQueue(Queue<Event> eventQueue) {
		this.eventQueue = eventQueue;
	}
	
	

}
