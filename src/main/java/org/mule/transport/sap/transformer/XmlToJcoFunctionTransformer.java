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

                    logger.debug("START ELEMENT IS FOUND");
                    logger.debug("start localName = "+localName);

                    if (localName.equals(MessageConstants.JCO)) {
                        functionName = getAttributeValue(MessageConstants.JCO_ATTR_NAME,reader);

                        try {
                            function = this.connector.getAdapter().getFunction(functionName);
                        } catch(JCoException e) {
                            throw new XMLStreamException(e);
                        }
                        logger.debug("function name:"+functionName);
                        
                    } else if (functionName!=null) {
                        if (localName.equals(MessageConstants.IMPORT)) {
                            //recordType = IMPORT;

                            push(function.getImportParameterList());
                        } else if (localName.equals(MessageConstants.EXPORT)) {
                            //recordType = EXPORT;
                                                        
                            push(function.getExportParameterList());
                        } else if (localName.equals(MessageConstants.TABLES)) {
                            //recordType = TABLES;
                            tableName = null;
                            push(function.getTableParameterList());

                        } else if (localName.equals(MessageConstants.TABLE)) {
                            if(tableName!=null) {
                                pop();
                            }
                            tableName
                                = getAttributeValue(MessageConstants.TABLE_ATTR_NAME,reader);

                            logger.debug("tableName = "+tableName);
                            push(this.record.getTable(tableName));
                        } else if (localName.equals(MessageConstants.STRUCTURE)) {
                            structureName
                                = getAttributeValue(MessageConstants.STRUCTURE_ATTR_NAME,reader);
                            push(this.record.getStructure(structureName));
                        } else if (localName.equals(MessageConstants.ROW)) {
                            rowId
                                = getAttributeValue(MessageConstants.ROW_ATTR_ID,reader);
                            logger.debug("rowId = "+rowId);
                            if (this.record instanceof JCoTable) {
                                ((JCoTable)this.record).appendRow();
                            }
                        } else if (localName.equals(MessageConstants.FIELD)) {
                            fieldName =
                                getAttributeValue(MessageConstants.STRUCTURE_ATTR_NAME,reader);
                            value = reader.getElementText().trim(); // get an element value
                            logger.debug("FieldName = "+fieldName);
                            logger.debug("value = "+value);

                            this.record.setValue(fieldName,value);

                        }
                    }


                } else if (eventType==XMLStreamReader.END_DOCUMENT) {

                    // find END_DOCUMENT
                    logger.debug("END DOCUMENT IS FOUND");
                } else if (eventType==XMLStreamReader.END_ELEMENT) {
                    logger.debug("END ELEMENT IS FOUND");
                    logger.debug("end localName = "+localName);
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
        } catch (Exception e) {
            logger.fatal(e);
            throw new TransformerException(this,e);
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
            logger.debug("\n"+function.getImportParameterList().toXML());
            logger.debug("\n"+function.getExportParameterList().toXML());
            logger.debug("\n"+function.getTableParameterList().toXML());
            
            return function;
        }
    }

    private String getAttributeValue(String name, XMLStreamReader reader) {
        int count = reader.getAttributeCount();

        if(true) {
            return reader.getAttributeValue(0);
        }
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
