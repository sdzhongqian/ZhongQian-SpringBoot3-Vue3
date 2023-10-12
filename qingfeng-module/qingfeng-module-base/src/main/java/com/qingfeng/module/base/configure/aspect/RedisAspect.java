package com.qingfeng.module.base.configure.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qingfeng.module.common.framework.redis.RedisService;
import com.qingfeng.module.common.utils.MD5;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class RedisAspect {
    private final static Logger logger = LoggerFactory.getLogger(RedisAspect.class);

    @Resource
    private RedisService redisService;

    /**
     * 定义切入点，使用了 @RedisCache 的方法
     */
    @Pointcut("@annotation(com.qingfeng.module.base.configure.aspect.RedisCache)")
    public void redisCachePoint() {
    }

    @Pointcut("@annotation(com.qingfeng.module.base.configure.aspect.RedisEvict)")
    public void redisEvictPoint() {
    }

    @After("redisEvictPoint()")
    public void evict(JoinPoint point) {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        // 获取RedisEvict注解
        RedisEvict redisEvict = method.getAnnotation(RedisEvict.class);
        Object[] objects = point.getArgs();
        /***
         * 增加删除List的功能，但是参数列表只能又一个List参数
         */
        if(objects == null || objects.length!=1){
            deleteKey(redisEvict,method,false,objects);
            return;
        }

        Object object = objects[0];
        if(object==null || ! (object instanceof List)){
            deleteKey(redisEvict,method,false,objects);
            return;
        }
        List<Object> list = (List<Object> ) object;
        for (Object item : list) {
            deleteKey(redisEvict,method,true,item);
        }
    }


    /**
     * 删除缓存
     * @param redisEvict 注解
     * @param method 方法名
     * @param isList 参数是否未集合
     * @param arg 参数列表
     */
    private void deleteKey(RedisEvict redisEvict,Method method,Boolean isList,Object... arg){
        if(redisEvict.key().contains("|")){
            String[] keys = redisEvict.key().split("\\|");
            for (String key:keys) {
                String rk = key;
                redisService.delForPrefix(rk);
            }
        }else{
            String rk = redisEvict.key();
            redisService.delForPrefix(rk);
        }
    }
    /**
     * 环绕通知，方法拦截器
     */
    @Around("redisCachePoint()")
    public Object writeReadFromRedis(ProceedingJoinPoint point) {
        try {
            String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
            String[] users = userParams.split(":");
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            // 获取RedisCache注解
            RedisCache redisCache = method.getAnnotation(RedisCache.class);
            Class<?> returnType = ((MethodSignature) point.getSignature()).getReturnType();
            if (redisCache != null && redisCache.read()) {
                // 查询操作
                String fieldKey = MD5.md5(JSON.toJSONString(point.getArgs(), SerializerFeature.IgnoreNonFieldGetter));

                String cacheKey = redisCache.key();
                String user_id = "";
                String organize_id = "";
                if(!userParams.equals("anonymousUser")&&users.length==3){
                    user_id = userParams.split(":")[1];
                    organize_id = userParams.split(":")[2];
                }
                if(cacheKey.contains("organize_id")){
                    cacheKey = cacheKey.replaceAll("organize_id",organize_id);
                }
                if(cacheKey.contains("user_id")){
                    cacheKey = cacheKey.replaceAll("user_id",user_id);
                }
                String rk = cacheKey +":"+redisCache.fieldKey()+ ":" + fieldKey;
                Object obj = redisService.get(rk);
                if (obj == null) {
                    // Redis 中不存在，则从数据库中查找，并保存到 Redis
                    obj = point.proceed();
                    if (obj != null) {
                        long expired = redisCache.expired();
                        if (expired > 0) {
                            //缓解缓存雪崩
                            if(redisCache.expiredType()== 1){
                                expired = expired + (int)(Math.random()*expired);
                            }
                            redisService.set(rk, obj, expired);
                        } else {
                            redisService.set(rk, obj);
                        }
                    }
                } else {
//                    obj = JSONObject.parseObject(obj.toString());
                }
                return obj;
            }
        } catch (Throwable ex) {
            logger.error("<====== RedisCache 执行异常:  ======>", ex);
        }
        return null;
    }
}
