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
package org.mule.transport.sap.transformer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mule.util.IOUtils;

public class XmlToJcoFunctionTransformerTest extends TestCase
{
    public XmlToJcoFunctionTransformerTest(String testName) {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( XmlToJcoFunctionTransformerTest.class );
    }

    public void testParse1 () {
        /*
        try {
            String xml = IOUtils.getResourceAsString("CUSTOMER_GETLIST.xml", this.getClass());
            byte[] xmlBytes = xml.getBytes();

            XmlToJcoFunctionTransformer transformer = new XmlToJcoFunctionTransformer();
            transformer.transform(xmlBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
    public void testParse2 () {
        /*
        try {
            String xml = IOUtils.getResourceAsString("DENPYO.xml", this.getClass());
            byte[] xmlBytes = xml.getBytes();

            XmlToJcoFunctionTransformer transformer = new XmlToJcoFunctionTransformer();
            transformer.transform(xmlBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
    public void testParse3 () {
        /*
        try {
            String xml = IOUtils.getResourceAsString("QUERY_TABLE_DD02T.xml", this.getClass());
            byte[] xmlBytes = xml.getBytes();

            XmlToJcoFunctionTransformer transformer = new XmlToJcoFunctionTransformer();
            transformer.transform(xmlBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
    public void testParse4 () {
        /*
        try {
            String xml = IOUtils.getResourceAsString("result.xml", this.getClass());
            byte[] xmlBytes = xml.getBytes();

            XmlToJcoFunctionTransformer transformer = new XmlToJcoFunctionTransformer();
            transformer.transform(xmlBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

}
