package com.vertx.http2.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;

public class Http2VertxLoadClient {
	public static Vertx vertx = Vertx.vertx();
	public static AtomicLong createRequestSent = new AtomicLong(0);
	public static AtomicLong createResponseRec = new AtomicLong(0);
	public static Properties ipconfigProps;
	public static Properties loadconfigProps;

	public static void main(String[] args) {
		HttpClientOptions clientOptions = new HttpClientOptions().setHttp2ClearTextUpgrade(false).setProtocolVersion(HttpVersion.HTTP_2);
		
		FileInputStream ipPort;
		try {
			ipPort = new FileInputStream(
					new File(".." + File.separator + "configuration" + File.separator + "clientipconfig.properties"));
	

		ipconfigProps = new Properties();
		ipconfigProps.load(ipPort);
		ipPort.close();

 		clientOptions.setConnectTimeout(3000);
 	    clientOptions.setLocalAddress(ipconfigProps.getProperty("localip"));
 		 clientOptions.setMaxWaitQueueSize(1000000);
		 clientOptions.setTcpCork(true).setTcpFastOpen(true).setTcpKeepAlive(true).setTcpNoDelay(true)
				.setTcpQuickAck(true).setReuseAddress(true).setReusePort(true);
		HttpClient client= vertx.createHttpClient(clientOptions);
		FileInputStream in = new FileInputStream(
				new File(".." + File.separator + "configuration" + File.separator + "load_test.properties"));

		loadconfigProps = new Properties();
		loadconfigProps.load(in);
		in.close();
		Http2VertxLoadClient loadClient=new Http2VertxLoadClient();
		loadClient.startLoad(loadconfigProps, client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void startLoad(Properties configProps, HttpClient clientInstance) {

		final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(
				Integer.parseInt(configProps.getProperty("clientThreads")));
		exec.prestartAllCoreThreads();
		Http2ClientCallbacksExample clientCallbacksExample=new Http2ClientCallbacksExample();

		if(Boolean.valueOf(configProps.getProperty("sanity"))) {
			Scanner sc=new Scanner(System.in);
			
			while(true) {
				System.out.println(" Press Enter to send request ");
				sc.nextLine();
			new Thread(new RequestSenderTask(clientInstance,clientCallbacksExample,ipconfigProps)).start();
			}
			
 		}else {
 			Timer timer = new Timer();
 			timer.schedule(new ClientLoadListener(createRequestSent, createResponseRec), 1000, 1000);

		for (int client = 0; client < Integer.parseInt(configProps.getProperty("noOfClients")); client++) {
			/*
			 * final ScheduledFuture<?> future = exec.scheduleAtFixedRate(new
			 * RequestSenderTask(clientInstance), 100000, 200, TimeUnit.MICROSECONDS); new
			 * Timer().schedule(new TimerTask() {
			 * 
			 * @Override public void run() { future.cancel(false); } }, 10000);
			 */
			exec.scheduleAtFixedRate(new RequestSenderTask(clientInstance,clientCallbacksExample,ipconfigProps),
					Integer.parseInt(configProps.getProperty("initialdelay")),
					Integer.parseInt(configProps.getProperty("period")),
					TimeUnit.valueOf(configProps.getProperty("timeUnit")));

			/*
			 * exec.scheduleAtFixedRate(new RequestSenderTask2(clientInstance),
			 * Integer.parseInt(configProps.getProperty("initialdelay")),
			 * Integer.parseInt(configProps.getProperty("period")),
			 * TimeUnit.valueOf(configProps.getProperty("timeUnit")));
			 */
		}
		}


	}
}
