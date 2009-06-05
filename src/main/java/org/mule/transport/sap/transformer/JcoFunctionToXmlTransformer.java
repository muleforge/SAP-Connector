package org.mule.transport.sap.transformer;

import java.util.Date;
import java.io.StringWriter;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.transformer.AbstractTransformer;
import org.mule.api.MuleMessage;

import org.mule.transport.sap.util.MessageConstants;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoParameterField;

import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoRecordFieldIterator;
import com.sap.conn.jco.JCoRecordField;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoStructure;


/**
 * Describe class <code>JcoFunctionToXmlTransformer</code> here.
 *
 * @author <a href="mailto:makoto@zebra"></a>
 * @version 1.0
 */
public class JcoFunctionToXmlTransformer
    extends AbstractMessageAwareTransformer {

    private static Log logger = LogFactory.getLog(JcoFunctionToXmlTransformer.class);
    XMLOutputFactory factory = null;    
    XMLEventFactory eventFactory = null;
    XMLEventWriter writer = null;
    XMLEvent event = null;

    public Object transform(MuleMessage message, String encoding)
        throws TransformerException {
        String result = null;
        JCoFunction function = (JCoFunction)message.getPayload();
        try {
            result = (String)transform(function, encoding);
        } catch(XMLStreamException e) {
            throw new TransformerException(this,e);
        }
        return result;
    }

    /**
     * Describe <code>transform</code> method here.
     *
     * @param obj an <code>Object</code> value
     * @return an <code>Object</code> value
     * @exception XMLStreamException if an error occurs
     */
    public Object transform(Object obj, String encoding) throws XMLStreamException {
        // getting JCoFunction object
        JCoFunction function = (JCoFunction)obj;

        //logger.info(function.getImportParameterList().toXML());
        //logger.info(function.getExportParameterList().toXML());
        //logger.info(function.getTableParameterList().toXML());

        
        factory = XMLOutputFactory.newInstance();
        eventFactory = XMLEventFactory.newInstance(); 

        StringWriter stringWriter = new StringWriter();

        String result = null;
 
        try {
            // getting instance of writer
            writer = factory.createXMLEventWriter(stringWriter);

            // writing start document

            writeStartDocument(writer);
 
            // writing JCO start element

            writeCharacters(writer,"\n");
            writeStartElement(writer,MessageConstants.JCO);

            // writing NAME
            writeAttribute(writer,MessageConstants.JCO_ATTR_NAME,
                           function.getName());

            // writing TIMESTAMP
            writeAttribute(writer,
                           MessageConstants.JCO_ATTR_TIMESTAMP,
                           String.valueOf(new Date().getTime()));

            // writing VERSION
            writeAttribute(writer,
                           MessageConstants.JCO_ATTR_VERSION,
                           MessageConstants.JCO_ATTR_VERSION_VALUE);

            // writing Import

            writeCharacters(writer,"\n"  );
            writeImportElement(writer, function) ;

            // writing Export
            writeCharacters(writer,"\n  ");
            writeExportElement(writer, function) ;

            // writing Tables

            writeCharacters(writer,"\n"  );
            writeTablesElement(writer, function) ;


            // writing Errors

            writeCharacters(writer,"\n  ");

            writeErrorElement(writer, function.getExceptionList()) ;

            // writing JCO end element
            writeEndElement(writer,MessageConstants.JCO);

            // close document

            writeEndDocument(writer);
            writer.flush();
            
            result = stringWriter.toString();
            
        } catch (XMLStreamException ex) {
            throw ex;
        } finally {
            // close
            if (writer != null) {
                try {
                    writer.close();
                } catch (XMLStreamException ex) {}
            }
            if (stringWriter != null) {
                try {
                    stringWriter.close();
                } catch (IOException ex) {}
            }
        }
        return result;
    }
    private void writeImportElement(XMLEventWriter writer,
                                    JCoFunction function) throws XMLStreamException {

        writeStartElement(writer,MessageConstants.IMPORT);

        // writing inside of IMPORT
        writeParameterList(writer, function.getImportParameterList());
        writeEndElement(writer,MessageConstants.IMPORT);
    }

    private void writeExportElement(XMLEventWriter writer,
                                    JCoFunction function) throws XMLStreamException {
        writeStartElement(writer, MessageConstants.EXPORT);
        // writing inside of EXPORT
        writeParameterList(writer, function.getExportParameterList());

        writeEndElement(writer, MessageConstants.EXPORT);
    }

    private void writeTablesElement(XMLEventWriter writer,
                             JCoFunction function) throws XMLStreamException {

        writeStartElement(writer, MessageConstants.TABLES);
        // writing inside of TAVLES
        writeTableParameterList(writer, function.getTableParameterList());

        writeEndElement(writer, MessageConstants.TABLES);
    }

    private void writeParameterList(XMLEventWriter writer,
                                    JCoParameterList parameterList) throws XMLStreamException {
        if (parameterList == null) {
            return;
        }
        
        JCoParameterFieldIterator
            parameterFieldIterator = parameterList.getParameterFieldIterator();

        while (parameterFieldIterator.hasNextField()) {
            // getting STRUCTUTE or FIELD
            JCoParameterField
                parameterField = parameterFieldIterator.nextParameterField();

            if (parameterField.isStructure()){
                // case of STRUCTURE
                String name = parameterField.getName();
                JCoStructure structure = parameterField.getStructure();

                writeStartElement(writer,MessageConstants.STRUCTURE);
                writeAttribute(writer,MessageConstants.STRUCTURE_ATTR_NAME,name);

                JCoRecordFieldIterator
                    recordFieldIterator = structure.getRecordFieldIterator();

                // process FIELDs inside STRUCTURE
                
                while (recordFieldIterator.hasNextField()) {
                    JCoRecordField recordField = recordFieldIterator.nextRecordField();
                    writeField(writer, recordField);
                }
                writeEndElement(writer,MessageConstants.STRUCTURE);

            }  else {
                // case of FIELD
                writeField(writer, parameterField);
            }
        }
    }

    private void writeTableParameterList(XMLEventWriter writer,
                                         JCoParameterList parameterList)
        throws XMLStreamException {
        if (parameterList == null) {
            return;
        }

        JCoParameterFieldIterator
            parameterFieldIterator = parameterList.getParameterFieldIterator();
        
        while (parameterFieldIterator.hasNextField()) {
            JCoParameterField
                parameterField = parameterFieldIterator.nextParameterField();

            JCoTable jcoTable = parameterField.getTable();

            writeStartElement(writer, MessageConstants.TABLE);
            writeAttribute(writer,MessageConstants.TABLE_ATTR_NAME,parameterField.getName());
                
            int numRows = jcoTable.getNumRows();

            for (int i=0; i<numRows; i++)
            {
                // setting a position of row
                
                jcoTable.setRow(i);

                writeStartElement(writer,MessageConstants.ROW);
                writeAttribute(writer, MessageConstants.ROW_ATTR_ID,String.valueOf(i));

                JCoRecordFieldIterator iterator = jcoTable.getRecordFieldIterator();

                while(iterator.hasNextField()) {
                    JCoRecordField recordField = iterator.nextRecordField();
                    writeField(writer, recordField);
                }
                writeEndElement(writer, MessageConstants.ROW);

            }
            writeEndElement(writer,MessageConstants.TABLE);

        }
    }
    private void writeErrorElement(XMLEventWriter writer, AbapException[] exceptions)
        throws XMLStreamException {
        if (exceptions==null) {
            logger.info("There is no exception");
        } else {
            if (exceptions.length > 0)
                {
                    writeStartElement(writer, MessageConstants.ERRORS);

                    for (int i=0; i<exceptions.length; i++)
                        {
                            AbapException abapException = exceptions[i];
                            writeStartElement(writer, MessageConstants.ERROR);
                            writeAttribute(writer, MessageConstants.ERROR_ATTR_KEY,
                                           abapException.getKey());
                            writeCharacters(writer, abapException.getMessage());
                            writeEndElement(writer, MessageConstants.ERROR);
                        }
                    writeEndElement(writer, MessageConstants.ERRORS);
                }
        }
    }
    
    private void writeField(XMLEventWriter writer, JCoField field) throws XMLStreamException {
        writeStartElement(writer,MessageConstants.FIELD);
        writeAttribute(writer,MessageConstants.FIELD_ATTR_NAME,field.getName());
        writeCharacters(writer, field.getString().trim());
        writeEndElement(writer, MessageConstants.FIELD);
    }

    private void writeStartElement(XMLEventWriter writer, String elementName)
        throws XMLStreamException {
        XMLEvent event = eventFactory.createStartElement("", "", elementName);
        writer.add(event);
    }

    private void writeEndElement(XMLEventWriter writer, String elementName)
        throws XMLStreamException {
        XMLEvent event = eventFactory.createEndElement("","", elementName);
        writer.add(event);
    }

    private void writeAttribute(XMLEventWriter writer, String key, String value)
        throws XMLStreamException {
        XMLEvent event = eventFactory.createAttribute(key,value);
        writer.add(event);
    }

    private void writeCharacters(XMLEventWriter writer, String characters)
        throws XMLStreamException {
        XMLEvent event = eventFactory.createCharacters(characters);
        writer.add(event);
    }

    private void writeStartDocument(XMLEventWriter writer) throws XMLStreamException {
        XMLEvent event = eventFactory.createStartDocument();
        writer.add(event);
    }

    private void writeEndDocument(XMLEventWriter writer) throws XMLStreamException {
        XMLEvent event = eventFactory.createEndDocument();
        writer.add(event);
    }

    public static boolean isBapiReturnCodeOkay(final JCoFunction function)
    {
        try
        {
            JCoParameterList parameterList = function.getExportParameterList();

            if ((parameterList != null) && (parameterList.getValue("RETURN")!=null))
            {
                JCoStructure bapiReturn = parameterList.getStructure("RETURN");
                String type = bapiReturn.getString("TYPE");
                if ("".equals(type) || "S".equals(type)==true)  {
                    logger.info("JCo call is ok ");
                } else {
                    logger.info("JCo call is ng ");
                }
                return "".equals(type) || "S".equals(type);
            }
            else
            {
                parameterList = function.getTableParameterList();

                if ((parameterList != null) &&
                    (parameterList.getValue("RETURN")!=null))
                {
                    JCoTable bapiReturn = parameterList.getTable("RETURN");
                    int count = bapiReturn.getNumRows();
                    for (int i=0; i<count; i++)
                    {
                        String type = bapiReturn.getString("TYPE");
                        if (!("".equals(type) || "S".equals(type)))
                            return false;
                    }
                    logger.info("JCo call is ok ");
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            return false;
        }

        return false;
    }

}
