package com.ykgroup.dayco.uaa.common.presentation;

import java.net.BindException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.ykgroup.dayco.uaa.auth.exception.InvalidJwtAuthenticationException;
import com.ykgroup.dayco.uaa.common.exception.DataNotFoundException;
import com.ykgroup.dayco.uaa.common.presentation.vo.Error;
import com.ykgroup.dayco.uaa.common.presentation.vo.GlobalMessage;

@Controller
@ControllerAdvice
public class ExceptionControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    /**
     * Exception Class Handler
     * @param request
     * @param message
     * @return
     */
    public ResponseEntity<Error<GlobalMessage>> responseErrorMessage(HttpServletRequest request,
                                                                     GlobalMessage message) {
        final Map<String, String[]> requestMap = request.getParameterMap();
        final StringBuffer requestParameters = new StringBuffer();
        requestMap.forEach((key, values) -> {
                               Optional<String> parameters = Arrays.stream(values).reduce((value1, value2) ->
                                value1 + "," + value2);
                                if(parameters.isEmpty()) {
                                    requestParameters.append("&" + key);
                                } else {
                                    requestParameters.append("&" + key + "=" + parameters);
                                }
                           });
        String href = request.getRequestURL().toString();
        logger.info("href :: " + href);
        logger.info("requestParameters :: " + requestParameters.toString());
        if(requestParameters.toString().length() > 0 ) {
            href += "?" + requestParameters.toString().substring(1);
        }
        Link link = new Link(href, "self");
        Error<GlobalMessage> errorMessage = new Error<>(message);
        errorMessage.add(link);
        return new ResponseEntity<>(
                errorMessage,
                HttpStatus.valueOf(message.getStatus()));
    }

    /**
     * 서버 에러 일 경우
     */
    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(Exception exception, HttpServletRequest request, Locale locale) {
        GlobalMessage message = new GlobalMessage();
        message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        message.setMessage(messageSourceAccessor.getMessage(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())));
        return responseErrorMessage(request, message);
    }

    /**
     * 서버가 Request-URI 와 일치하는 것을 찾지 못했을 경우
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNoHandlerFound(
            NoHandlerFoundException exception, HttpServletRequest request, Locale locale) {
        System.out.println("handleNoHandlerFound");
        GlobalMessage message = new GlobalMessage();
        message.setStatus(HttpStatus.NOT_FOUND.value());
        message.setMessage(messageSourceAccessor.getMessage(String.valueOf(HttpStatus.NOT_FOUND.value())));
        return responseErrorMessage(request, message);
    }

    /**
     * 요청 method 가 다를 경우
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException exception, HttpServletRequest request, Locale locale) {
        GlobalMessage message = new GlobalMessage();
        message.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        message.setMessage(messageSourceAccessor.getMessage(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value())));
        return responseErrorMessage(request, message);
    }

    /**
     * Validation 에러 일 경우
     */
    @ExceptionHandler({ BindException.class, MethodArgumentNotValidException.class, IllegalArgumentException.class })
    public Object validatorExceptionHandler(Exception exception, HttpServletRequest request, Locale locale) {
        GlobalMessage message = new GlobalMessage();
        message.setStatus(HttpStatus.BAD_REQUEST.value());
        message.setMessage(messageSourceAccessor.getMessage(String.valueOf(HttpStatus.BAD_REQUEST.value())));
        return responseErrorMessage(request, message);
    }

    /**
     * 파라미터 타입이 일치하지 않을 경우
     */
    @ExceptionHandler(TypeMismatchException.class)
    public Object typeMismatchExceptionHandler(TypeMismatchException exception, HttpServletRequest request,
                                               Locale locale) {
        GlobalMessage message = new GlobalMessage();
        message.setStatus(HttpStatus.BAD_REQUEST.value());
        message.setMessage(messageSourceAccessor.getMessage(String.valueOf(HttpStatus.BAD_REQUEST.value())));
        return responseErrorMessage(request, message);
    }

    /**
     * 요청에 대한 결과 Data 가 없을 경우
     */
    @ExceptionHandler(DataNotFoundException.class)
    public Object dataNotFoundExceptionHandler(DataNotFoundException exception, HttpServletRequest request, Locale locale) {
        GlobalMessage message = new GlobalMessage();
        message.setStatus(HttpStatus.NO_CONTENT.value());
        message.setMessage(messageSourceAccessor.getMessage(String.valueOf(HttpStatus.NO_CONTENT.value())));
        return responseErrorMessage(request, message);
    }

    /**
     * 인증 실패했을 경우
     */
    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public Object invalidJwtAuthenticationExceptionHandler(InvalidJwtAuthenticationException exception, HttpServletRequest request, Locale locale) {
        GlobalMessage message = new GlobalMessage();
        message.setMessage(messageSourceAccessor.getMessage("auth.error"));
        message.setStatus(HttpStatus.UNAUTHORIZED.value());
        return responseErrorMessage(request, message);
    }

    @RequestMapping(value = "/errors/{errorCode}")
    public Object errorHandler(HttpServletRequest request,
                               @PathVariable int errorCode,
                               Locale locale) throws Exception {
        GlobalMessage message = new GlobalMessage();
        message.setMessage(messageSourceAccessor.getMessage(String.valueOf(errorCode)));
        message.setStatus(errorCode);
        return responseErrorMessage(request, message);
    }

}
