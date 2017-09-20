package com.utstar.nettydemo.endecode;

import java.util.ArrayList;
import java.util.List;

import com.utstar.nettydemo.endecode.SubscribeReqProto.SubscribeReq.Builder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SubReqClientHandler extends ChannelInboundHandlerAdapter{

	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {
			ctx.write(subReq(i));
		}
		ctx.flush();
	}

	private SubscribeReqProto.SubscribeReq subReq(int i) {
		Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setUserName("zhangsan");
		builder.setProductName("netty");
		builder.setSubReqId(i);
		List<String> address=new ArrayList<String>();  
        address.add("china");  
        address.add("usa");  
        address.add("france");  
        builder.addAllAddress(address);  
		return builder.build();
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
