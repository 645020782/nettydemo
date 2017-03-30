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
		File file = new File("d:/t.txt");
		System.out.println(file.length());
		FileInputStream fin = new FileInputStream("d:/t.txt");
		FileChannel channel = fin.getChannel();
		ByteBuffer dst = ByteBuffer.allocate(25);
		ByteBuffer bb = ByteBuffer.allocate(20);
		int lastPosition = 0;
		int len = 0;
		while ((len = channel.read(dst)) != -1){
			//dst.flip();
			dst.rewind();
			while (dst.hasRemaining()) {
				byte b = dst.get();
				if (b == 13) {
					int p = bb.position();
					int l = p - lastPosition ;
					byte[] bs = new byte[l];
					lastPosition = p;
					bb.get(bs);
					System.out.println(new String(bs,0,bs.length) + "isFinish");
				} else {
					if (bb.position() < bb.limit()) {
						bb.put(b);
					} else {
						bb.rewind();
					}
				}
			}
			dst.clear();
	
		}
		channel.close();
		fin.close();
	}
}
