package com.utstar.nettydemo.endecode;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SubReqMarshallingClientHandler extends ChannelInboundHandlerAdapter{

	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {
			ctx.write(subReq(i));
		}
		ctx.flush();
	}

	private SubscribeReq subReq(int i) {
		SubscribeReq builder = new SubscribeReq();
		builder.setUserName("zhangsan");
		builder.setProductName("netty");
		builder.setSubReqId(i);
		builder.setAddress("china");
		return builder;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("Recevice the response from server : [" + msg + "]");
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
