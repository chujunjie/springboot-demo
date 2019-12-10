package com.example.springbootdemo.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * @Description:
 * @Author: chujunjie
 * @Date: Create in 22:23 2019/2/19
 * @Modified By
 */
@Aspect
public class LogAspects {

    @Pointcut("execution(public * com.example.springbootdemo.spring.aop.Goods.*(..))")
    /*切点签名*/
    public void pointCut() {
    }

    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        System.out.println("@Before,执行方法" + joinPoint.getSignature().getName() + "，参数为{"
                + Arrays.asList(args) + "}");
    }

    @After("pointCut()")
    public void logEnd(JoinPoint joinPoint) {
        System.out.println("@After,结束方法" + joinPoint.getSignature().getName());
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public void logReturn(JoinPoint joinPoint, Object result) {
        System.out.println("@AfterReturning,方法" + joinPoint.getSignature().getName()
                + "正常返回, 运行结果为" + result);
    }

    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        System.out.println("@AfterThrowing, 方法" + joinPoint.getSignature().getName()
                + "异常, 异常信息为" + exception.toString());
    }
}
