package com.coffee_backend.exception;

/** 业务异常统一父类，方便捕获 / 记录日志 */
public abstract class BaseBusinessException extends RuntimeException {
    public BaseBusinessException(String message) { super(message); }
}
