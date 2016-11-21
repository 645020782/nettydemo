package com.utstar.nettydemo.httpdemo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture; 
import io.netty.channel.ChannelInitializer; 
import io.netty.channel.ChannelOption; 
import io.netty.channel.EventLoopGroup; 
import io.netty.channel.nio.NioEventLoopGroup; 
import io.netty.channel.socket.SocketChannel; 
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder; 
import io.netty.handler.codec.http.HttpResponseEncoder; 

public class HttpServer {

    private static Log log = LogFactory.getLog(HttpServer.class);
    
    public void start(int port) throws Exception {
        //1�������̳߳�
    	//�����������ӵ��̳߳�:bossGroup
    	//�������������¼����̳߳�:workerGroup
    	EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        	/*2���趨���������ࡣServerBootStrap
        	����1�п��ٵ��̳߳�
        	ָ�����Ӹ÷�������channel����
        	ָ����Ҫִ�е�childHandler
        	���ò��ֲ�������AdaptiveRecvByteBufAllocator�����С
        	.Option��������bossGroup��ز���
        	.childOption��������workerGroup��ز���*/
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                	/*��Ҫ��Ӷ��handlerʱ���ͱ���ע����ӵ�˳��
                                	���������handler��Ϊ�������ͣ�
                                	��������һ�ּ̳�ChannelInboundHandler�����ڴ������Կͻ��˵���Ϣ������Կͻ��˵���Ϣ���н��룬��ȡ�ȵȡ���������pipeline�е�ִ��˳�������˳��һ�¡�
                                	��������һ�ּ̳�ChannelOutboundHandler�����ڴ����������ͻ��˵���Ϣ������Ը���Ϣ���б༭������ȵȡ���������pipeline�е�ִ��˳�������˳���෴��
                                	��������ChannelOutboundHandler������handler������ChannelInboundHandler������ִ�в����ġ�*/
                                	// server�˷��͵���httpResponse������Ҫʹ��HttpResponseEncoder���б���
                                    ch.pipeline().addLast(new HttpResponseEncoder());
                                    // server�˽��յ�����httpRequest������Ҫʹ��HttpRequestDecoder���н���
                                    ch.pipeline().addLast(new HttpRequestDecoder());
                                    ch.pipeline().addLast(new HttpServerInboundHandler());
                                }
                            }).option(ChannelOption.SO_BACKLOG, 128) 
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer();
        log.info("Http Server listening on 8844 ...");
        server.start(8844);
    }
}