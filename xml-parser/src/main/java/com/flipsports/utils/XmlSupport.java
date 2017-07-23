package com.flipsports.utils;

import lombok.val;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;

public interface XmlSupport {

    default String readAttribute(StartElement element, String attrName) {
        val attr = element.getAttributeByName(new QName(attrName));
        if (attr != null) {
            return attr.getValue();
        }
        throw new IllegalArgumentException(String.format("Could not find attribute '%s' in element '%s'", attrName, element.toString()));
    }
}
