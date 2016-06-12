package com.qijiabin.zeromqDemo;

import org.zeromq.ZMQ;

/**
 * ========================================================
 * 日 期：2016年6月12日 上午9:53:23
 * 作 者：qijiabin
 * 版 本：1.0.0
 * 类说明：REQ/REP(c/s)模式
 * TODO
 * ========================================================
 * 修订日期     修订人    描述
 */
public class Example3 {
	
	public static void main(String[] args) {
		new Thread(new Server()).start();
		new Thread(new Client()).start();
	}
	
	/**
	 * ========================================================
	 * 日 期：2016年6月12日 上午10:05:50
	 * 作 者：qijiabin
	 * 版 本：1.0.0
	 * 类说明：
	 * TODO
	 * ========================================================
	 * 修订日期     修订人    描述
	 */
	static class Server implements Runnable {
		@Override
		public void run() {
			ZMQ.Context context = ZMQ.context(1);  
	        ZMQ.Socket socket = context.socket(ZMQ.REP);  
	        socket.bind("tcp://localhost:5555");  
	        while (true) {  
	            byte[] request;  
	            // 等待下一个client端的请求  
	            // 等待一个以0结尾的字符串  
	            // 忽略最后一位的0打印  
	            request = socket.recv(0);  
	            System.out.println("Received request: [" + new String(request, 0, request.length - 1) + "]");  
	  
	            try {  
	                Thread.sleep(1000);  
	            } catch (InterruptedException e) {  
	                e.printStackTrace();  
	            }  
	  
	            // 返回一个最后一位为0的字符串到客户端  
	            String replyString = "World" + " ";  
	            byte[] reply = replyString.getBytes();  
	            reply[reply.length - 1] = 0;  
	            socket.send(reply, ZMQ.NOBLOCK);  
	        }  
		}
	}
	
	/**
	 * ========================================================
	 * 日 期：2016年6月12日 上午10:05:21
	 * 作 者：qijiabin
	 * 版 本：1.0.0
	 * 类说明：REQ 发送完消息后，必须接收一个回应消息后，才能发送新的消息。
	 * 发送 "Hello"到服务端, 接收 "World"  
	 * ========================================================
	 * 修订日期     修订人    描述
	 */
	static class Client implements Runnable {
		@Override
		public void run() {
			ZMQ.Context context = ZMQ.context(1);  
	        ZMQ.Socket socket = context.socket(ZMQ.REQ);  
	        socket.connect("tcp://localhost:5555");  
	  
	        for (int request_nbr = 0; request_nbr <= 10; request_nbr++) {  
	            // 创建一个末尾为0的字符串  
	            String requestString = "Hello" + " ";  
	            byte[] request = requestString.getBytes();  
	            request[request.length - 1] = 0;  
	            // 发送  
	            socket.send(request, ZMQ.NOBLOCK);  
	  
	            // 获得返回值  
	            byte[] reply = socket.recv(0);  
	            // 输出去掉末尾0的字符串  
	            System.out.println("Received reply " + request_nbr + ": [" + new String(reply, 0, reply.length - 1) + "]");  
	        }  
		}
	}

}
