package com.utstar.nettydemo.http;

public abstract class JobConfigBuilder {

	private static final String USER_NAME = "cdhfive";
	private static final String JOB_TRACKER = "datanode1:8032";
	private static final String NAME_NODE = "hdfs://datanode1:8020";
	
	//鐜閰嶇疆
	public StringBuilder versionConfig(){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("    <configuration>");
		return sb;
	}
	
	//闆嗙兢閰嶇疆
	public StringBuilder clusterConfig(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("        <property>");
		sb.append("            <name>" + "user.name" + "</name>");
		sb.append("            <value>" + USER_NAME + "</value>");
		sb.append("        </property>");
		
		sb.append(      "<property>");
		sb.append(          "<name>jobTracker</name>");
		sb.append(          "<value>" + JOB_TRACKER + "</value>");
		sb.append(      "</property>");
		sb.append(      "<property>");
		sb.append(          "<name>nameNode</name>");
		sb.append(          "<value>" + NAME_NODE + "</value>");
		sb.append(      "</property>");
		
		return sb;
	}
	
	//鑷畾涔夐厤缃�
	public abstract StringBuilder customeConfig();
}
