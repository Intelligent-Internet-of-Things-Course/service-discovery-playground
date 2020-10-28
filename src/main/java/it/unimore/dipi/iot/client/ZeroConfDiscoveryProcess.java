package it.unimore.dipi.iot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

public class ZeroConfDiscoveryProcess {

	private final static Logger logger = LoggerFactory.getLogger(ZeroConfDiscoveryProcess.class);

	private ServiceListener serviceListener;

	private String SERVICE_TYPE = "_coap._udp.local.";
	
	//HTTP EXAMPLE
	//String HTTP_SERVICE_TYPE = "_http._tcp.local.";
	
	JmDNS jmdns;

	public ZeroConfDiscoveryProcess(){
		
		serviceListener = new ServiceListener(){

			public void serviceResolved(ServiceEvent ev) {

				String serviceName = ev.getInfo().getQualifiedName();
				String serviceAddress = ev.getInfo().getHostAddresses()[0];
				int servicePort = ev.getInfo().getPort();

				logger.info("mDNS Discovery -> Service resolved: {} - Address: {} - Port: {}", serviceName, serviceAddress, servicePort);
			}

			public void serviceRemoved(ServiceEvent ev) {
				logger.info("mDNS Discovery -> Node removed: {}", ev.getName());
			}

			public void serviceAdded(ServiceEvent event) {
				logger.info("mDNS Discovery -> Service added: {}", event.getName());

				//Request ServiceInfo and its resolution
				jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
			}
		};
		
		try {

			/* Used to bind JMDNS to a single interface
			 * jmdns = JmDNS.create(NetworkInfo.getInetAddress(NetworkInfo.getNetworkAddress(NetworkInfo.IPv4)),"");
			 */
			
			logger.info("mDNS Discovery -> initializing ...");
			
			jmdns = JmDNS.create(null,null);
			jmdns.addServiceListener(SERVICE_TYPE, serviceListener);
			
			logger.info("mDNS Discovery -> Listener ({}) Added !", SERVICE_TYPE);

			Thread.sleep(120*1000);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ZeroConfDiscoveryProcess();
	}
}
