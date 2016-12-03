package wasdev.sample.servlet;

import java.util.ArrayList;
import java.util.List;


public class LineBotRequest {
	public String replyToken;
	public List<SendMessage> messages;
	
	public LineBotRequest(){
		this.messages = new ArrayList<SendMessage>();
	}
}
