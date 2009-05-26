package org.mule.transport.sap;

import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.mule.transport.AbstractMessageAdapter;
import org.mule.api.transport.MessageTypeNotSupportedException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Describe class <code>SapMessageAdapter</code> here.
 *
 */
public class SapMessageAdapter extends AbstractMessageAdapter 
{
    private static final long serialVersionUID = 1L;

    private Object payload = null;

    /**
     * Creates a new <code>SapMessageAdapter</code> instance.
     *
     * @param message an <code>Object</code> value
     * @exception MessageTypeNotSupportedException if an error occurs
     */
    public SapMessageAdapter(final Object message)
        throws MessageTypeNotSupportedException
    {
        if (message instanceof byte[]) {
            this.payload = message;
        } else if (message instanceof String) {
            this.payload = message;
        } else if (message instanceof Object) {
            this.payload = message;
        } else {
            throw new MessageTypeNotSupportedException(message, getClass());
        }
    }

    /**
     * Returns the message payload 'as is'.
     * @return an <code>Object</code> value
     */
    public Object getPayload()
    {
        return this.payload;
    }
}
