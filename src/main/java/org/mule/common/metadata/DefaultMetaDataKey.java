/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.common.metadata;

public class DefaultMetaDataKey implements MetaDataKey, TypeMetaDataModel {

    private String id;
    private String displayName;
    private boolean isFromCapable = true;

    public DefaultMetaDataKey(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
        this.isFromCapable = true;
    }

    public DefaultMetaDataKey(String id, String displayName, boolean isFromCapable) {
        this.id = id;
        this.displayName = displayName;
        this.isFromCapable = isFromCapable;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return "DefaultMetaDataKey:{ displayName:" + displayName + " id:" + id + " }";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DefaultMetaDataKey other = (DefaultMetaDataKey) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public int compareTo(MetaDataKey otherMetadataKey) {
        return id.compareTo(otherMetadataKey.getId());
    }

    public boolean isFromCapable() {
        return isFromCapable;
    }
}
