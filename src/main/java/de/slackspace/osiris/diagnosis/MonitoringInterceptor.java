package de.slackspace.osiris.diagnosis;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;

import static javax.interceptor.Interceptor.Priority.LIBRARY_BEFORE;

import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Monitored
@Interceptor
@Priority(LIBRARY_BEFORE)
public class MonitoringInterceptor {
    
    private final Map<String, Logger> loggers = new HashMap<>();
    
    @Inject
    InvocationContextParser ctxParser;
    
    @Inject 
    ResultConverter resultConverter;
    
    @AroundInvoke
    public Object logInvocation(InvocationContext ctx) throws Exception {
        String thread = Thread.currentThread().getName();
        Monitored monitored = ctx.getMethod().getAnnotation(Monitored.class);
        Logger logger = findLogger(monitored.layer());
        
        logBeforeProceed(logger, monitored.useCase(), thread, ctx);
        
        long startTime = System.nanoTime();
        Object result = ctx.proceed();
        long executionTimeMs = (System.nanoTime() - startTime) / 1000000;

        logAfterProceed(logger, monitored.useCase(), thread, ctx, executionTimeMs, result);
        
        return result;
    }
    
    private void logBeforeProceed(Logger logger, String useCase, String thread, InvocationContext ctx) {
    	logger.debug("UseCase: [{}] Thread: [{}] Method: [{}] Args: [{}]", useCase, thread, ctxParser.getFullMethodName(ctx), ctxParser.getParameters(ctx));
    }
    
    private void logAfterProceed(Logger logger, String useCase, String thread, InvocationContext ctx, long executionTimeMs, Object result) {
    	String resultString = resultConverter.convertToString(result);
    	
    	logger.debug("UseCase: [{}] Thread: [{}] Method: [{}] Took: [{} ms] Returned: [{}]", useCase, thread, ctxParser.getFullMethodName(ctx), executionTimeMs, resultString);
    }
    
    private Logger findLogger(String layer) {
        if(!loggers.containsKey(layer)) {
            loggers.put(layer, LoggerFactory.getLogger(layer));
        }
        
        return loggers.get(layer);
    }
}
