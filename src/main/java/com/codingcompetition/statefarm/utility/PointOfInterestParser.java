package com.codingcompetition.statefarm.utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.codingcompetition.statefarm.model.PointOfInterest;

public class PointOfInterestParser {

	private DefaultHandler handler = new DefaultHandler() {

		private Map<Object, String> descriptors = new HashMap<Object, String>();

		public void startElement(String uri, String localName, String qName, Attributes attributes) {

			if (qName.equalsIgnoreCase("NODE")) {

				elements.push(attributes.getValue("lon"));
				elements.push(attributes.getValue("lat"));
			}

			if (qName.equalsIgnoreCase("TAG")) {

				elements.push(attributes.getValue("v"));
				elements.push(attributes.getValue("k"));
			}
		}

		public void endElement(String uri, String localName, String qName) {

			if (qName.equalsIgnoreCase("TAG")) {

				descriptors.put(elements.pop(), elements.pop());
			}

			if (qName.equalsIgnoreCase("NODE")) {

				descriptors.put("latitude", elements.pop());
				descriptors.put("longitude", elements.pop());
				objects.push(new PointOfInterest(descriptors));
			}

		}
	};

	// Stacks for storing the elements and objects.
	private Stack<String> elements = new Stack<String>();
	private Stack<PointOfInterest> objects = new Stack<PointOfInterest>();

	public List<PointOfInterest> parse(String fileName) throws IOException, SAXException {

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			saxParser.parse("src/main/resources" + fileName, handler);

			return objects;

		} catch (Exception e) {

			throw new SAXException(e);
		}
	}

}
