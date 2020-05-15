package org.javaboy.vhr.logaop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.OpLog;
import org.javaboy.vhr.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sun.plugin.liveconnect.SecurityContextHelper;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 系统日志，切面处理类
 */
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private SysLogService sysLogService;

    //定义切点@pointcut
    //在注解的地方切入代码
    @Pointcut("@annotation(org.javaboy.vhr.logaop.Operation)")
    public void LogPointCut(){

    }

    //切面，配置通知
    @AfterReturning("LogPointCut()")
    public void saveSysLog(JoinPoint joinPoint){
        //保存日志
        OpLog opLog=new OpLog();

        //从切面织入点通过反射获取织入点方法
        MethodSignature signature=(MethodSignature)joinPoint.getSignature();

        //获取织入点的方法
        Method method = signature.getMethod();

        Operation operation = method.getAnnotation(Operation.class);

        //将操作内容写入到oplog中
        if(operation!=null){
            String value = operation.value();
            opLog.setOperate(value);
        }

        //将操作时间写到oplog中
        opLog.setAdddate(new Date());

        //获取操作人
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Hr hr = (Hr) authentication.getPrincipal();
        opLog.setHrid(hr.getId());

        //保存日志
        sysLogService.saveSysLog(opLog);
    }
}
