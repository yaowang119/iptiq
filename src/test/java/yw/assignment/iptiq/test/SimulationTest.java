package yw.assignment.iptiq.test;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;

import yw.assignment.iptiq.client.ClientRequestManager;
import yw.assignment.iptiq.loadbalancer.LoadBalancer;
import yw.assignment.iptiq.loadbalancer.RandomLoadBalancer;
import yw.assignment.iptiq.loadbalancer.RoundRobinLoadBalancer;
import yw.assignment.iptiq.provider.ProviderManager;
import yw.assignment.iptiq.simulation.Event;
import yw.assignment.iptiq.simulation.EventExecution;

public class SimulationTest {

	Queue<Event> eventQueue = new LinkedList<Event>();
	LoadBalancer randomLoadBalancer = new RandomLoadBalancer();
	LoadBalancer roundRobinLoadBalancer = new RoundRobinLoadBalancer();
	ProviderManager pm = new ProviderManager();
	ClientRequestManager cm = new ClientRequestManager();
	EventExecution exc = new EventExecution(randomLoadBalancer, pm, cm, eventQueue);
	EventExecution exc2 = new EventExecution(roundRobinLoadBalancer, pm, cm, eventQueue);

	/**
	 * Test Scenario 1 Random Load Balancer All the providers are running idle
	 */
	@Test
	public void testScenario1() {
		System.out.println("Test Scenario 1 -----------Random Load Balancer 1--------------------------");
		// at time 1: add 3 providers, 2 client requests
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 1, 1));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 1, 1));
		// at time 2: request 0 and 1 call loadbalancer, add 2 client requests
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 2, 0));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 2, 1));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 2, 1));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 2, 1));
		// at time 3: add 1 client request and call load balancer
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 3, 1));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 2, 2));
		exc.execute();
	}

	/**
	 * Test Scenario 2 Random Load Balancer All the Providers are running at maximum
	 * capacity. Some requests will be rejected. After adding new providers, new
	 * requests can be accepted again.
	 */
	@Test
	public void testScenario2() {
		System.out.println("Test Scenario 2 -----------Random Load Balancer 2--------------------------");
		// at time 1: add 5 providers
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		// at time 2: add 50 requests
		for (int i = 0; i < 50; i++)
			eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 2, 1));
		// at time 3 : 50 requests call loadbalancer
		for (int i = 0; i < 50; i++)
			eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 2, i));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 3, 1));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 3, 1));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 3, 50));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 3, 51));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 4, null));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 5, 1));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 5, 1));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 5, 52));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 5, 53));
		exc.execute();
	}

	/**
	 * process longer requests
	 */
	@Test
	public void testScenario3() {
		// at time 1: add 2 providers
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		// at time 2: add 2 long requests
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 2, 10));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 2, 0));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 2, 20));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 2, 1));
		exc.execute();

	}

	/**
	 * test the check method with round robin load balancer
	 */
	@Test
	public void testScenario4() {
		// at time 1: add 2 provider
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		eventQueue.add(new Event(Event.ADD_PROVIDER, 1, null));
		// at time 2: add 5 requests
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 2, 1));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 2, 1));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 2, 1));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 2, 1));
		eventQueue.add(new Event(Event.ADD_CLIENT_REQUEST, 2, 1));
//		at time 3 : send 5 requests
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 3, 0));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 3, 1));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 3, 2));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 3, 3));
		eventQueue.add(new Event(Event.CLIENT_SEND_REQUEST, 3, 4));
		//at time 4: set one provider to be inactive and check activity of all providers
		eventQueue.add(new Event(Event.REMOVE_PROVIDER,4,0));
		eventQueue.add(new Event(Event.CHECK_PROVIDERS,4,null));
		//at time 5: check activity of all providers
		eventQueue.add(new Event(Event.CHECK_PROVIDERS,5,null));
		exc2.execute();
	}

	/**
	 * test the round robin load balancer
	 */
	@Test
	public void testScenario5() {

	}
}
