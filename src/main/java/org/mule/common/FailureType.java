/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.common;

public class FailureType
{
    public static final FailureType INVALID_CONFIGURATION = new FailureType("INVALID_CONFIGURATION");
    public static final FailureType INVALID_CREDENTIALS = new FailureType("INVALID_CREDENTIALS");
    public static final FailureType NOT_AUTHORIZED = new FailureType("NOT_AUTHORIZED");
    public static final FailureType UNKNOWN_HOST = new FailureType("UNKNOWN_HOST");
    public static final FailureType CONNECTION_FAILURE = new FailureType("CONNECTION_FAILURE");
    public static final FailureType RESOURCE_UNAVAILABLE = new FailureType("RESOURCE_UNAVAILABLE");
    public static final FailureType UNSPECIFIED = new FailureType("UNSPECIFIED");
    public static final FailureType UNKNOWN_METADATA_KEY = new FailureType("UNKNOWN_METADATA_KEY");
    public static final FailureType NO_DATASENSE_AVAILABLE = new FailureType("NO_DATASENSE_AVAILABLE");

    private String name;

    protected FailureType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
