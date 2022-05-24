package io.javabrains.moviecatalogservice.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;

@Service
public class UserRatingInfo {
	
	@Autowired
    private RestTemplate restTemplate;
	
	
	@HystrixCommand(fallbackMethod="getFallbackUserRating"
//			,commandProperties = {
//	        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "300"),
//	        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
//	        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "6000"),
//	        @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "18000")
//	        }
	)
	public UserRating getUserRating(@PathVariable("userId") String userId) {
		return restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
	}
    
    private UserRating getFallbackUserRating(@PathVariable("userId") String userId) {
    	UserRating userRating = new UserRating();
    	userRating.setUserId(userId);
    	userRating.setRatings(Arrays.asList(
    			new Rating("0",0)
    			));
    	return userRating;
    }
}
