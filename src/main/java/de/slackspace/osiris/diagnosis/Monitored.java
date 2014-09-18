package de.slackspace.osiris.diagnosis;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 * Logs the method call with its parameters, return values and its execution time.
 * Please note that the layer property defines also the name of the logger, so make sure that 
 * the logger is enabled in your logging configuration. All logging will be done on 'debug' level.
 * <p>
 * Logging will only work on CDI managed beans and public methods.
 * 
 */
@InterceptorBinding
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Inherited
public @interface Monitored {
    
    /**
     * Specifies the application layer where the method call occurs (e.g. BUSINESS, PRESENTATION, DATASTORE...).
     * This will be used as name of the logger.
     * Therefore if you use 'BUSINESS' as value here, a logger called 'BUSINESS' will be used to log the method call.
     * 
     */
    @Nonbinding
    public String layer() default "BUSINESS";
    
    /**
     * Specifies the use-case where the method call occurs.
     * This will be included in the log message.
     * 
     */
    @Nonbinding
    public String useCase() default "N/A";
    
    /**
     * Specifies if request tracing should be started. Every request will be uniquely tagged, 
     * so that you can trace the request through the application. 
     * 
     * This usually only makes sense at your application boundary (REST-Endpoint, Servlet, API-Endpoint ...).
     * 
     */
    @Nonbinding
    public boolean isRequestEntry() default false;
}
