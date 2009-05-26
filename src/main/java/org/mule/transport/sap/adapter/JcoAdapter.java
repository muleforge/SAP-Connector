package org.mule.transport.sap.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.mule.transport.sap.SapConnector;

import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;

import com.sap.conn.jco.ext.DestinationDataProvider;


/**
 * This class is to connect to SAP server using JCO Library 3.0.
 * @author <a href="mailto:makoto@zebra"></a>
 * @version 1.0
 */
public class JcoAdapter
{
    private static Log logger = LogFactory.getLog(JcoAdapter.class);
    Properties connectProperties = new Properties();
    
    private JCoRepository repository;
    private JCoDestination destination;
    private SapConnector connector;

    static String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";

    /**
     * This is a constructor for creating a new <code>SapDataSource</code> instance.
     *
     * @param properties properties for SAP JCO

     */
    public JcoAdapter(final SapConnector connector)
    {
        this.connector = connector;
    }

    /**
     * Initialisation JCO connection pool
     *
     * @throws Exception
     */
    public synchronized void doInitialize() throws Exception
    {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST,
                                      this.connector.getJcoAshost());
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,
                                      this.connector.getJcoSysnr());
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,
                                      this.connector.getJcoClient());
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,
                                      this.connector.getJcoUser());
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,
                                      this.connector.getJcoPasswd());
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,
                                      this.connector.getJcoLang());

        if (this.connector.isJcoTrace() == true) {
            connectProperties.setProperty(DestinationDataProvider.JCO_TRACE,"1");
        } else {
            connectProperties.setProperty(DestinationDataProvider.JCO_TRACE,"0");
        }
                                      


        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY,
                                      new Integer(this.connector.getJcoPoolCapacity()).toString());
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,
                                      new Integer(this.connector.getJcoPeakLimit()).toString());

        createDataFile(ABAP_AS_POOLED, "jcoDestination", connectProperties);

        destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        this.repository =  destination.getRepository();
    }

    public Object invoke(Object payload) throws JCoException {
        JCoFunction function = (JCoFunction)payload;
        function.execute(this.destination);
        logger.info(function);
        return function;
    }

    /**
     * Dispose JCO connection pool
     */
    public synchronized void dispose()
    {
    }

    /**
     * Describe <code>createFunction</code> method here.
     *
     * @param name a <code>String</code> value
     * @return a <code>JCO.Function</code> value
     */
    public JCoFunction getFunction(final String name)
        throws JCoException
    {
        JCoFunctionTemplate functionTempl
            = this.repository.getFunctionTemplate(name);

        if (functionTempl == null)
            return null;

        return functionTempl.getFunction();
    }

    /**
     * <code>invoke</code> calls a BAPI request and obtains response.
     *
     * @param function a <code>JCO.Function</code> value
     */
    public void invoke(final JCoFunction function)
        throws JCoException
    {
        function.execute(this.destination);
        return;
    }

    static void createDataFile(String name, String suffix, Properties properties)
    {
        File cfg = new File(name+"."+suffix);
        if(!cfg.exists())
        {
            try
            {
                FileOutputStream fos = new FileOutputStream(cfg, false);
                properties.store(fos, "for connection");
                fos.close();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Unable to create the destination file "
                                                   + cfg.getName(), e);
            }
        }
    }
}
