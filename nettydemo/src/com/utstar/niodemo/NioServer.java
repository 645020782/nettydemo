package com.utstar.niodemo;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer {
	public static final int PORT = 7777;
	private Selector selector;
	private ByteBuffer buf;
	public static void main(String[] args) throws IOException {
		NioServer server = new NioServer();
		server.init();
		server.listener();
	}
	private void init() throws IOException {
		buf = ByteBuffer.allocate(1024);
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ServerSocket socket = ssc.socket();
		socket.bind(new InetSocketAddress(PORT));
		selector = Selector.open();
		ssc.register(selector, SelectionKey.OP_ACCEPT);
	}
	private void listener() throws IOException {
		while (true) {
			try {
				selector.select();
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					keyHandler(key);
				}
			} catch (Exception e) {
				
			}
			
		}
	}
	private void keyHandler(SelectionKey key) {
		SocketChannel channel = null;
		try {
			if (key.isAcceptable()) {
				ServerSocketChannel server = (ServerSocketChannel) key.channel();
				channel = server.accept();
				channel.configureBlocking(false);
				System.out.println("server a : " + channel);
				channel.register(selector, SelectionKey.OP_READ);
			} else if (key.isReadable()) {
				channel = (SocketChannel) key.channel();
				buf.clear();
				int read = channel.read(buf);
				if (read > 0) {
					buf.flip();
					CharBuffer cBuf = CharsetHelper.decode(buf);
					//System.out.println("server receive : " + cBuf.toString());
					ByteBuffer bBuf = CharsetHelper.encode(CharBuffer.wrap("hi!come from server!"));
					channel.write(bBuf);
				}
				
			}
		} catch(Exception e) {
			key.cancel();
		}
		
	}
}
