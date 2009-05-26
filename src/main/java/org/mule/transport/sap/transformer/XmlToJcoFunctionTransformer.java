package org.mule.transport.sap.transformer;

import java.util.ArrayList;

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
import org.mule.api.transport.Connector;
import org.mule.transport.sap.SapConnector;

import org.mule.transport.sap.util.MessageConstants;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.JCoException;


public class XmlToJcoFunctionTransformer 
    extends AbstractMessageAwareTransformer {


        

    private static Log logger
        = LogFactory.getLog(XmlToJcoFunctionTransformer.class);

    SapConnector connector = null;
    ArrayList records = new ArrayList(3);
    JCoRecord record = null;

    //enum recordType {IMPORT, EXPORT,TABLES};

    public XmlToJcoFunctionTransformer() {
        super();
    }

    public XmlToJcoFunctionTransformer(SapConnector connector) {
        this.connector = connector;
    }

    public Object transform(MuleMessage message, String encoding)
        throws TransformerException {
        if (connector ==null) {
            connector = (SapConnector)this.getEndpoint().getConnector();
        }

        Object obj = message.getPayload();

        JCoFunction function = null;
        if (obj instanceof byte[]) {
            try {
                function = transform(new ByteArrayInputStream((byte[])obj), encoding);
            } catch(XMLStreamException e) {
                throw new TransformerException(this,e);
            }
        }
        return function;
    }

    public JCoFunction transform(InputStream stream, String encoding)
        throws XMLStreamException {

        XMLStreamReader reader = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        String functionName = null;
        String tableName = null;
        String structureName = null;
        String rowId = null;
        String fieldName = null;
        String value = null;

        JCoFunction function = null;
 
        try {
            reader = factory.createXMLStreamReader(stream);
            String localName = null;
            while (reader.hasNext()) {
                int eventType = reader.next();

                if (eventType==XMLStreamReader.START_DOCUMENT) {
                    // find START_DOCUMENT
                    
                } else if (eventType==XMLStreamReader.START_ELEMENT) {
                    // find START_ELEMENT
                    
                    localName = reader.getLocalName();
                    
                    if (localName.equals(MessageConstants.JCO)) {
                        functionName = getAttributeValue(MessageConstants.JCO_ATTR_NAME,reader);

                        try {
                            function = this.connector.getAdapter().getFunction(functionName);
                        } catch(JCoException e) {
                            throw new XMLStreamException(e);
                        }
                        
                        System.out.println("function name:"+functionName);
                    } else if (functionName!=null) {
                        if (localName.equals(MessageConstants.IMPORT)) {
                            //recordType = IMPORT;
                            logger.info("localName = "+localName);

                            push(function.getImportParameterList());
                        } else if (localName.equals(MessageConstants.EXPORT)) {
                            //recordType = EXPORT;
                            logger.info("localName = "+localName);
                                                        
                            push(function.getExportParameterList());
                        } else if (localName.equals(MessageConstants.TABLES)) {
                            //recordType = TABLES;
                            logger.info("localName = "+localName);
                            push(function.getTableParameterList());
                        } else if (localName.equals(MessageConstants.TABLE)) {
                            tableName
                                = getAttributeValue(MessageConstants.TABLE_ATTR_NAME,reader);
                            logger.info("localName = "+localName);
                            logger.info("tableName = "+tableName);
                            push(this.record.getTable(tableName));
                        } else if (localName.equals(MessageConstants.STRUCTURE)) {
                            structureName
                                = getAttributeValue(MessageConstants.STRUCTURE_ATTR_NAME,reader);
                            logger.info("localName = "+localName);
                            push(this.record.getStructure(structureName));
                        } else if (localName.equals(MessageConstants.ROW)) {
                            rowId
                                = getAttributeValue(MessageConstants.ROW_ATTR_ID,reader);
                            logger.info("localName = "+localName);
                            if (this.record instanceof JCoTable) {
                                ((JCoTable)this.record).appendRow();
                            }
                        } else if (localName.equals(MessageConstants.FIELD)) {
                            fieldName =
                                getAttributeValue(MessageConstants.STRUCTURE_ATTR_NAME,reader);
                            value = reader.getElementText().trim(); // get an element value
                            logger.info("localName = "+localName);
                            logger.info("FieldName = "+fieldName);
                            logger.info("value = "+value);
                            this.record.setValue(fieldName,value);

                        }
                    }
                } else if (eventType==XMLStreamReader.END_DOCUMENT) {
                    // find END_DOCUMENT
                } else if (eventType==XMLStreamReader.END_ELEMENT) {
                    // find END_ELEMENT
                    if (localName.equals(MessageConstants.IMPORT) ||
                        localName.equals(MessageConstants.EXPORT) ||
                        localName.equals(MessageConstants.TABLES) ||
                        localName.equals(MessageConstants.TABLE) ||
                        localName.equals(MessageConstants.STRUCTURE))
                        {
                            pop();
                        }
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
            logger.info(function.getImportParameterList().toXML());
            logger.info(function.getExportParameterList().toXML());
                                        
            logger.info(function.getTableParameterList().toXML());
            return function;
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

    private void push(final JCoRecord r)
    {
        if (r != null) {
            if (this.record != null)
                this.records.add(this.record);

            this.record = r;
        }
    }

    private void pop()
    {
        if (!this.records.isEmpty())
            {
            this.record =
                (JCoRecord)records.remove(this.records.size() - 1);
        }
    }
}
