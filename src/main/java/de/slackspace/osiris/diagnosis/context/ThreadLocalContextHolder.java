package de.slackspace.osiris.diagnosis.context;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalContextHolder {

	public static final String TRANSACTION_ID = "TransactionId";
	private static final ThreadLocal<Map<String,Object>> THREAD_WITH_CONTEXT = new ThreadLocal<Map<String,Object>>();

    private ThreadLocalContextHolder() {
    }
    
    public static void putTransactionId(String transactionId) {
    	put(TRANSACTION_ID, transactionId);
    }
    
    protected static void put(String key, Object payload) {
        if(THREAD_WITH_CONTEXT.get() == null){

            THREAD_WITH_CONTEXT.set(new HashMap<String, Object>());
        }

        THREAD_WITH_CONTEXT.get().put(key, payload);
    }
    
    public static String getTransactionId() {
    	Object transactionIdObj = get(TRANSACTION_ID);
    	if(transactionIdObj != null) {
    		return (String) transactionIdObj;
    	}
    	
    	return null;
    }

    protected static Object get(String key) {
    	if(THREAD_WITH_CONTEXT.get() != null) {
    		return THREAD_WITH_CONTEXT.get().get(key);	
    	}
        return null;
    }

    public static void cleanupThread(){
        THREAD_WITH_CONTEXT.remove();
    }
}
