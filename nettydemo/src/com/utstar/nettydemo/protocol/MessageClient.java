package com.utstar.nettydemo.protocol;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class MessageClient {
	private ExecutorService  executor = Executors.newScheduledThreadPool(1);
	public void connect(final int port) {
		try {
			NioEventLoopGroup worker = new NioEventLoopGroup();
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(worker);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					sc.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
					sc.pipeline().addLast("nettyMessageEncoder", new NettyMessageEncoder());
					sc.pipeline().addLast("loginAuthReqHandler", new LoginAuthReqHandler());
					//sc.pipeline().addLast("heartBeatReqHandler", new HeartBeatReqHandler());
					//sc.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
				}
			});
			ChannelFuture future = bootstrap.connect("127.0.0.1", port).sync();
			System.out.println("Netty time Client connected at port " + port);
			future.channel().close().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
		/*	executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(5);
						connect(port);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});*/
		}
	}
	public static void main(String[] args) {
		new MessageClient().connect(8888);
	}
}
