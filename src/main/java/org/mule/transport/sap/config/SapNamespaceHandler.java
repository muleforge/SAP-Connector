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
package org.mule.transport.sap.config;

import org.mule.transport.sap.SapConnector;

import org.mule.config.spring.handlers.AbstractMuleNamespaceHandler;
import org.mule.endpoint.URIBuilder;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Registers a Bean Definition Parser for handling <code><sap:connector></code> elements
 * and supporting endpoint elements.
 */
public class SapNamespaceHandler extends AbstractMuleNamespaceHandler
{
    public void init()
    {
        //registerStandardTransportEndpoints(SapConnector.SAP, URIBuilder.PATH_ATTRIBUTES);
        registerStandardTransportEndpoints(SapConnector.SAP, new String[]{URIBuilder.ADDRESS});
        registerConnectorDefinitionParser(SapConnector.class);
    }
}
