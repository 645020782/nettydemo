package com.utstar.nettydemo.endecode;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SubReqMarshallingClient {
	public void connect(int port) {
		NioEventLoopGroup worker = new NioEventLoopGroup();
		Bootstrap boot = new Bootstrap();
		boot.group(worker);
		boot.channel(NioSocketChannel.class);
		boot.option(ChannelOption.TCP_NODELAY, true);
		boot.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());  
				sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());  
				sc.pipeline().addLast(new SubReqMarshallingClientHandler());  
			}
		});
		ChannelFuture future = boot.connect("127.0.0.1", port);
		try {
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			worker.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new SubReqMarshallingClient().connect(8888);
	}
}
