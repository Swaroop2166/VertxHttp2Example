package com.vertx.http2.client;

import java.util.Properties;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;

public class RequestSenderTask implements Runnable {
	public static String message="{\"invocationSequenceNumber\":24354,\"serviceSpecificationInfo\":\"serviceconvextid\",\"nfConsumerIdentification\":{\"nFIPv6Address\":\"2405:200:1413:100:d:10\",\"nFIPv4Address\":\"10.32.131.171\",\"nFName\":\"f185c731-5c2f-47e2-93b3-f6cae7627b08\",\"nodeFunctionality\":\"AMF\",\"nFFqdn\":\"fqdn\",\"nFPLMNID\":{\"mnc\":\"456\",\"mcc\":\"123\"}},\"invocationTimeStamp\":\"2020-05-4T13:18:34Z\",\"multipleUnitUsage\":[{\"ratingGroup\":1234,\"usedUnitContainer\":[{\"downlinkVolume\":78,\"quotaManagementIndicator\":\"OFFLINE_CHARGING\",\"serviceId\":567,\"localSequenceNumber\":0,\"pDUContainerInformation\":{\"afChargingIdentifier\":7000,\"applicationserviceProviderIdentity\":\"server\",\"3gppPSDataOffStatus\":\"ACTIVE\",\"rATType\":\"EUTRA\",\"chargingRuleBaseName\":\"rule A\"}},{\"downlinkVolume\":78,\"quotaManagementIndicator\":\"ONLINE_CHARGING\",\"serviceId\":567,\"localSequenceNumber\":0,\"pDUContainerInformation\":{\"afChargingIdentifier\":7000,\"applicationserviceProviderIdentity\":\"server\",\"3gppPSDataOffStatus\":\"ACTIVE\",\"rATType\":\"EUTRA\",\"chargingRuleBaseName\":\"rule A\"}}],\"requestedUnit\":{\"totalVolume\":1000,\"downlinkVolume\":5000,\"serviceSpecificUnits\":123,\"uplinkVolume\":4000,\"time\":200}}],\"pDUSessionChargingInformation\":{\"pduSessionInformation\":{\"servingCNPlmnId\":{\"mnc\":\"789\",\"mcc\":\"456\"},\"chargingCharacteristics\":\"HOME_DEFAULT\",\"pduAddress\":{\"pduAddressprefixlength\":1,\"pduIPv4Address\":\"10.32.136.137\",\"pduIPv6AddresswithPrefix\":\"2405:200:1413:100:0:0:30:12\",\"iPv4dynamicAddressFlag\":true,\"iPv6dynamicPrefixFlag\":false},\"sscMode\":\"SSC_MODE_1\",\"networkSlicingInfo\":{\"sNSSAI\":{\"sd\":\"sd\",\"sst\":123}},\"ratType\":\"EUTRA\",\"chargingCharacteristicsSelectionMode\":\"HOME_DEFAULT\",\"dnnId\":\"JIONET\",\"pduType\":\"IPV6\",\"hPlmnId\":{\"mnc\":\"456\",\"mcc\":\"123\"},\"servingNetworkFunctionID\":{\"servingNetworkFunctionInformation\":{\"nFIPv6Address\":\"2405:200:1413:100:d:10\",\"nFIPv4Address\":\"10.32.131.171\",\"nFName\":\"f185c731-5c2f-47e2-93b3-f6cae7627b08\",\"nodeFunctionality\":\"AMF\",\"nFFqdn\":\"fqdn\",\"nFPLMNID\":{\"mnc\":\"456\",\"mcc\":\"123\"}}},\"dnnSelectionMode\":\"VERIFIED\",\"3gppPSDataOffStatus\":\"ACTIVE\",\"startTime\":\"2019-12-04T17:14:16.474+05:30\",\"sessionStopIndicator\":false,\"pduSessionID\":123,\"subscribedQoSInformation\":{\"priorityLevel\":12,\"5qi\":12,\"arp\":{\"preemptCap\":\"MAY_PREEMPT\",\"preemptVuln\":\"PREEMPTABLE\"}}},\"chargingId\":123,\"userInformation\":{\"roamerInOut\":\"IN_BOUND\",\"unauthenticatedFlag\":false,\"servedGPSI\":\"msisdn-123456789\",\"servedPEI\":\"494737373737373\"},\"uetimeZone\":\"GMT+05:30\",\"homeProvidedChargingId\":456},\"notifyUri\":\"http://10.32.136.131:8097/\",\"oneTimeEventType\":\"IEC\",\"subscriberIdentifier\":\"IMSI-405854000000000-JIONET\"}\r\n" + 
			"";
	public static String message2="{\"invocationSequenceNumber\":24354}";
	HttpClient client;
	Http2ClientCallbacksExample clientCallbacksExample;
	Properties ipconfigProps;
	RequestSenderTask(HttpClient client,Http2ClientCallbacksExample clientCallbacksExample, Properties ipconfigProps){
		this.client=client;
		this.clientCallbacksExample=clientCallbacksExample;
		this.ipconfigProps=ipconfigProps;
	}
	@Override
	public void run() {


		HttpClientRequest request = client.requestAbs(HttpMethod.POST,"http://" + ipconfigProps.getProperty("remoteip") + ":"
				+ Integer.valueOf(ipconfigProps.getProperty("remoteport")) + "/create",clientCallbacksExample);
			request.setTimeout(5000);
			// Now do stuff with the request
			request.putHeader("content-length",String.valueOf(message.getBytes().length));
			request.putHeader("content-type", "text/plain");
			request.write(Buffer.buffer(message.getBytes()));
			// Make sure the request is ended when you're done with it
			request.end();
			
			
			Http2VertxLoadClient.createRequestSent.getAndIncrement();
			

	}

}
