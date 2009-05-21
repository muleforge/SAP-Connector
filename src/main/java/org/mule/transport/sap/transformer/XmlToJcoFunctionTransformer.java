package org.mule.transport.sap.transformer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.stream.XMLInputFactory;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.transformer.AbstractTransformer;
import org.mule.api.MuleMessage;

import org.mule.transport.sap.util.MessageConstants;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.JCoException;


public class XmlToJcoFunctionTransformer extends AbstractMessageAwareTransformer {
    //enum recordType {IMPORT, EXPORT,TABLES};

    private static Log logger
        = LogFactory.getLog(XmlToJcoFunctionTransformer.class);

    public Object transform(MuleMessage message, String encoding)
        throws TransformerException {

        Object obj = message.getPayload();

        JCoFunction function = null;
        if (obj instanceof byte[]) {
            try {
                function = tranform(new ByteArrayInputStream((byte[])obj), encoding);
            } catch(XMLStreamException e) {
                throw new TransformerException(this,e);
            }
        }
        return function;
    }

    public JCoFunction transform(ByteArrayInputStream stream, String encoding)
        throws XMLStreamException {

        XMLStreamReader reader = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        String functionName = null;
        String tableName = null;
        String structureName = null;
        String rowId = null;
        String fieldName = null;
        String value = null;

        try {
            reader = factory.createXMLStreamReader(stream);

            while (reader.hasNext()) {
                int eventType = reader.next();

                if (eventType==XMLStreamReader.START_DOCUMENT) {
                    // find START_DOCUMENT
                    
                } else if (eventType==XMLStreamReader.START_ELEMENT) {
                    // find START_ELEMENT
                    
                    String localName = reader.getLocalName();
                    if (localName.equals(MessageConstants.JCO)) {
                        functionName = getAttributeValue(MessageConstants.JCO_ATTR_NAME,reader);
                        System.out.println("function name:"+functionName);
                    } else if (functionName!=null) {
                        if (localName.equals(MessageConstants.IMPORT)) {
                            //recordType = IMPORT;
                            System.out.println(localName);
                        } else if (localName.equals(MessageConstants.EXPORT)) {
                            //recordType = EXPORT;
                            System.out.println(localName);
                        } else if (localName.equals(MessageConstants.TABLES)) {
                            //recordType = TABLES;
                            System.out.println(localName);
                        } else if (localName.equals(MessageConstants.TABLE)) {
                            tableName
                                = getAttributeValue(MessageConstants.TABLE_ATTR_NAME,reader);
                            System.out.println("table:"+tableName);
                        } else if (localName.equals(MessageConstants.STRUCTURE)) {
                            structureName
                                = getAttributeValue(MessageConstants.STRUCTURE_ATTR_NAME,reader);
                            System.out.println("structure:"+structureName);
                        } else if (localName.equals(MessageConstants.ROW)) {
                            rowId
                                = getAttributeValue(MessageConstants.ROW_ATTR_ID,reader);
                            System.out.println("row:"+rowId);
                        } else if (localName.equals(MessageConstants.FIELD)) {
                            fieldName = getAttributeValue(MessageConstants.STRUCTURE_ATTR_NAME,reader);
                            value = reader.getElementText().trim(); // get an element value
                            System.out.println(fieldName+":"+value);
                        }
                    }
                } else if (eventType==XMLStreamReader.END_DOCUMENT) {
                    // find END_DOCUMENT
                } else if (eventType==XMLStreamReader.END_ELEMENT) {
                    // find END_ELEMENT
                } 
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException ex) {}
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {}
            }
        }
    }

    private String getAttributeValue(String name, XMLStreamReader reader) {
        int count = reader.getAttributeCount();
        
        for (int i = 0; i < count; i++) {
            if (reader.getAttributeLocalName(i).equals(name)) {
                String value = reader.getAttributeValue(i);
                return value;
            }

        }
        return null;
    }

    public static void main(String[] args) {
        XmlToJcoFunctionTransformer transformter = new XmlToJcoFunctionTransformer();
    }

}