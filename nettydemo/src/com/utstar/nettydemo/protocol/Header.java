package com.utstar.nettydemo.protocol;

import java.util.Map;

public class Header {
	//1、校验码
	private int rcrCode = 0xabef0101;
	//2、消息长度
	private int length;
	//3、消息类型
	private byte type;
	//4、消息优先级
	private byte priority;
	//5、附件
	private Map<String, Object> attachment;
	//6、sessionId
	private Long sessionId;
	public int getRcrCode() {
		return rcrCode;
	}
	public void setRcrCode(int rcrCode) {
		this.rcrCode = rcrCode;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getPriority() {
		return priority;
	}
	public void setPriority(byte priority) {
		this.priority = priority;
	}
	public Map<String, Object> getAttachment() {
		return attachment;
	}
	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}
	public Long getSessionId() {
		return sessionId;
	}
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	public Header(int rcrCode, int length, byte type, byte priority,
			Map<String, Object> attachment, Long sessionId) {
		super();
		this.rcrCode = rcrCode;
		this.length = length;
		this.type = type;
		this.priority = priority;
		this.attachment = attachment;
		this.sessionId = sessionId;
	}
	public Header() {
		super();
	}
	@Override
	public String toString() {
		return "Header [rcrCode=" + rcrCode + ", length=" + length + ", type="
				+ type + ", priority=" + priority + ", attachment="
				+ attachment + ", sessionId=" + sessionId + "]";
	}
}
