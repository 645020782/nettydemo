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
		//1.���������߳����빤���߳���
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		//2.�������÷���������
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		//2.1�����������߳���
		serverBootstrap.group(bossGroup, workerGroup);
		//2.2����������Ƶ��
		serverBootstrap.channel(NioServerSocketChannel.class);
		//2.3���������ô�����
		serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				/*��Ҫ��Ӷ��handlerʱ���ͱ���ע����ӵ�˳��
          	���������handler��Ϊ�������ͣ�
          	��������һ�ּ̳�ChannelInboundHandler�����ڴ������Կͻ��˵���Ϣ������Կͻ��˵���Ϣ���н��룬��ȡ�ȵȡ���������pipeline�е�ִ��˳�������˳��һ�¡�
          	��������һ�ּ̳�ChannelOutboundHandler�����ڴ����������ͻ��˵���Ϣ������Ը���Ϣ���б༭������ȵȡ���������pipeline�е�ִ��˳�������˳���෴��
          	��������ChannelOutboundHandler������handler������ChannelInboundHandler������ִ�в����ġ�*/
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
