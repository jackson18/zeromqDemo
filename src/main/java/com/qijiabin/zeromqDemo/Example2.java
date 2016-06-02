package com.qijiabin.zeromqDemo;


import org.zeromq.ZMQ;


/**
 * ========================================================
 * 日 期：2016年6月1日 下午4:44:42
 * 作 者：qijiabin
 * 版 本：1.0.0
 * 类说明：基于信封-内容的pub/sub发布订阅
 * TODO
 * ========================================================
 * 修订日期     修订人    描述
 */
public class Example2 {

	public static void main(String[] args) {
		new Thread(new Server()).start();
		new Thread(new Client()).start();
	}
	
	static class Client implements Runnable {
        @Override
        public void run() {
        	ZMQ.Context context = ZMQ.context(1);  
            ZMQ.Socket subscriber = context.socket(ZMQ.SUB);  
      
            subscriber.connect("tcp://localhost:5563");  
            subscriber.subscribe("We would like to see this".getBytes());  
            while (true) {  
                // 读取信封内容  
                // 读取内容  
                String address = new String(subscriber.recv(0));  
                String contents = new String(subscriber.recv(0));  
                System.out.println(address + " : " + contents);  
            }  
        }
    }

    static class Server implements Runnable {
        @Override
        public void run() {
        	ZMQ.Context context = ZMQ.context(1);  
            ZMQ.Socket publisher = context.socket(ZMQ.PUB);  
            publisher.bind("tcp://localhost:5563");  
            while (true) {  
                // 以信封-内容的形式写入2条消息  
                publisher.send("A".getBytes(), ZMQ.SNDMORE);  
                publisher.send("ok!".getBytes(), 0);  
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}  
            }  
        }
    }
	
}
