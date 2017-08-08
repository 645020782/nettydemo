package com.utstar.nettydemo.first;

import com.utstar.nettydemo.httpdemo.HttpServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class JsonServer {
	public void bind(int port) {
		//1.生成连接线程组与工作线程组
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		//2.生成设置服务启动类
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		//2.1启动类设置线程组
		serverBootstrap.group(bossGroup, workerGroup);
		//2.2启动类设置频道
		serverBootstrap.channel(NioServerSocketChannel.class);
		//2.3启动类设置处理器
		serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				/*当要添加多个handler时，就必须注意添加的顺序。
          	　　这里的handler分为两种类型：
          	　　　　一种继承ChannelInboundHandler，用于处理来自客户端的消息，比如对客户端的消息进行解码，读取等等。该类型在pipeline中的执行顺序与添加顺序一致。
          	　　　　一种继承ChannelOutboundHandler，用于处理即将发往客户端的消息，比如对该消息进行编辑，编码等等。该类型在pipeline中的执行顺序与添加顺序相反。
          	　　而且ChannelOutboundHandler的所有handler，放在ChannelInboundHandler下面是执行不到的。*/
				sc.pipeline().addLast(new HttpResponseEncoder());
				sc.pipeline().addLast(new HttpRequestDecoder());
				sc.pipeline().addLast(new JsonInBoundHander());
			}
		});
		serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
		serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		try {
			ChannelFuture future = serverBootstrap.bind(port).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args) throws Exception {
        JsonServer server = new JsonServer();
        System.out.println("Http Server listening on 8844 ...");
        server.bind(8844);
    }
}
