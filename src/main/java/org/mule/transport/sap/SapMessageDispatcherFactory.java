/*
 * $Id$
 *
 * Copyright (c) Osaka Gas Information System Research Institute Co., Ltd.
 * All rights reserved.  http://www.ogis-ri.co.jp/
 * 
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
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
