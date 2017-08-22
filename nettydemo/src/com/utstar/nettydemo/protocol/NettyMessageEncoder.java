package com.utstar.nettydemo.protocol;

import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
	NettyMarshallingEncoder nettyMarshallingEncoder;
	
	public NettyMessageEncoder() {
		this.nettyMarshallingEncoder = MarshallingCodeCFactory.buildMarshallingEncoder();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg,
			List<Object> out) throws Exception {
		if (msg == null || msg.getHeader() == null) {
			throw new Exception("The encode message is null");
		}
		ByteBuf sendBuffer = Unpooled.buffer();
		Header header = msg.getHeader();
		sendBuffer.writeInt(header.getRcrCode());
		sendBuffer.writeByte(header.getPriority());
		sendBuffer.writeByte(header.getType());
		sendBuffer.writeLong(header.getSessionId());
		sendBuffer.writeInt(header.getLength());
		sendBuffer.writeInt(header.getAttachment().size());
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		for (Map.Entry<String, Object> param : header.getAttachment().entrySet()) {
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			value = param.getValue();
			sendBuffer.writeInt(keyArray.length);
			sendBuffer.writeBytes(keyArray);
			nettyMarshallingEncoder.encode(ctx, value, sendBuffer);
		}
		key = null;
		keyArray = null;
		value = null;
		if (msg.getBody() != null) {
			nettyMarshallingEncoder.encode(ctx, msg.getBody(), sendBuffer);
		} else {
			sendBuffer.writeInt(0);
			sendBuffer.setIndex(4, sendBuffer.readableBytes());
		}
		
	}
	
}
