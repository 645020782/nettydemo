package com.utstar.nettydemo.endecode;

import java.util.ArrayList;
import java.util.List;

public class TestSubscribeReq {
		public static void main(String[] args)throws Exception{
	        SubscribeReqProto.SubscribeReq req=createSubscribeReq();
	        System.out.println("Before encode:"+req.toString());

	        SubscribeReqProto.SubscribeReq result=decode(encode(req));
	        System.out.println("decode cotent is:"+result.toString());
	    }
	    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
	            return req.toByteArray();
	    }
	    private static  SubscribeReqProto.SubscribeReq decode(byte[] body) throws Exception{
	        return SubscribeReqProto.SubscribeReq.parseFrom(body);
	    }

	    private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
	        SubscribeReqProto.SubscribeReq.Builder builder= SubscribeReqProto.SubscribeReq.newBuilder();
	        builder.setSubReqId(1);
	        builder.setUserName("zhouzhigang");
	        builder.setProductName("Netty Book");
	        List<String> address=new ArrayList<String>();
	        address.add("ºþÄÏ³¤É³");
	        address.add("ºþÄÏÖêÖÞ");
	        address.add("ºþÄÏÏæÌ¶");
	        builder.addAllAddress(address);
	        return builder.build();

	    }
}

