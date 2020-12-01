package pkkwu.Calendar.api;

public class Event {
	private int day;
	private String desc;

	public Event(int day, String desc) {
		this.day = day;
		this.desc = desc;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
