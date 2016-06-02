package com.qijiabin.zeromqDemo;

import java.util.Date;

import org.zeromq.ZMQ;

/**
 * ========================================================
 * 日 期：2016年6月1日 下午4:24:31
 * 作 者：qijiabin
 * 版 本：1.0.0
 * 类说明：发布与订阅模式
 * TODO
 * ========================================================
 * 修订日期     修订人    描述
 */
public class Example1 {
	
	public static void main(String[] args) {
		new Thread(new Server()).start();
		new Thread(new Client()).start();
	}

	static class Server implements Runnable {
		
		@Override
		public void run() {
			// 创建1个I/O线程的上下文 
			ZMQ.Context context = ZMQ.context(1);  
			// 发布者
	        ZMQ.Socket socket = context.socket(ZMQ.PUB);  
	        // 绑定服务地址及端口  
	        socket.bind ("tcp://localhost:9005");   
	          
	        while (true) {  
	        	try {  
	        		Thread.sleep (3000);  
	        	}catch(InterruptedException e){  
	        		e.printStackTrace();  
	        	}  
	        	// 订阅者需要订阅myTopic才能接收到消息 
	            String time = "myTopic " + new Date().getTime();  
	            socket.send(time.getBytes(), 0);  
	            System.out.println("@@@@ Server Send:" + time);  
	        }
		}
		
	}
	
	static class Client implements Runnable {
		
		@Override
		public void run() {
			// 创建1个I/O线程的上下文 
			ZMQ.Context context = ZMQ.context(1);  
			// 订阅者
	        ZMQ.Socket socket = context.socket(ZMQ.SUB);    
	        // 订阅主题
	        socket.subscribe("myTopic ".getBytes());   
	        // 创建链接  
	        socket.connect("tcp://localhost:9005"); 
	          
	        byte[] msg = null;  
	        while (true) {  
	            try {  
	                msg = socket.recv(0);  
	                System.out.println("#### Client Receive:" + new String(msg));  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  
	        }  
		}
	}
	
}
