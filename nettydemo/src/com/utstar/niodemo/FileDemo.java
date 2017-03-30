package com.utstar.niodemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

import net.sf.json.util.NewBeanInstanceStrategy;
import io.netty.channel.Channel;

public class FileDemo {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		FileInputStream fin = new FileInputStream("d:/58199.txt");
		FileChannel channel = fin.getChannel();
		ByteBuffer dst = ByteBuffer.allocate(1024*10);
		ByteBuffer bb = ByteBuffer.allocate(1024*3);
		byte[] bs = new byte[25];
		int lastPosition = 0;
		int len = 0;
		while ((len = channel.read(dst)) != -1){
			dst.flip();
			//dst.rewind();
			while (dst.hasRemaining()) {
				byte b = dst.get();
				if (b == 13) {
					readLine(dst, bb);
				} else {
					if (bb.position() < bb.limit()) {
						bb.put(b);
					}
				}
			}
			dst.clear();
	
		}
		readLine(dst, bb);
		channel.close();
		fin.close();
	}

	/**
	 * @param dst
	 * @param bb
	 */
	private static void readLine(ByteBuffer dst, ByteBuffer bb) {
		byte[] bs;
		int p = bb.position();
		bs = new byte[p];
		if (dst.position() < dst.limit()) {
			dst.get();
		}
		bb.flip();
		bb.get(bs);
		String s = new String(bs,0,bs.length);
		System.out.println(s + " isFinish");
		bb.clear();
	}
}
