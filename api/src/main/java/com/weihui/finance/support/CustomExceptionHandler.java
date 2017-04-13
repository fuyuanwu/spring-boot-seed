package com.weihui.finance.support;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.weihui.finance.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 公共异常处理切面
 */
@ControllerAdvice(annotations = { RestController.class })
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = { ServiceException.class })
    public final ResponseEntity<Object> handleServiceException(ServiceException ex, HttpServletRequest request,
                                                               WebRequest webRequest) {
        // 注入servletRequest，用于出错时打印请求URL与来源地址
        logError(ex, request);

        HttpHeaders headers = new HttpHeaders();
        return handleExceptionInternal(ex, null, headers, HttpStatus.valueOf(ex.errorCode.httpStatus), webRequest);
    }

    @ExceptionHandler(value = { Exception.class })
    public final ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        logError(ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        HttpHeaders headers = new HttpHeaders();
        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * 重载ResponseEntityExceptionHandler的 handleExceptionInternal 方法，加入日志
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        logError(ex);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ErrorResult result = new ErrorResult(status.value(), ex.getMessage());

        if (body instanceof ErrorResult) {
            result = (ErrorResult) body;
        }

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<Object>(result, headers, status);
    }

    private void logError(Exception ex) {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", ex.getMessage());
        logger.error(JSON.toJSONString(map), ex);
    }

    private void logError(Exception ex, HttpServletRequest request) {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", ex.getMessage());
        map.put("from", request.getRemoteAddr());
        String queryString = request.getQueryString();
        map.put("path", queryString != null ? (request.getRequestURI() + "?" + queryString) : request.getRequestURI());

        logger.error(JSON.toJSONString(map), ex);
    }
}
