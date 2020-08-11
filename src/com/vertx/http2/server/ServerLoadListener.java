package com.vertx.http2.server;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class ServerLoadListener extends TimerTask {

	private AtomicLong reqLastUpdatedValue = new AtomicLong(0);
	// private AtomicLong ansLastUpdatedValue = new AtomicLong(0);
	private AtomicLong reqCurrentValue;
	private AtomicLong ansCurrentValue;

	public ServerLoadListener(AtomicLong reqCurrentValue, AtomicLong ansCurrentValue) {
		this.reqCurrentValue = reqCurrentValue;
		this.ansCurrentValue = ansCurrentValue;
	}

	@Override
	public void run() {
		// long ansVal = ansCurrentValue.get() - ansLastUpdatedValue.get();
		long ansVal = reqCurrentValue.get() - ansCurrentValue.get();
		long reqVal = reqCurrentValue.get() - reqLastUpdatedValue.get();

		System.out.println(new Date() + " : " + reqVal + " requests received. " + ansVal + " difference."/*+reqCurrentValue+","+ansCurrentValue*/);
		this.reqLastUpdatedValue.set(this.reqCurrentValue.get());
		// this.ansLastUpdatedValue.set(this.ansCurrentValue.get());
	}
}
