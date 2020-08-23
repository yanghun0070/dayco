package com.ykgroup.dayco.common.aspect;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ExceptionLogger {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @AfterThrowing(pointcut = "execution(* com.ykgroup.dayco..*.*(..))", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) throws Throwable {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        logger.error("========================= Exception Information Begin =============================== ");
        String requestUrl = request.getScheme() + "://" + request.getServerName() + ":"
                            + request.getServerPort() + request.getRequestURI();

        requestUrl += request.getQueryString() == null ? "" : "?" + request.getQueryString();
        Signature signature = joinPoint.getSignature();
        logger.error("Remote Ip : " + request.getRemoteAddr());
        logger.error("User Id : " + request.getRemoteUser());
        logger.error("request Url :" + requestUrl);

        //request parameter
        Enumeration enumeration = request.getParameterNames();
        while(enumeration.hasMoreElements()) {
            String parameterName =  (String) enumeration.nextElement();
            logger.error("request parameter : {} = {}", parameterName, request.getParameter(parameterName));
        }

        //method arguments
        Object []args = joinPoint.getArgs();
        logger.error("Exception location : " + signature.toLongString());
        for(int i = 0; i < args.length; i++) {
            if(args[i] != null) {
                logger.error("arguments [" + args[i].getClass().getName() + "] = {}", args[i]);
            }
        }
        logger.error("Exception Detail ", e);
        logger.error("========================= Exception Information End =============================== ");
    }

}
