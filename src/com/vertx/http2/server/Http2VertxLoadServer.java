package com.vertx.http2.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;

public class Http2VertxLoadServer
{
	public static Vertx vertx = Vertx.vertx();
	public static AtomicLong createRequestRecFromAMF = new AtomicLong(0);
	public static AtomicLong createResponseSentToAMF = new AtomicLong(0);

	public static void main(String[] args) {
		HttpServerOptions options = new HttpServerOptions().setLogActivity(true);

		Timer timer = new Timer();
			timer.schedule(new ServerLoadListener(Http2VertxLoadServer.createRequestRecFromAMF,
					Http2VertxLoadServer.createResponseSentToAMF), 1000, 1000);

			FileInputStream ipPort;
			try {
				ipPort = new FileInputStream(
						new File(".." + File.separator + "configuration" + File.separator + "serveripconfig.properties"));
			Properties ipconfigProps = new Properties();
			ipconfigProps.load(ipPort);
			ipPort.close();
			options = new HttpServerOptions().setReuseAddress(true).setReusePort(true).setTcpKeepAlive(true)
					.setTcpNoDelay(true).setUsePooledBuffers(true);
			
			options.setTcpCork(true).setTcpFastOpen(true).setTcpKeepAlive(true).setTcpNoDelay(true)
			.setTcpQuickAck(true).setUsePooledBuffers(true);
		
		Http2Settings clientSettings=options.getInitialSettings();
		clientSettings.setMaxConcurrentStreams(200);
		options.setInitialSettings(clientSettings);
		HttpServer server = vertx.createHttpServer(options);
 		Http2ServerCallbacksExample serverCallbacksExample=new Http2ServerCallbacksExample();
		server.requestHandler(serverCallbacksExample);
		
		server.listen(Integer.valueOf(ipconfigProps.getProperty("localport")), ipconfigProps.getProperty("localip"), res -> {
			  if (res.succeeded()) {
				    System.out.println("Server is now listening!");
				  } else {
				    System.out.println("Failed to bind!");
				  }
				});
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
