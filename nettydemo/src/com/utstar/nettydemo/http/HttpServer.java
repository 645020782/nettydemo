package com.utstar.nettydemo.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpServer {
	private static final String SERVER_URI = "127.0.0.1";
	private static final int PORT = 8888;

	public void start() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					 .handler(new LoggingHandler(LogLevel.INFO))
					 .childHandler(new ChannelInitializer<SocketChannel>() {
						 @Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							// TODO Auto-generated method stub
							 ch.pipeline().addLast(new HttpServerCodec());
							 ch.pipeline().addLast(new HttpObjectAggregator(2048));
							 ch.pipeline().addLast(new ParseRequestHandler());
							 ch.pipeline().addLast(new OozieRequestHandler());
						}
					});
             
			ChannelFuture f = b.bind(SERVER_URI, PORT).sync();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception{
		//http://www.cnblogs.com/hapjin/p/5610253.html
		new HttpServer().start();
	}
}