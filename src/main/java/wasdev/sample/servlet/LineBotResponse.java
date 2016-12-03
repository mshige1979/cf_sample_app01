package wasdev.sample.servlet;

import java.util.List;

public class LineBotResponse {
	public List<Event> events;
	public Event get(int idx){
		return events.get(idx);
	}
}
