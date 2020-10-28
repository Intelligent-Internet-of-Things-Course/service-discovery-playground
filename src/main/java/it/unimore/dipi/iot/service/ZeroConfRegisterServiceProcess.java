package it.unimore.dipi.iot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public class ZeroConfRegisterServiceProcess {

	private final static Logger logger = LoggerFactory.getLogger(ZeroConfRegisterServiceProcess.class);

	private JmDNS jmdns;

	private String SERVICE_TYPE = "_coap._udp.local.";

	private String SERVICE_NAME = "example";

	private int SERVICE_PORT = 5683;

	//private String proxyService = "_httpcoap._tcp.local.";

	private ServiceInfo service;
	
	public ZeroConfRegisterServiceProcess(){
		
		try {

			/* Bind JmDNS instance to a specific network interface
			 * jmdns = JmDNS.create(NetworkInfo.getInetAddress(NetworkInfo.getNetworkAddress(NetworkInfo.IPv4)),"");
			 */

			logger.info("mDNS Register -> initializing ...");
			
			jmdns = JmDNS.create(null,null);
			service = ServiceInfo.create(SERVICE_TYPE, SERVICE_NAME, SERVICE_PORT, 0 ,0, "");
			jmdns.registerService(service);
			
			logger.info("mDNS Register -> Service: {} registered !", SERVICE_NAME);

			Thread.sleep(60*1000);

			jmdns.unregisterService(service);

			logger.info("mDNS Register -> Service: {} removed !", SERVICE_NAME);

			Thread.sleep(60*1000);
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ZeroConfRegisterServiceProcess();
	}

}
