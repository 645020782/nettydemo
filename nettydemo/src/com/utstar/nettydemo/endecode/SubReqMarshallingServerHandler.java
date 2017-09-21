package com.utstar.nettydemo.endecode;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SubReqMarshallingServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		SubscribeReq req= (SubscribeReq) msg;
		System.out.println("server receiver client message is:"+req.toString());  
        ctx.writeAndFlush(resp(req.getSubReqId()));  
	}

	private SubscribeResp resp(int subReqId) {
		SubscribeResp resp = new SubscribeResp();
		resp.setSubReqId(subReqId);
		resp.setRespCode(0);
		resp.setDesc("Netty Book order succeed 3 day later,sent to the designated adderss");
		return resp;
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
