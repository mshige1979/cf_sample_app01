package wasdev.sample.servlet;

public class Event {
	public String replyToken;
	public String type;
	public long timestamp;
	public Source source;
	public Message message;
	public Object postback;
	
	public String getEventType(){
		return type;
	}
	public String getMessageType(){
		
		String res = null;
		
		if(null != message){
			res = message.type;
		}
		
		return res;
	}
	
	public String getSourceId(){
		String res = null;
		
		if(null != source){
			res = source.userId;
		}
		
		return res;
	}
}
