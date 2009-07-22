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
package org.mule.transport.sap.util;

public interface MessageConstants
{
    String JCO                      = "jco";
    String JCO_ATTR_NAME          = "name";
    String JCO_ATTR_VERSION       = "version";
    String JCO_ATTR_VERSION_VALUE = "1.0";
    String JCO_ATTR_TIMESTAMP     = "timestamp";

    String IMPORT                 = "import";
    String EXPORT                 = "export";
    String TABLES                 = "tables";

    String FIELD                  = "field";
    String FIELD_ATTR_NAME        = "name";

    String ROW                    = "row";
    String ROW_ATTR_ID            = "id";

    String STRUCTURE              = "structure";
    String STRUCTURE_ATTR_NAME    = "name";

    String TABLE                  = "table";
    String TABLE_ATTR_NAME        = "name";

    String ERRORS                 = "errors";
    String ERROR                  = "error";
    String ERROR_ATTR_KEY         = "key";
}
