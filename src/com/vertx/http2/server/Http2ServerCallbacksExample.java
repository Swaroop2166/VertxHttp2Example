package com.vertx.http2.server;

import org.json.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

public class Http2ServerCallbacksExample implements Handler<HttpServerRequest> {
	String str="";
	JSONObject obj=null;
	@Override
	public void handle(HttpServerRequest request) {
		// TODO Auto-generated method stub
		Http2VertxLoadServer.createRequestRecFromAMF.getAndIncrement();
		ByteBuf totalBuffer = PooledByteBufAllocator.DEFAULT.directBuffer();
		request.handler(buffer -> {
			totalBuffer.writeBytes(buffer.getByteBuf());
		});
	
		request.endHandler(v -> {
			HttpServerResponse response = request.response();
			response.setStatusCode(200);
			byte[] data = new byte[totalBuffer.readableBytes()];

			try {
				totalBuffer.readBytes(data);
				str = new String(data, "UTF-8");
				obj = new JSONObject(str);
				obj.get("invocationSequenceNumber");
				response.write("Ok");
			//	response.end();
			//	Http2VertxLoadServer.createResponseSentToAMF.getAndIncrement();

			} catch (Exception e) {
				System.out.println("Ended flag " + request.isEnded());
				System.out.println("&&&&&&&&&&7777777 Length Header " + request.getHeader("content-length")
						+ "Length Received " + data.length);

				System.out.println("@@@@@@@@@@@@@**************" + request.response().streamId() + "*********" + obj);
				e.printStackTrace();

				System.exit(0);

			}
		});

	}
}