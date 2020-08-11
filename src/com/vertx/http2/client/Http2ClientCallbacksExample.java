package com.vertx.http2.client;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientResponse;

public class Http2ClientCallbacksExample implements Handler<HttpClientResponse>{

	@Override
	public void handle(HttpClientResponse arg0) {
		if(arg0.statusCode()==200)
		  Http2VertxLoadClient.createResponseRec.getAndIncrement();

		
	}
	
	
	
}
