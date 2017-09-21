package com.utstar.nettydemo.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class MessageServer {
	public void bind(int port) {
		NioEventLoopGroup boss = new NioEventLoopGroup();
		NioEventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boss, worker);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.option(ChannelOption.SO_BACKLOG, 128);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast("nettyMessageDecoder",new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
				sc.pipeline().addLast(new NettyMessageEncoder());
				sc.pipeline().addLast("loginAuthRespHandler", new LoginAuthRespHandler());
				//sc.pipeline().addLast("heartBeatRespHandler", new HeartBeatRespHandler());
				//sc.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
			}
		});
		try {
			ChannelFuture futrue = bootstrap.bind(port).sync();
			System.out.println("Netty time Server started at port " + port);
			futrue.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new MessageServer().bind(8888);
	}
}
