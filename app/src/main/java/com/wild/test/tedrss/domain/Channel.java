package com.wild.test.tedrss.domain;
/*
 * Created by Wild on 15.05.2015.
 */

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "channel", strict = false)
public class Channel {
    @ElementList(name = "item", inline = true)
    public List<RssItem> itemList;
}
