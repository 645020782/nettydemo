package com.utstar.niodemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoServer2 {
	public static final int DEFAULT_PORT = 7777;
	public static void main(String[] args) throws IOException {
		System.out.println("Listening for connection on port " + DEFAULT_PORT);  
		//创建selector
		Selector selector = Selector.open();
		//创建serverchannel并初始化
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ServerSocket ss = ssc.socket();
		ss.bind(new InetSocketAddress(DEFAULT_PORT));
		ssc.configureBlocking(false);
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		//
		while (true) {
			int reallyNum = selector.select();
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> it = keys.iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				try {
					if (key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel client = server.accept();
						System.out.println("Accepted connection from " + client);  
						client.configureBlocking(false);
						SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
						ByteBuffer buf = ByteBuffer.allocate(100);
						clientKey.attach(buf);
					} else if (key.isReadable()) {
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer buf = (ByteBuffer) key.attachment();
						int n = client.read(buf);  
	                    if (n > 0) {  
	                        buf.flip();  
	                        key.interestOps(SelectionKey.OP_WRITE);     // switch to OP_WRITE  
	                    }  
					} else if (key.isWritable()) {
						System.out.println("is writable..."); 
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer buf = (ByteBuffer) key.attachment();
						if (buf.remaining() == 0) {  // write finished, switch to OP_READ  
	                        buf.clear();  
	                        key.interestOps(SelectionKey.OP_READ);  
	                    }  
					}
				} catch (Exception e) {
					key.cancel();
					key.channel().close();
				}
				it.remove();
			}
			
		}
	}
}
