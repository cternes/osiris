package de.slackspace.osiris.diagnosis;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import static javax.interceptor.Interceptor.Priority.LIBRARY_BEFORE;
import javax.interceptor.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Monitored
@Interceptor
@Priority(LIBRARY_BEFORE)
public class MonitoringInterceptor {
    
    private final Map<String, Logger> loggers = new HashMap<>();
    
    @AroundInvoke
    public Object logInvocation(InvocationContext ctx) throws Exception {
        String thread = Thread.currentThread().getName();
        Monitored monitored = ctx.getMethod().getAnnotation(Monitored.class);
        
        Logger logger = findLogger(monitored.layer());
        logger.debug("UseCase: [{}] Thread: [{}] Method: [{}] Args: [{}]", monitored.useCase(), thread, getFullMethodName(ctx), getParameters(ctx));
        
        long startTime = System.nanoTime();
        Object result = ctx.proceed();
        long executionTimeMs = (System.nanoTime() - startTime)/1000;

        logger.debug("UseCase: [{}] Thread: [{}] Method: [{}] Took: [{} ms] Returned: [{}]", monitored.useCase(), thread, getFullMethodName(ctx), executionTimeMs, result);
        
        return result;
    }
    
    private String getFullMethodName(InvocationContext ctx) {
        String clazzName = ctx.getMethod().getDeclaringClass().getName();
        String methodName = ctx.getMethod().getName();
        
        return String.format("%s:%s", clazzName, methodName);
    }
    
    private String getParameters(InvocationContext ctx) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < ctx.getParameters().length; i++) {
            if(i > 0) {
                sb.append(", ");
            }
            sb.append(ctx.getParameters()[i].toString());
        }
        
        return sb.toString();
    }
    
    private Logger findLogger(String layer) {
        if(!loggers.containsKey(layer)) {
            loggers.put(layer, LoggerFactory.getLogger(layer));
        }
        
        return loggers.get(layer);
    }
}
