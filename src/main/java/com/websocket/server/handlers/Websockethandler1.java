package com.websocket.server.handlers;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.server.model.Stock;

public class Websockethandler1 implements WebSocketHandler{

	 private final ObjectMapper objectMapper = new ObjectMapper();
	    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	    Random r = new Random();
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("connected");
		this.sessions.add(session);
//		messageedit(session);
	}
	
	private void messageedit(WebSocketSession session) throws InterruptedException, IOException {
		float oldPrice = 0.0f;
        //Publishing new stock prices every one second for 100 times
        for (int i=0; i < 100; i ++){
            //Calculating Random stock price between 12$ to 13$
            float stockPrice = 12 + r.nextFloat() * (13 - 12);
            float roundedPrice = (float) (Math.round(stockPrice * 100.0) / 100.0);

            //Creating a Stock Object
            Stock stock = new Stock("Amazon",
                    "https://cdn.cdnlogo.com/logos/a/77/amazon-dark.svg",
                    roundedPrice);
            //Finding whether the stock pric increased or decreased
            if (roundedPrice > oldPrice){
                stock.setIncreased(true);
            }
            oldPrice = roundedPrice;

            //Sending StockPrice
            TextMessage message1 = new TextMessage(objectMapper.writeValueAsString(stock));
            session.sendMessage(message1);
            Thread.sleep(1000);
        }
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("hlo");
		System.out.println(message.toString());
		sessions.forEach(e->{
			try {
				e.sendMessage(message);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
	}

}
