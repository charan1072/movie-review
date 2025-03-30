package com.example.movies.aspect;


import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class RequestAttributeAspect {

    private final HttpServletRequest httpServletRequest;


    public RequestAttributeAspect(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Around("@annotation(com.example.movies.aspect.annotations.SetRequestAttributes)")
    public Object aroundSetRequestAttribute(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            httpServletRequest.setAttribute("className", className);
            httpServletRequest.setAttribute("methodName", methodName);
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            Parameter[] parameters = method.getParameters();
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                if (parameter.isAnnotationPresent(PathVariable.class)) {
                    httpServletRequest.setAttribute("Id", args[i]);
                    break;
                } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                    Object requestBody = args[i];
                    if (requestBody != null) {
                        Field[] fields = requestBody.getClass().getDeclaredFields();
                        if (fields.length > 0) {
                            fields[1].setAccessible(true);
                            Object entityIdValue = fields[1].get(requestBody);
                            httpServletRequest.setAttribute("Id", entityIdValue);
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

        return joinPoint.proceed();
    }
}
