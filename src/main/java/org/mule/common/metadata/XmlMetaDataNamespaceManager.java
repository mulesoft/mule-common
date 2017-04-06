package org.mule.common.metadata;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.TreeMap;

public class XmlMetaDataNamespaceManager {

    private static final String PREFIX = "ns";

    private Map<String, String> namespacePrefix = new TreeMap<String, String>();

    public XmlMetaDataNamespaceManager() {
    }

    public QName assignPrefixIfNotPresent(QName name) {
        if (name.getNamespaceURI().isEmpty()) {
            return name;
        } else if (!isPrefixDeclared(name)) {
            if (namespacePrefix.containsKey(name.getNamespaceURI())) {
                return new QName(name.getNamespaceURI(), name.getLocalPart(), namespacePrefix.get(name.getNamespaceURI()));
            }
            else {
                int size = namespacePrefix.size();
                String prefix;
                do {
                    prefix = PREFIX + size;
                    size++;
                } while (namespacePrefix.containsKey(prefix));
                namespacePrefix.put(name.getNamespaceURI(), prefix);
                return new QName(name.getNamespaceURI(), name.getLocalPart(), prefix);
            }
        }
        return name;
    }

    public boolean isPrefixDeclared(QName name) {
        return name.getPrefix() != null && !name.getPrefix().isEmpty();
    }
}