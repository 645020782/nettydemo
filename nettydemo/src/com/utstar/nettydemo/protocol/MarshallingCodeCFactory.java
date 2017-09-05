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
 *	jboss Marshalling ����빤����
 *	�������ɱ������ͽ������
 */
public final class MarshallingCodeCFactory {
	/**
	 * ���ɽ������
	 * @return
	 */
	public static NettyMarshallingDecoder buildMarshallingDecoder() {
		//��ȡmarshallerFactory����,serial��ʾ��������java���л���������
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
	 * ���ɱ������
	 * @return
	 */
	public static NettyMarshallingEncoder buildMarshallingEncoder() {
		//��ȡmarshallerFactory����,serial��ʾ��������java���л���������
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
