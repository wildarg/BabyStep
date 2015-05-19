package com.wild.test.tedrss.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/*
 * Created by Wild on 15.05.2015.
 */
@Root(name = "rss", strict = false)
public class RSS {
    @Element
    public Channel channel;
}
