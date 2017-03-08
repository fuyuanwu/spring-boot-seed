package com.weihui.finance.support;

public class ErrorResult {

	public int code;
	public String message;

	public ErrorResult() {
	}

	public ErrorResult(int code, String message) {
		this.code = code;
		this.message = message;
	}
}