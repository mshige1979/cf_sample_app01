package wasdev.sample.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import java.io.BufferedReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/SimpleServlet")
public class SimpleServlet extends HttpServlet {
    private static final long serialVersionUID = 3L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JsonParseException, JsonMappingException {
    	
    	ServletContext sc = getServletContext();
    	String num = (String)sc.getAttribute("count");
    	if(null == num){
    		num = "1";
    	} else {
    		num = String.valueOf((Integer.parseInt(num) + 1));
    	}
    	sc.setAttribute("count", num);
    	
    	String body = (String)sc.getAttribute("body");
    	String token = (String)sc.getAttribute("token");
    	String signature = (String)sc.getAttribute("signature");
    	
    	response.setContentType("text/html; charset=UTF-8");
        response.getWriter().print(num);
        response.getWriter().print(body);
        response.getWriter().print(token);
        response.getWriter().print(signature);
    }
    
    @Override    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
    	
    	ServletContext sc = getServletContext();
    	String num = (String)sc.getAttribute("count");
    	if(null == num){
    		num = "1";
    	} else {
    		num = String.valueOf((Integer.parseInt(num) + 1));
    	}
    	sc.setAttribute("count", num);
    	
    	// 受信
    	BufferedReader bufferReaderBody = new BufferedReader(request.getReader());
		String body = bufferReaderBody.readLine();
		sc.setAttribute("body", body);
		
		// 署名
		try{
			String channel_secret = "aec190a3839462a454906032f834adf4";
			String signature = request.getHeader("X-Line-Signature");
			SecretKeySpec key = new SecretKeySpec(channel_secret.getBytes(), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(key);
	        byte[] source = body.getBytes(StandardCharsets.UTF_8);
	        String createdSignature = Base64.encodeBase64String(mac.doFinal(source));
	        if (!signature.equals(createdSignature)) {
		    	sc.setAttribute("signature", "ng");
	        	response.setStatus(200);
	            return;
	        }else{
		    	sc.setAttribute("signature", "ok");
	        }
		}catch(NoSuchAlgorithmException e){
		}catch(InvalidKeyException e){
		}
		
		// json->オブジェクト変換
		ObjectMapper mapper = new ObjectMapper();
        LineBotResponse result = mapper.readValue(body, LineBotResponse.class);
        
        // イベントデータ取得
        Event event = result.get(0);
            System.out.println(event.replyToken);
    	sc.setAttribute("token", event.replyToken);
    	
    	// 送信データを作成
    	LineBotRequest lineReq = null;
        String json = null ;
        lineReq = new LineBotRequest();
        lineReq.replyToken = event.replyToken;
        lineReq.messages.add(new SendMessageText("hogehoge"));
        json = mapper.writeValueAsString(lineReq);
        
        // 送信処理
        String access_token = "JoGPMXrT0qrP/20D2d33TfFBeZPKF5o8tBL59S4id+NY1VjHV5fg2wCOs97f9D9aiLc44id96Xdn6niFY7oo2M1UTjJSb6OrPO43gim7M6pRvMdps6RWv9zuB9n08t9B8+xpoFV4vCE4+7P5ijLungdB04t89/1O/w1cDnyilFU=";
        HttpPost httpPost = new HttpPost("https://api.line.me/v2/bot/message/reply");    
        httpPost.setHeader("Content-Type", "application/json");
       	httpPost.setHeader("Authorization", "Bearer " + access_token);
        StringEntity params = new StringEntity(json, StandardCharsets.UTF_8);
        httpPost.setEntity(params);
        
        try (CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse resp = client.execute(httpPost);
                BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), StandardCharsets.UTF_8))) {
            
            int statusCode = resp.getStatusLine().getStatusCode();
            switch (statusCode) {
            case 200:
                // ↓これは空のJSON({})が返るはず
                br.readLine();
                break;
            default:
            }
        } catch (final ClientProtocolException e) {
        } catch (final IOException e) {
        }
    	
    }

}
