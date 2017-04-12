package com.utstar.niodemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioClient {
	private static Selector selector;
	public static final int PORT = 7777;
	private static ByteBuffer buf = ByteBuffer.allocate(1024);
	public static void main(String[] args) throws IOException {
		NioClient client = new NioClient();
		client.init();
	}
	private static void init() throws IOException {
		SocketChannel client = SocketChannel.open();
		client.configureBlocking(false);
		client.connect(new InetSocketAddress("localhost",PORT));
		selector = Selector.open();
		client.register(selector, SelectionKey.OP_CONNECT);
		while (true) {
			selector.select();
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
		while (it.hasNext()) {
			SelectionKey key = it.next();
			it.remove();
			if (key.isConnectable()) {
				if (client.finishConnect()) {
					key.interestOps(SelectionKey.OP_READ);
					client.write(CharsetHelper.encode(CharBuffer.wrap("hello!come from client!")));
				}
			} else if (key.isReadable()) {
				int read = client.read(buf);
				if (read > 0) {
					buf.flip();
					CharBuffer cBuf = CharsetHelper.decode(buf);
					System.out.println("client receive : " + cBuf.toString());
				}
				client.write(CharsetHelper.encode(CharBuffer.wrap("hello!come from client2!")));
				
			}
		}
		}
	}
}
