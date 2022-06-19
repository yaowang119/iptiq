package yw.assignment.iptiq.simulation;

public class Event {
	//client events
	public static int ADD_CLIENT_REQUEST=0;
	public static int CLIENT_SEND_REQUEST=1;
	//provider manager events
	public static int ADD_PROVIDER=2;
	public static int REMOVE_PROVIDER=3;
	//load balancer events
	public static int CHECK_PROVIDERS=4;
	//provider event
	
	private int startTime;
	private Object param;
	private int event;
	
	public Event(int event, int startTime, Object param) {
		this.event = event;
		this.startTime = startTime;
		this.param = param;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}
	
	
}
