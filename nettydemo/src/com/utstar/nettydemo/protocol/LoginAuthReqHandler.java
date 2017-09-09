package com.utstar.nettydemo.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(buildLoginRep());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage message = (NettyMessage) msg;
		Header header = message.getHeader();
		if (header != null && header.getType() == MessageType.LOGIN_RESP) {
			byte resp = (byte) message.getBody();
			if (resp != (byte) 0) {
				ctx.close();
				System.out.println("login faild");
			} else {
				ctx.fireChannelRead(msg);
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
		ctx.fireExceptionCaught(cause);
	}
	private NettyMessage buildLoginRep() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ);
		message.setHeader(header);
		return message;
	}
	
}
