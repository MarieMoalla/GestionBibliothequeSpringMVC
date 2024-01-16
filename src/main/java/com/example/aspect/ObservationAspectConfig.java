package com.example.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.observation.aop.ObservedAspect;
import io.micrometer.observation.ObservationRegistry;

@Configuration
public class ObservationAspectConfig {

	
	public ObservedAspect observedAspect (ObservationRegistry observationRegistry)
	{
		observationRegistry.observationConfig().observationHandler(new PerformanceTracker());	
		return new ObservedAspect(observationRegistry);
	}
}

