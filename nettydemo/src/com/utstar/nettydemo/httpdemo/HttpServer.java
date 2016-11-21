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
        //1、创建线程池
    	//创建处理连接的线程池:bossGroup
    	//创建处理所有事件的线程池:workerGroup
    	EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        	/*2、设定辅助启动类。ServerBootStrap
        	传入1中开辟的线程池
        	指定连接该服务器的channel类型
        	指定需要执行的childHandler
        	设置部分参数，如AdaptiveRecvByteBufAllocator缓存大小
        	.Option用于设置bossGroup相关参数
        	.childOption用于设置workerGroup相关参数*/
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                	/*当要添加多个handler时，就必须注意添加的顺序。
                                	　　这里的handler分为两种类型：
                                	　　　　一种继承ChannelInboundHandler，用于处理来自客户端的消息，比如对客户端的消息进行解码，读取等等。该类型在pipeline中的执行顺序与添加顺序一致。
                                	　　　　一种继承ChannelOutboundHandler，用于处理即将发往客户端的消息，比如对该消息进行编辑，编码等等。该类型在pipeline中的执行顺序与添加顺序相反。
                                	　　而且ChannelOutboundHandler的所有handler，放在ChannelInboundHandler下面是执行不到的。*/
                                	// server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                                    ch.pipeline().addLast(new HttpResponseEncoder());
                                    // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
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