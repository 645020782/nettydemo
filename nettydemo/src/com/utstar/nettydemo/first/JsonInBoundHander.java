package com.utstar.nettydemo.first;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;

public class JsonInBoundHander extends ChannelInboundHandlerAdapter{
	private Map<String, String> parms = new HashMap<String, String>();
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		HttpRequest request = null;
		if (msg instanceof HttpRequest) {
			request = (HttpRequest) msg;
			parms = this.parse(request);
		}
		if (msg instanceof HttpContent) {
			HttpContent content = (HttpContent) msg;
			if (content instanceof LastHttpContent) {
				ByteBuf buf = content.content();
				buf.release();
				String name = parms.get("name") == null ? "" : parms.get("name");
				String age = parms.get("age") == null ? "" : parms.get("age");
				JSONObject json = new JSONObject();
				json.put("name", name);
				json.put("age", age);
				String res = json.toString();
				//生成response
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
						HttpResponseStatus.OK,Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
				//设置返回头
				HttpHeaders headers = response.headers();
				headers.set("Content-Type","text/plain");
				headers.set("Content-Length", response.content().readableBytes());
				headers.set("Content-Encoding","UTF-8");
				//返回结果
				ctx.write(response);
                ctx.flush();
			}
		}
		 /*Map<String, String> parse = this.parse(request);
	        Set<Entry<String, String>> entrySet = parse.entrySet();
	        Iterator<Entry<String, String>> iterator = entrySet.iterator();
	        while(iterator.hasNext()){
	        	Entry<String, String> next = iterator.next();
	        	System.out.println("key:"+next.getKey()+",value:"+next.getValue());
	        }*/
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
	}
	public Map<String, String> parse(HttpRequest request) throws Exception {
        HttpMethod method = request.method();

        Map<String, String> parmMap = new HashMap<>();
        //System.out.println("method:" + method);
        if (HttpMethod.GET == method) {
            // 是GET请求
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            /*decoder.parameters().entrySet().forEach( entry -> {
                // entry.getValue()是一个List, 只取第一个元素
                parmMap.put(entry.getKey(), entry.getValue().get(0));
            });*/
            Map<String, List<String>> parameters = decoder.parameters();
            Set<Entry<String, List<String>>> entrySet = parameters.entrySet();
            Iterator<Entry<String, List<String>>> iterator = entrySet.iterator();
            while(iterator.hasNext()){
            	Entry<String, List<String>> next = iterator.next();
            	parmMap.put(next.getKey(), next.getValue().get(0));
            	//System.out.println("key:"+next.getKey()+",value:"+next.getValue().get(0));
            }
        } /*else if (HttpMethod.POST == method) {
            // 是POST请求
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
            decoder.offer(request);

            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

            for (InterfaceHttpData parm : parmList) {

                Attribute data = (Attribute) parm;
                parmMap.put(data.getName(), data.getValue());
            }

        } else {
            // 不支持其它方法
            throw new MethodNotSupportedException(""); // 这是个自定义的异常, 可删掉这一行
        }*/

        return parmMap;
    }
}
