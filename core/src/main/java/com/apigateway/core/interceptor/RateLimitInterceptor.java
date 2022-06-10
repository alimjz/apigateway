package com.apigateway.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apigateway.core.exception.AuthenticationException;
import com.apigateway.core.exception.LimitExceedException;
import com.apigateway.core.util.RequestMovieTopRank;
import io.github.bucket4j.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {


	private final Bucket bucket;


	public RateLimitInterceptor(){
		Bandwidth bandwidth = Bandwidth.classic(5,Refill.greedy(5,Duration.ofMinutes(1)));
		this.bucket = Bucket4j.builder().addLimit(bandwidth).build();
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception arg3)
			throws Exception {
		log.info("Request is complete");


	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object, ModelAndView model)
			throws Exception {
		log.info("Post Handle Executed.");
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws LimitExceedException {
		log.info("Before Handler execution");

		ConsumptionProbe probe = this.bucket.tryConsumeAndReturnRemaining(1);
		if (probe.isConsumed()) {
			response.addHeader("X-Rate-Limit-Remaining",
					Long.toString(probe.getRemainingTokens()));
			return true;
		}

		response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429
		response.addHeader("X-Rate-Limit-Retry-After-Milliseconds",
				Long.toString(TimeUnit.NANOSECONDS.toMillis(probe.getNanosToWaitForReset())));
		log.info("Handler execution is complete");
		throw new LimitExceedException(RateLimitInterceptor.class);
	}

}