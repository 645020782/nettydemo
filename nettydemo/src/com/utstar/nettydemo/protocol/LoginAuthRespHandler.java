package com.utstar.nettydemo.protocol;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter{
	private String[] IP_LIST = {"127.0.0.1"};
	private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage message = (NettyMessage) msg;
		if (message != null && message.getHeader().getType() == MessageType.LOGIN_REQ) {
			String nodeIndex = ctx.channel().remoteAddress().toString();
			NettyMessage respMessage = null;
			//登录认证
			if (nodeCheck.containsKey(nodeIndex)) {
				respMessage = buildResponse((byte) -1);
			} else {
				//IP认证
				//获取IP地址
				InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
				String hostAddress = address.getAddress().getHostAddress();
				boolean isLoginOk = false;
				//IP过滤
				for (String ip : IP_LIST) {
					if (ip.equals(hostAddress)) {
						isLoginOk = true;
						break;
					}
				}
				respMessage = isLoginOk ? buildResponse((byte) 0) : buildResponse((byte) -1);
				if (isLoginOk) {
					nodeCheck.put(nodeIndex, true);
				}
			}
			ctx.writeAndFlush(respMessage);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	private NettyMessage buildResponse(byte result) {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP);
		message.setHeader(header);
		message.setBody(result);
		return message;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.fireExceptionCaught(cause);
		ctx.close();
	}
	

}
