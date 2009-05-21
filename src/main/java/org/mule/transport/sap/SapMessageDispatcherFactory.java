package org.mule.transport.sap;

import org.mule.api.MuleException;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.transport.MessageDispatcher;
import org.mule.transport.AbstractMessageDispatcherFactory;

/**
 * <code>SapMessageDispatcherFactory</code> is DispatcherFactory
 * for Mule SAP transport.
 */
public class SapMessageDispatcherFactory
    extends AbstractMessageDispatcherFactory
           
{
	/**
     * <code>create</code> method generates a message dispacher
     * for SAP transport.
     *
     * @param endpoint an endpoint for SAP instance
     * @return a new message dispacher instance.
     * @exception MuleException if an error occurs
     */
    public MessageDispatcher create(OutboundEndpoint endpoint)
        throws MuleException {
		return new SapMessageDispatcher(endpoint);
	}
}
