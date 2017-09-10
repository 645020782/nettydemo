package com.utstar.nettydemo.protocol;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter{
	private ScheduledFuture<?> heartBeat;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage message = (NettyMessage) msg;
		if (message.getHeader() != null && 
				message.getHeader().getType() == MessageType.LOGIN_RESP) {
			heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0,
					5, TimeUnit.SECONDS);
		} else if (message.getHeader() != null && 
				message.getHeader().getType() == MessageType.HEARTBEAT_RESP) {
			System.out.println("Client receive the server heartbeat response! : -->" +
				message);
		} else {
			ctx.fireChannelRead(message);
		}
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		if (heartBeat != null) {
			heartBeat.cancel(true);
			heartBeat = null;
		}
		ctx.fireExceptionCaught(cause);
		ctx.close();
	}
	public class HeartBeatTask implements Runnable {
		private final ChannelHandlerContext ctx;
		public HeartBeatTask(ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}
		@Override
		public void run() {
			NettyMessage message = buildHeartBeat();
			ctx.writeAndFlush(message);
		}
		private NettyMessage buildHeartBeat() {
			NettyMessage msg = new NettyMessage();
			Header header = new Header();
			header.setType(MessageType.HEARTBEAT_REQ);
			msg.setHeader(header);
			return msg;
		}
	}
	
}
