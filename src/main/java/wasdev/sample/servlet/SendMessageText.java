package wasdev.sample.servlet;

public class SendMessageText extends SendMessage {
	public String text;
	private static final String TYPE = "text";
	
	{
		this.type = TYPE;
	}
	
	public SendMessageText(){
		this.text = "";
	}
	
	public SendMessageText(String text){
		this.text = text;
	}
}
