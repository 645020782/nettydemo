package com.utstar.nettydemo.protocol;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**
 * @author Lenovo
 *	jboss Marshalling 编解码工厂类
 *	用于生成编码对象和解码对象
 */
public final class MarshallingCodeCFactory {
	/**
	 * 生成解码对象
	 * @return
	 */
	public static NettyMarshallingDecoder buildMarshallingDecoder() {
		//获取marshallerFactory对象,serial表示创建的是java序列化工厂对象
		final MarshallerFactory marshallerFactory = Marshalling
				.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory,
				configuration);
		NettyMarshallingDecoder decoder = new NettyMarshallingDecoder(provider);
		return decoder;
	}
	/**
	 * 生成编码对象
	 * @return
	 */
	public static NettyMarshallingEncoder buildMarshallingEncoder() {
		//获取marshallerFactory对象,serial表示创建的是java序列化工厂对象
		final MarshallerFactory marshallerFactory = Marshalling
				.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory,
				configuration);
		NettyMarshallingEncoder encoder = new NettyMarshallingEncoder(provider);
		return encoder;
	}
}
