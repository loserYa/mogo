package com.loser.common.config;

import com.loser.common.entity.ApiResponse;
import com.loser.common.entity.CommonBizStatus;
import com.loser.common.entity.IBizStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerConfig {

    /**
     * 全局异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> exceptionHandler(Throwable e) {

        // 状态码
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        IBizStatus bizStatus = CommonBizStatus.F;

        // 异常消息
        String message = e.getMessage();
        if (StringUtils.isEmpty(message)) {
            message = badRequest.getReasonPhrase();
        }
        log.error("exceptionHandler", e);

        ApiResponse<?> body = ApiResponse.err(bizStatus, message);
        return ResponseEntity
                .status(badRequest)
                .body(body);
    }

}
