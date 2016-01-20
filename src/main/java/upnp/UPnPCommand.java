/*
UPnP Cmd - A Gogo shell command for inspecting UPnP devices
 
Copyright (C) 2016 Didier DONSEZ
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package upnp;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Context;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.upnp.UPnPAction;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPEventListener;
import org.osgi.service.upnp.UPnPService;
import org.osgi.service.upnp.UPnPStateVariable;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component(immediate = true)
// @Instantiate
@Provides(specifications = UPnPCommand.class)
public class UPnPCommand
{

	private Map<String, ServiceRegistration> uPnPEventListeners = new HashMap<String, ServiceRegistration>();

	@Context
	private BundleContext bundleContext;

	@Requires(optional=true)
	private UPnPDevice[] m_upnpdevices;

	@ServiceProperty(name = "osgi.command.scope", value = "upnp")
	String scope;

	@ServiceProperty(name = "osgi.command.function", value = "{}")
	String[] function = new String[]
	{ "devices", "services", "statevariables", "actions", "subscribe",
			"unsubscribe" };

	@Descriptor("devices")
	public void devices()
	{
		System.out.println("UPnP Devices:");
		for (int i = 0; i < m_upnpdevices.length; i++)
		{
			UPnPDevice device = m_upnpdevices[i];
			java.util.Dictionary descriptions = device.getDescriptions(null);

			System.out.println("Device UDN: "
					+ descriptions.get(UPnPDevice.UDN));

			// list the properties
			System.out.println("- Properties:");
			for (Enumeration e = descriptions.keys(); e.hasMoreElements();)
			{
				Object key = e.nextElement();
				Object value = descriptions.get(key);
				System.out.println("\t" + key + " = " + value);
			}

			// list the services
			System.out.println("- Services:");
			UPnPService[] services = device.getServices();
			for (int s = 0; s < services.length; s++)
			{
				UPnPService service = services[s];
				System.out.println("\tService : " + service.getId());
				System.out.println("\t\ttype : " + service.getType());

				// list the state variables name
				System.out.println("\t\tState Variables : ");
				UPnPStateVariable[] stateVariables = service
						.getStateVariables();
				for (int v = 0; v < stateVariables.length; v++)
				{
					System.out.println("\t\t\t" + stateVariables[v].getName());
				}

				// list the actions name
				System.out.println("\t\tActions : ");
				UPnPAction[] actions = service.getActions();
				for (int a = 0; a < actions.length; a++)
				{
					System.out.println("\t\t\t" + actions[a].getName());
				}
			}

			System.out.println();
		}
	}

	@Descriptor("services")
	public void services(String deviceId)
	{
		System.out.println("NOT IMPLEMENTED !");
	}

	@Descriptor("statevariables")
	public void statevariables(String deviceId, String serviceId)
	{
		System.out.println("NOT IMPLEMENTED !");
	}

	@Descriptor("actions")
	public void actions(String deviceId, String serviceId)
	{
		System.out.println("NOT IMPLEMENTED !");
	}

	/**
	 * Subscribe to the state variable changes of a service
	 * 
	 * @param deviceId
	 * @param serviceId
	 */
	@Descriptor("subscribe")
	public void subscribe(String deviceId, String serviceId)
	{
		UPnPEventListener uPnPEventListener = new UPnPEventListener()
		{

			public void notifyUPnPEvent(java.lang.String deviceId,
					java.lang.String serviceId, java.util.Dictionary events)
			{
				System.out.println("UPnP Notification from " + deviceId + " : "
						+ serviceId + " - " + events.toString());
			}
		};
		Dictionary dict = new Properties();
		dict.put(UPnPDevice.ID, deviceId); // The ID of a specific device to
											// listen for events.
		dict.put(UPnPService.ID, serviceId); // The ID of a specific service to
												// listen for events.
		ServiceRegistration serviceRegistration = bundleContext
				.registerService(UPnPEventListener.class.getName(),
						uPnPEventListener, dict);
		uPnPEventListeners.put(deviceId + " " + serviceId, serviceRegistration);
		System.out.println("Subscribe to events from " + deviceId + " "
				+ serviceId);

	}

	/**
	 * Unsubscribe to the state variable changes of a service
	 * 
	 * @param deviceId
	 * @param serviceId
	 */
	@Descriptor("unsubscribe")
	public void unsubscribe(String deviceId, String serviceId)
	{
		ServiceRegistration serviceRegistration = uPnPEventListeners
				.remove(deviceId + " " + serviceId);
		if (serviceRegistration != null)
		{
			serviceRegistration.unregister();
			System.out.println("Unsubscribe to events from " + deviceId + " "
					+ serviceId);
		}
		else
		{
			System.out.println("No subscribing for " + deviceId + " "
					+ serviceId);
		}
	}

}
