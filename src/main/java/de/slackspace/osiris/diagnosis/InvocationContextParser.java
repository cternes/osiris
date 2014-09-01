package de.slackspace.osiris.diagnosis;

import javax.interceptor.InvocationContext;

public class InvocationContextParser {

	public String getParameters(InvocationContext ctx) {
        StringBuilder sb = new StringBuilder();
        
        if(ctx.getParameters() != null) {
        	for (int i = 0; i < ctx.getParameters().length; i++) {
                if(i > 0) {
                    sb.append(", ");
                }
                sb.append(ctx.getParameters()[i].toString());
            }
        }
        
        return sb.toString();
    }
	
	public String getFullMethodName(InvocationContext ctx) {
        String clazzName = ctx.getMethod().getDeclaringClass().getName();
        String methodName = ctx.getMethod().getName();
        
        return String.format("%s:%s", clazzName, methodName);
    }
}
