package com.kuangkie.huawei.pojo;

public class Ports {
	/**
	 * 访问端口
	 */
	private int port;
	/**
	 * 监听端口
	 */
	private int target_port;
	private String protocol;
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the target_port
	 */
	public int getTarget_port() {
		return target_port;
	}
	/**
	 * @param target_port the target_port to set
	 */
	public void setTarget_port(int target_port) {
		this.target_port = target_port;
	}
	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}
	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
}
