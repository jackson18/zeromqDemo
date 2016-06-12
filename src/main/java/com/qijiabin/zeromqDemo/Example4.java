package com.qijiabin.zeromqDemo;

import org.zeromq.ZMQ;

/**
 * ========================================================
 * 日 期：2016年6月12日 上午10:07:07
 * 作 者：qijiabin
 * 版 本：1.0.0
 * 类说明：push/pull模式
 * TODO
 * ========================================================
 * 修订日期     修订人    描述
 */
public class Example4 {

	public static void main(String[] args) {
		ZMQ.Context context = ZMQ.context(1);  
        ZMQ.Socket sync = context.socket(ZMQ.PUSH);  
        sync.connect("tcp://localhost:5564");  
        sync.send("".getBytes(), 0);  
        int update_nbr;  
        for (update_nbr = 0; update_nbr < 10; update_nbr++) {  
            sync.send("Rhubarb".getBytes(), ZMQ.NOBLOCK);  
        }  
        sync.send("END".getBytes(), 0);  
	}
	
	static class Pull implements Runnable {
		@Override
		public void run() {
			ZMQ.Context context = ZMQ.context(1);  
	        ZMQ.Socket sync = context.socket(ZMQ.PULL);  
	        sync.bind("tcp://localhost:5564");  
	        sync.recv(0);  
	        int update_nbr = 0;  
	        while (true) {  
	            byte[] stringValue = sync.recv(0);  
	            String string = new String(stringValue);  
	            if (string.equals("END")) {  
	                break;  
	            }  
	            update_nbr++;  
	            System.out.println("Received " + update_nbr + " updates. :" + string);  
	        }  
	        sync.close();  
	        context.term();
		}
	}
	
}


