package com.example.aspect;

import java.util.concurrent.atomic.AtomicLong;

import io.micrometer.observation.Observation;
import io.micrometer.observation.Observation.Context;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PerformanceTracker  implements ObservationHandler<Observation.Context>{

	private AtomicLong totalResponseTime = new AtomicLong(0);
	private AtomicLong requestCount = new AtomicLong(0);
	
	@Override
	public void onStart (Observation.Context context)
	{
	    /*log.info("Execution started for {}", context.getName());
	    context.put("startTime", System.currentTimeMillis());
	    context.put("creationTime", System.currentTimeMillis());

	    // Log or record latency
	    long latency = System.currentTimeMillis() - (long) context.get("creationTime");
	    log.info("Temp de latence {} - {} ms", context.getName(), latency);*/
	}
	
	@Override
	public void onStop (Observation.Context context)
	{
		/*long endTime = System.currentTimeMillis();
	    long startTime = context.getOrDefault("startTime", 0L);

	    // Log or record response time
	    long responseTime = endTime - startTime;
	    log.info("Temp de réponse pour {} - {} ms", context.getName(), responseTime);
	    
	    // Update total response time and request count
	    totalResponseTime.addAndGet(responseTime);
	    requestCount.incrementAndGet();
	    
	    log.info("Temp de réponse moyen {} ms ",getAverageResponseTime());*/
	}
	
	// Method to get average response time
	public double getAverageResponseTime() {
	    long total = totalResponseTime.get();
	    long count = requestCount.get();

	    // Avoid division by zero
	    return (count > 0) ? (double) total / count : 0.0;
	}
	
	@Override
	public void onError(Observation.Context context) {
		log.info("Error occured {} ", context.getError().getMessage());
	}
	@Override
	public boolean supportsContext(Context context) {
		return true;
	}



}
