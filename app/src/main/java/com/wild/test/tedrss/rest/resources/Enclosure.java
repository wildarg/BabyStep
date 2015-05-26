package com.wild.test.tedrss.rest.resources;
/*
 * Created by Wild on 15.05.2015.
 */
//<enclosure url="http://download.ted.com/talks/BurtRutan_2006.mp4" length="66925195" type="video/mp4"

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Element
public class Enclosure {

    @Attribute
    public String url;
    @Attribute
    public Long length;
    @Attribute
    public String type;

}
