package wasdev.sample.servlet;

public class SendMessageImage extends SendMessage {
	public String originalContentUrl;
	public String previewImageUrl;
	
private static final String TYPE = "image";
	
	{
		this.type = TYPE;
	}
	
	public SendMessageImage(){
		this.originalContentUrl = "";
		this.previewImageUrl = "";
	}
	
	public SendMessageImage(String originalContentUrl, String previewImageUrl){
		this.originalContentUrl = originalContentUrl;
		this.previewImageUrl = previewImageUrl;
	}
}
