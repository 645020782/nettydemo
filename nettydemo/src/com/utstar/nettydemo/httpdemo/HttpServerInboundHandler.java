package com.utstar.nettydemo.httpdemo;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.MethodNotSupportedException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

    private static Log log = LogFactory.getLog(HttpServerInboundHandler.class);

    private HttpRequest request;
    private int count=0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
    	count++;
        if (msg instanceof HttpRequest) {
        	System.out.println("msg1:" + msg);
            request = (HttpRequest) msg;

            String uri = request.getUri();
            System.out.println("Uri:" + uri);
            System.out.println("第一次从request出来");
        }
        if (msg instanceof HttpContent) {
        	System.out.println("msg2:" + msg);
            HttpContent content = (HttpContent) msg;
            if(content instanceof LastHttpContent){
            	ByteBuf buf = content.content();
                System.out.println("第二次从reponse出来");
                System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
                buf.release();

                String res = "I am OK";
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                        OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
                response.headers().set(CONTENT_TYPE, "text/plain");
                response.headers().set(CONTENT_LENGTH,
                        response.content().readableBytes());
                if (HttpHeaders.isKeepAlive(request)) {
                    response.headers().set(CONNECTION, Values.KEEP_ALIVE);
                }
                ctx.write(response);
                ctx.flush();
            }
            
        }
        System.out.println("count:" + count);
        Map<String, String> parse = this.parse(request);
        Set<Entry<String, String>> entrySet = parse.entrySet();
        Iterator<Entry<String, String>> iterator = entrySet.iterator();
        while(iterator.hasNext()){
        	Entry<String, String> next = iterator.next();
        	System.out.println("key:"+next.getKey()+",value:"+next.getValue());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage());
        ctx.close();
    }
    public Map<String, String> parse(HttpRequest request) throws Exception {
        HttpMethod method = request.method();

        Map<String, String> parmMap = new HashMap<>();
        System.out.println("method:" + method);
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
