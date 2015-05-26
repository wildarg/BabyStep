package com.wild.test.tedrss.rest.resources;
/*
 * Created by Wild on 15.05.2015.
 */

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Element
public class Image {
    @Attribute
    public String url;
}
