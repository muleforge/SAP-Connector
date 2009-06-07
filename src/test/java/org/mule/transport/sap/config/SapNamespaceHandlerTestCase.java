package org.mule.transport.sap;

import org.mule.tck.FunctionalTestCase;


public class SapNamespaceHandlerTestCase extends FunctionalTestCase
{
    protected String getConfigResources()
    {
        //TODO You'll need to edit this file to configure the properties specific to your transport
        return "mule-config.xml";
    }

    public void testSapConfig() throws Exception
    {
        /*
        SapConnector c = (SapConnector) muleContext.getRegistry().lookupConnector("SapConnector");
        assertNotNull(c);
        //assertTrue(c.isConnected());
        //assertTrue(c.isStarted());

        //TODO Assert specific properties are configured correctly
        */
    }

}
