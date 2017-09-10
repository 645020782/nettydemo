package com.utstar.nettydemo.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage message = (NettyMessage) msg;
		if (message.getHeader() != null && 
				message.getHeader().getType() == MessageType.HEARTBEAT_REQ) {
			NettyMessage heartBeat = buildHeartBeat();
			System.out.println("server receive the client heartbeat!! : -->" + message);
			ctx.writeAndFlush(heartBeat);
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
		ctx.fireExceptionCaught(cause);
		ctx.close();
	}
	private NettyMessage buildHeartBeat() {
		NettyMessage msg = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP);
		msg.setHeader(header);
		return msg;
	}
}
