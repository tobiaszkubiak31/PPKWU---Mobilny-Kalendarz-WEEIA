package pkkwu.Calendar.api;

public class Event {
	private int day;
	private String eventInfo;

	public Event(int day, String eventInfo) {
		this.day = day;
		this.eventInfo = eventInfo;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getEventInfo() {
		return eventInfo;
	}
	public void setEventInfo(String eventInfo) {
		this.eventInfo = eventInfo;
	}
}
