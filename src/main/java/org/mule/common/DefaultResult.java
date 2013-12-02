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

import java.io.PrintWriter;
import java.io.StringWriter;

public class DefaultResult<T> implements Result<T>
{

    private T obj;
    private Status status;
    private String message;
    private FailureType failureType;
    private String stacktrace;

    public DefaultResult(T obj)
    {
        this(obj, Status.SUCCESS);
    }

    public DefaultResult(T obj, TestResult.Status status)
    {
        this(obj, status, "");
    }

    public DefaultResult(T obj, TestResult.Status status, String message)
    {
        this(obj, status, message, (Status.FAILURE.equals(status)) ? FailureType.UNSPECIFIED : null, null);
    }

    public DefaultResult(T obj, TestResult.Status status, String message, FailureType failureType, Throwable throwable)
    {
        this.obj = obj;
        this.status = status;
        this.message = message;
        this.failureType = failureType;
        if (throwable != null)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            this.stacktrace = sw.toString();
        }
        else
        {
            this.stacktrace = null;
        }
    }

    public T get()
    {
        return obj;
    }

    public String getMessage()
    {
        return message;
    }

    public Status getStatus()
    {
        return status;
    }

    public FailureType getFailureType() {
        return failureType;
    }

    public String getStacktrace() {
        return stacktrace;
    }

}
