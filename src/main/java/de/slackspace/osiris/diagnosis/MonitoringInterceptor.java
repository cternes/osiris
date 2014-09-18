package de.slackspace.osiris.diagnosis;

import static javax.interceptor.Interceptor.Priority.LIBRARY_BEFORE;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.slackspace.osiris.diagnosis.context.ThreadLocalContextHolder;

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
        createTransactionIdIfNecessary(monitored.isRequestEntry());
        Logger logger = findLogger(monitored.layer());
        
        String transactionId = ThreadLocalContextHolder.getTransactionId();
        logBeforeProceed(logger, monitored.useCase(), thread, ctx, transactionId);
        
        long startTime = System.nanoTime();
        Object result = ctx.proceed();
        long executionTimeMs = (System.nanoTime() - startTime) / 1000000;

        logAfterProceed(logger, monitored.useCase(), thread, ctx, executionTimeMs, result, transactionId);
        
        return result;
    }

	private void createTransactionIdIfNecessary(boolean isRequestEntry) {
		if(isRequestEntry) {
			ThreadLocalContextHolder.putTransactionId(UUID.randomUUID().toString());
		}
	}
    
    private void logBeforeProceed(Logger logger, String useCase, String thread, InvocationContext ctx, String transactionId) {
    	logger.debug("TransactionId: [{}] UseCase: [{}] Thread: [{}] Method: [{}] Args: [{}]", transactionId, useCase, thread, ctxParser.getFullMethodName(ctx), ctxParser.getParameters(ctx));
    }
    
    private void logAfterProceed(Logger logger, String useCase, String thread, InvocationContext ctx, long executionTimeMs, Object result, String transactionId) {
    	String resultString = resultConverter.convertToString(result);
    	
    	logger.debug("TransactionId: [{}] UseCase: [{}] Thread: [{}] Method: [{}] Took: [{} ms] Returned: [{}]", transactionId, useCase, thread, ctxParser.getFullMethodName(ctx), executionTimeMs, resultString);
    }
    
    private Logger findLogger(String layer) {
        if(!loggers.containsKey(layer)) {
            loggers.put(layer, LoggerFactory.getLogger(layer));
        }
        
        return loggers.get(layer);
    }
}
