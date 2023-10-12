package com.qingfeng.module.base.configure.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qingfeng.module.base.entity.Czlog;
import com.qingfeng.module.base.service.ICzlogService;
import com.qingfeng.module.common.utils.DateTimeUtil;
import com.qingfeng.module.common.utils.GuidUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
@EnableAsync
public class LogAspect {
    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private ICzlogService czlogService;

    private String requestPath = null ; // 请求地址
    private long startTimeMillis = 0; // 开始时间
    private long endTimeMillis = 0; // 结束时间
    private String user = null; // 操作人
    private HttpServletRequest request = null;//请求

    /**
     * @OperationAnnotation(content="获取用户列表信息",sysType=1,opType=5,action="获取用户列表信息")
     * 注解的位置
     */
    @Pointcut("@annotation(com.qingfeng.module.base.configure.aspect.OperationAnnotation)")
    public void logPointCut() {}

    /**
     * @param joinPoint
     * @Description 前置通知  方法调用前触发   记录开始时间,从session中获取操作人
     */
    @Before(value="logPointCut()")
    public void before(JoinPoint joinPoint){
        startTimeMillis = System.currentTimeMillis();
    }
    /**
     * @param joinPoint
     * @Description 获取入参方法参数
     * @return
     */
    public Map<String, Object> getNameAndValue(JoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>();
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            if(paramValues[i] instanceof Integer || paramValues[i] instanceof String) {
                param.put(paramNames[i], paramValues[i]);
            }
        }
        return param;
    }
    /**
     * @param joinPoint
     * @Description 后置通知    方法调用后触发   记录结束时间 ,操作人 ,入参等
     */
    @After(value="logPointCut()")
    public void after(JoinPoint joinPoint) {
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        String users[] = userParams.split(":");
        request = getHttpServletRequest();
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String title = method.getAnnotation(OperationAnnotation.class).content();
        String action = method.getAnnotation(OperationAnnotation.class).action();
        int sysType = method.getAnnotation(OperationAnnotation.class).sysType();
        int opType = method.getAnnotation(OperationAnnotation.class).opType();
        String param = JSON.toJSONString(joinPoint.getArgs(), SerializerFeature.IgnoreNonFieldGetter);
        endTimeMillis = System.currentTimeMillis();
        String reqtime = (endTimeMillis-startTimeMillis)+"ms";
        String reqpath = getHttpServletRequest().getServletPath();
        Czlog czlog = new Czlog();
        czlog.setId(GuidUtil.getGuid());
        czlog.setType("0");
        czlog.setTitle(title);
        czlog.setAction(action);
        czlog.setSysType(sysType+"");
        czlog.setOpType(opType+"");
        czlog.setParam(param);
        czlog.setReqtime(reqtime);
        czlog.setReqpath(reqpath);
        czlog.setCreate_time(DateTimeUtil.getDateTimeStr());
        czlog.setCreate_user(users[1]);
        czlog.setCreate_organize(users[2]);
        czlog.setUser_name(users[0]);
        czlog.setOpera_class(targetName);
        czlog.setOpera_method(methodName);
        czlogService.save(czlog);
        logger.info(JSON.toJSONString(czlog));
    }
    /**
     * @Description: 获取request
     */
    public HttpServletRequest getHttpServletRequest(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        return request;
    }
    /**
     * @param joinPoint
     * @return 环绕通知
     * @throws Throwable
     */
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return null;
    }
    /**
     * @param joinPoint
     * @Description 异常通知
     */
    public void throwing(JoinPoint joinPoint) {
        System.out.println("异常通知");
    }
}