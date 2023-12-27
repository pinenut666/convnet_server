package net.convnet.server.web;

import cn.dev33.satoken.util.SaResult;
import net.convnet.server.common.result.CommonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常拦截
    @ExceptionHandler
    public CommonResult<Object> handlerException(Exception e) {
        e.printStackTrace();
        return CommonResult.error(e.getMessage());
    }
}
