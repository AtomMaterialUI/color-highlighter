package com.mallowigi.utils;

/*
 * Color Browser - Color browser plugin for IDEA
 * Copyright (C) 2006 Rick Maddy. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class XmlUtil {
  private static DocumentBuilder builder;

  private XmlUtil() {
  }

  @NotNull
  public static Document loadDocument(@NotNull final String resource) throws ParserConfigurationException, IOException,
      SAXException {
    final InputStream stream = XmlUtil.class.getResourceAsStream(resource);
    if (stream == null) {
      throw new FileNotFoundException("Unable to locate resource " + resource);
    }

    return getBuilder().parse(stream);
  }

  @NotNull
  private static synchronized DocumentBuilder getBuilder() throws ParserConfigurationException {
    if (builder == null) {
      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      builder = factory.newDocumentBuilder();
    }

    return builder;
  }
}