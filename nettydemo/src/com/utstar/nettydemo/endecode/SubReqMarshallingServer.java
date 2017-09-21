package com.utstar.nettydemo.endecode;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SubReqMarshallingServer {
	public void bind(int port) {
		NioEventLoopGroup boss = new NioEventLoopGroup();
		NioEventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap boot = new ServerBootstrap();
		boot.group(boss, worker);
		boot.channel(NioServerSocketChannel.class);
		boot.option(ChannelOption.SO_BACKLOG, 1024);
		boot.handler(new LoggingHandler(LogLevel.INFO)); 
		boot.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());  
				sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());  
				sc.pipeline().addLast(new SubReqMarshallingServerHandler());  
			}
		});
		ChannelFuture future = boot.bind(port);
		try {
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new SubReqMarshallingServer().bind(8888);
	}
}
