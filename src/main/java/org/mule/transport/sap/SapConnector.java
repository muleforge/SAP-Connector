package org.mule.transport.sap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.mule.api.MuleException;
import org.mule.transport.AbstractConnector;
import org.mule.api.lifecycle.InitialisationException;

import org.mule.transport.sap.adapter.JcoAdapter;

public class SapConnector extends AbstractConnector
{
    private static Log logger = LogFactory.getLog(AbstractConnector.class);
    public static final String SAP = "sap";

    private JcoAdapter adapter = null;

    private String jcoClient;
    private String jcoUser;
    private String jcoPasswd;
    private String jcoLang;
    private String jcoAshost;
    private String jcoSysnr;
    private boolean jcoTrace = false;
    private int JcoPoolCapacity = 1;
    private int JcoPeakLimit = 1;
    private boolean isTest = false;


	protected void doConnect() throws Exception
    {
        logger.info("************SapConnector.doConnect()*****************");
	}

	protected void doDisconnect() throws Exception
    {
        logger.info("************SapConnector.doDisconnect()*****************");
	}

	protected void doDispose()
    {
        logger.info("************SapConnector.doDispose()*****************");
	}

	protected void doInitialise() throws InitialisationException
    {
        logger.info("************SapConnector.doInitialise()*****************");
	}

	protected void doStart() throws MuleException
    {
        logger.info("************SapConnector.doStart()*****************");
        logger.info("jcoClient:"+getJcoClient());
        logger.info("jcoUser:"+getJcoUser());
        logger.info("jcoLang:"+getJcoLang());
        logger.info("jcoAshost:"+getJcoAshost());
        logger.info("jcoSysnr:"+getJcoSysnr());
        logger.info("jcoTrace:"+isJcoTrace());

        // set sapConnector parameters
        
        this.adapter = new JcoAdapter(this);
        
	}

	protected void doStop() throws MuleException
    {
        logger.info("************SapConnector.doStop()*****************");
	}

	public String getProtocol()
    {
		return SAP;
	}

    /**
     * Gets the value of jcoClient
     *
     * @return the value of jcoClient
     */
    public String getJcoClient()
    {
        
        return this.jcoClient;
    }

    /**
     * Sets the value of jcoClient
     *
     * @param argJcoClient Value to assign to this.jcoClient
     */
    public void setJcoClient(String argJcoClient)
    {
        this.jcoClient = argJcoClient;
    }

    /**
     * Gets the value of jcoAshost
     *
     * @return the value of jcoAshost
     */
    public String getJcoAshost()
    {
        return this.jcoAshost;
    }

    /**
     * Sets the value of jcoAshost
     *
     * @param argJcoAshost Value to assign to this.jcoAshost
     */
    public void setJcoAshost(String argJcoAshost)
    {
        this.jcoAshost = argJcoAshost;
    }

    /**
     * Gets the value of jcoUser
     *
     * @return the value of jcoUser
     */
    public String getJcoUser()
    {
        return this.jcoUser;
    }

    /**
     * Sets the value of jcoUser
     *
     * @param argJcoUser Value to assign to this.jcoUser
     */
    public void setJcoUser(String argJcoUser)
    {
        this.jcoUser = argJcoUser;
    }

    /**
     * Gets the value of jcoPasswd
     *
     * @return the value of jcoPasswd
     */
    public String getJcoPasswd()
    {
        return this.jcoPasswd;
    }

    /**
     * Sets the value of jcoPasswd
     *
     * @param argJcoPasswd Value to assign to this.jcoPasswd
     */
    public void setJcoPasswd(String argJcoPasswd)
    {
        this.jcoPasswd = argJcoPasswd;
    }

    /**
     * Gets the value of jcoLang
     *
     * @return the value of jcoLang
     */
    public String getJcoLang()
    {
        return this.jcoLang;
    }

    /**
     * Sets the value of jcoLang
     *
     * @param argJcoLang Value to assign to this.jcoLang
     */
    public void setJcoLang(String argJcoLang)
    {
        this.jcoLang = argJcoLang;
    }

    /**
     * Gets the value of jcoSysnr
     *
     * @return the value of jcoSysnr
     */
    public String getJcoSysnr()
    {
        return this.jcoSysnr;
    }

    /**
     * Sets the value of jcoSysnr
     *
     * @param argJcoSysnr Value to assign to this.jcoSysnr
     */
    public void setJcoSysnr(String argJcoSysnr)
    {
        this.jcoSysnr = argJcoSysnr;
    }

    /**
     * Gets the value of jcoTrace
     *
     * @return the value of jcoTrace
     */
    public boolean isJcoTrace()
    {
        return this.jcoTrace;
    }

    /**
     * Sets the value of jcoTrace
     *
     * @param argJcoTrace Value to assign to this.jcoTrace
     */
    public void setJcoTrace(boolean argJcoTrace)
    {
        this.jcoTrace = argJcoTrace;
    }

    /**
     * Gets the value of adapter
     *
     * @return the value of adapter
     */
    public JcoAdapter getAdapter()
    {
        return this.adapter;
    }

    /**
     * Sets the value of adapter
     *
     * @param adapter Value to assign to this.adapter
     */
    public void setAdapter(JcoAdapter adapter)
    {
        this.adapter = adapter;
    }

    /**
     * Gets the value of jcoPoolCapacity
     *
     * @return the value of jcoPoolCapacity
     */
    public int getJcoPoolCapacity()
    {
        return this.JcoPoolCapacity;
    }

    /**
     * Sets the value of jcoPoolCapacity
     *
     * @param argJcoPoolCapacity Value to assign to this.jcoPoolCapacity
     */
    public void setJcoPoolCapacity(int argJcoPoolCapacity)
    {
        this.JcoPoolCapacity = argJcoPoolCapacity;
    }

    /**
     * Gets the value of jcoPeakLimit
     *
     * @return the value of jcoPeakLimit
     */
    public int getJcoPeakLimit()
    {
        return this.JcoPeakLimit;
    }

    /**
     * Sets the value of jcoPeakLimit
     *
     * @param argJcoPeakLimit Value to assign to this.jcoPeakLimit
     */
    public void setJcoPeakLimit(int argJcoPeakLimit)
    {
        this.JcoPeakLimit = argJcoPeakLimit;
    }

    /**
     * Gets the value of logger
     *
     * @return the value of logger
     */
    public static Log getLogger() {
        return SapConnector.logger;
    }

    /**
     * Sets the value of logger
     *
     * @param argLogger Value to assign to this.logger
     */
    public static void setLogger(Log argLogger) {
        SapConnector.logger = argLogger;
    }

    /**
     * Gets the value of isTest
     *
     * @return the value of isTest
     */
    public boolean isIsTest() {
        return this.isTest;
    }

    /**
     * Sets the value of isTest
     *
     * @param argIsTest Value to assign to this.isTest
     */
    public void setIsTest(boolean argIsTest) {
        this.isTest = argIsTest;
    }

}
