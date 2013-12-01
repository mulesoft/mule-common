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

public class DefaultTestResult extends DefaultResult<Void> implements TestResult
{
    public DefaultTestResult(org.mule.common.Result.Status status)
    {
        super(null, status);
    }

    public DefaultTestResult(TestResult.Status status, String message)
    {
        super(null, status, message);
    }

    public DefaultTestResult(TestResult.Status status, String message, FailureType failureType, Throwable throwable)
    {
        super(null, status, message, failureType, throwable);
    }
}
