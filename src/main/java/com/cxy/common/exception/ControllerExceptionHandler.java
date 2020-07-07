package com.cxy.common.exception;

import com.cxy.common.enums.ResultStatus;
import com.cxy.common.resultbean.ResultMsg;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: Luo
 * @description:
 * @time: 2020/6/17 20:50
 * Modified By:
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static Logger logger =  LoggerFactory.getLogger(ControllerExceptionHandler.class);

        /**
         * 未认证异常处理
         *
         * @return
         */
        @ResponseBody
        @ExceptionHandler(UnauthenticatedException.class)
        public ResultMsg<String> authenticationException(HttpServletRequest request , Exception e) {
            return ResultMsg.error(ResultStatus.USER_NOT_EXIST);
        }


        /**
         * 未授权异常处理
         *
         * @return
         */
        @ResponseBody
        @ExceptionHandler(value = UnauthorizedException.class)
        public ResultMsg<String> authorizationException(HttpServletRequest request , Exception e) {
            return ResultMsg.error(ResultStatus.USER_NOT_Authorization);
        }

        /**
         * 参数异常处理
         *
         * @return
         */
        @ResponseBody
        @ExceptionHandler(value = MethodArgumentNotValidException.class)
        public ResultMsg<List<FieldError>> ArgumentNotValidException(HttpServletRequest request , MethodArgumentNotValidException  e) {
            ResultMsg<List<FieldError>> result = new ResultMsg<>(ResultStatus.BIND_ERROR);
            List<FieldError> list=e.getBindingResult().getFieldErrors();
            result.setData(list);
            return result;
        }

        /**
         * 所有异常
         *
         * @return
         */
        @ResponseBody
        @ExceptionHandler(value = Exception.class)
        public ResultMsg<String> globalException(HttpServletRequest request , Exception e) {
            logger.info("系统异常，{}",e);
            return ResultMsg.error(ResultStatus.SYSTEM_ERROR);
        }
}
