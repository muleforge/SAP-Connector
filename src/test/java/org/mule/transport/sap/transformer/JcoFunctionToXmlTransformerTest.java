package org.mule.transport.sap.transformer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mule.util.IOUtils;

public class JcoFunctionToXmlTransformerTest extends TestCase
{
    public JcoFunctionToXmlTransformerTest(String testName) {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite( JcoFunctionToXmlTransformerTest.class );
    }

    public void testParse1 () {
        /*
        try {
            JcoFunctionToXmlTransformer transformer = new JcoFunctionToXmlTransformer();
            String xmlString = (String)transformer.transform(null);
            System.out.println("result");
            System.out.println(xmlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}
