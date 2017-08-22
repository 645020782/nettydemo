package com.utstar.nettydemo.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.HashMap;
import java.util.Map;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
	private NettyMarshallingDecoder nettyMarshallingDecoder;
	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset,
			int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		nettyMarshallingDecoder = MarshallingCodeCFactory.buildMarshallingDecoder();
	}
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
			throws Exception {
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null) {
			return null;
		}
		NettyMessage nettyMessage = new NettyMessage();
		Header header = new Header();
		header.setRcrCode(in.readInt());
		header.setPriority(in.readByte());
		header.setType(in.readByte());
		header.setSessionId(in.readLong());
		header.setLength(in.readInt());
		int size = in.readInt();
		if (size > 0) {
			Map<String, Object> attch = new HashMap<String, Object>(size);
			int keySize = 0;
			byte[] keyArray = null;
			String key = null;
			for (int i = 0; i < size; i++) {
				keySize = in.readInt();
				keyArray = new byte[keySize];
				in.readBytes(keyArray);
				key = new String(keyArray, "UTF-8");
				attch.put(key, nettyMarshallingDecoder.decode(ctx, in));
			}
			keyArray = null;
			key = null;
			header.setAttachment(attch);
			if (in.readableBytes() > 4) {
				nettyMessage.setBody(nettyMarshallingDecoder.decode(ctx, in));
			}
			nettyMessage.setHeader(header);
		}
		return nettyMessage;
	}
	
}
