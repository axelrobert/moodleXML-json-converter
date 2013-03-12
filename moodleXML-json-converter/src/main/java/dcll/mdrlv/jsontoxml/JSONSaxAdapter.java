/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dcll.mdrlv.jsontoxml;

import static org.codehaus.jackson.JsonToken.END_ARRAY;
import static org.codehaus.jackson.JsonToken.END_OBJECT;
import static org.codehaus.jackson.JsonToken.FIELD_NAME;
import static org.codehaus.jackson.JsonToken.START_ARRAY;
import static org.codehaus.jackson.JsonToken.START_OBJECT;
import static org.codehaus.jackson.JsonToken.VALUE_NULL;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Converts JSON to SAX events. It can be used either directly
 * ContentHandler ch = ...;
 * JsonSaxAdapter adapter = new JsonSaxAdapter(
 *		JsonSaxAdapterTest.JSON, ch);
 *	adapter.parse();
 *  or using {@link JsonXmlReader}
 *	Transformer transformer = TransformerFactory.
 *			newInstance().newTransformer();
 *	InputSource source = new InputSource(...);
 *	Result result = ...;
 *	transformer.transform(new SAXSource(
 *			new JsonXmlReader(),source), result);
 */
public class JSONSaxAdapter {

    /**
     *
     */
    private static final AttributesImpl EMPTY_ATTRIBUTES = new AttributesImpl();

    /**
     *
     */
    private final JsonParser jsonParser;

    /**
     *
     */
    private final ContentHandler contentHandler;

    /**
     *
     */
    private final String namespaceUri;

    /**
     *
     */
    private final boolean addTypeAttributes;

    /**
     *
     */
    private static final JsonFactory JSON_FACTORY = new JsonFactory();


    /**
     * @param json :
     * @param aContentHandler :
     */
    public JSONSaxAdapter(final String json,
    		final ContentHandler aContentHandler) {
        this(parseJson(json), aContentHandler);
    }


    /**
     * @param aJsonParser :
     * @param aContentHandler :
     */
    public JSONSaxAdapter(final JsonParser aJsonParser,
    		final ContentHandler aContentHandler) {
        this(aJsonParser, aContentHandler, "");
    }

    /**
     * @param aJsonParser :
     * @param aContentHandler :
     * @param aNamespaceUri :
     */
    public JSONSaxAdapter(final JsonParser aJsonParser,
    		final ContentHandler aContentHandler,
    		final String aNamespaceUri) {
    	this(aJsonParser, aContentHandler, aNamespaceUri, false);
    }

    /**
     * @param aJsonParser :
     * @param aContentHandler :
     * @param aNamespaceUri :
     * @param aAddTypeAttributes :
     */
    public JSONSaxAdapter(final JsonParser aJsonParser,
    		final ContentHandler aContentHandler,
    		final String aNamespaceUri,
    		final boolean aAddTypeAttributes) {
        this.jsonParser = aJsonParser;
        this.contentHandler = aContentHandler;
        this.namespaceUri = aNamespaceUri;
        this.addTypeAttributes = aAddTypeAttributes;
        contentHandler.setDocumentLocator(new DocumentLocator());
    }


    /**
     * @param json :
     * @return :
     */
    private static JsonParser parseJson(final String json) {
        try {
            return JSON_FACTORY.createJsonParser(json);
        } catch (Exception e) {
            throw new ParserException("Parsing error", e);
        }
    }

    /**
     * @throws ParserException :
     * Method parses JSON and emits SAX events.
     */
    public final void parse() throws ParserException {
        try {
            jsonParser.nextToken();
            contentHandler.startDocument();
            int elementsWritten = parseObject();
            if (elementsWritten > 1) {
                throw new ParserException(
                	"More than one root element. "
                +
                	"Can not generate legal XML.");
            }
            contentHandler.endDocument();
        } catch (Exception e) {
            throw new ParserException("Parsing error: " + e.getMessage(), e);
        }
    }

    /**
     * Parses generic object.
     * @return number of elements written
     * @throws IOException :
     * @throws JsonParseException :
     * @throws Exception :
     */
    private int parseObject()
    		throws IOException,
    		JsonParseException,
    		Exception {
        int elementsWritten = 0;
        while (jsonParser.nextToken() != null
        		&& jsonParser.getCurrentToken() != END_OBJECT) {
            if (FIELD_NAME.equals(jsonParser.getCurrentToken())) {
                String elementName = jsonParser.getCurrentName();
                //jump to element value
                jsonParser.nextToken();
                parseElement(elementName);
                elementsWritten++;
            } else {
                throw new ParserException("Error when parsing. "
                		+
                		"Expected field name got "
                		+
                		jsonParser.getCurrentToken());
            }
        }
        return elementsWritten;
    }

    /**
     * @param elementName :
     * @throws Exception :
     */
    private void parseElement(final String elementName) throws Exception {
        startElement(elementName);
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (START_OBJECT.equals(currentToken)) {
            parseObject();
        } else {
        	if (START_ARRAY.equals(currentToken)) {
        		parseArray(elementName);
        	} else {
        		if (currentToken.isScalarValue()) {
        			parseValue();
        		}
        	}
        }
        endElement(elementName);
    }

    /**
     * @param elementName :
     * @throws Exception :
     */
    private void parseArray(final String elementName) throws Exception {

        while (jsonParser.nextToken() != END_ARRAY
        		&& jsonParser.getCurrentToken() != null) {
            parseElement(elementName);
        }
    }

    /**
     * @throws Exception :
     */
    private void parseValue() throws Exception {
        if (VALUE_NULL != jsonParser.getCurrentToken()) {
            String text = jsonParser.getText();
            contentHandler.characters(text.toCharArray(), 0, text.length());
        }
    }


    /**
     * @param elementName :
     * @throws SAXException :
     */
    private void startElement(final String elementName)
    		throws SAXException {
        contentHandler.startElement(namespaceUri,
        		elementName, elementName, getTypeAttributes());
    }


    /**
     * @return :
     */
    protected final Attributes getTypeAttributes() {
    	if (addTypeAttributes) {
			String currentTokenType = getCurrentTokenType();
			if (currentTokenType != null) {
				AttributesImpl attributes =
						new AttributesImpl();
				attributes.addAttribute("", "type",
						"type", "string",
						currentTokenType);
				return attributes;
			} else {
				return EMPTY_ATTRIBUTES;
			}
    	} else {
    		return EMPTY_ATTRIBUTES;
    	}
	}


    /**
     * @return :
     */
    protected final String getCurrentTokenType() {
        switch (jsonParser.getCurrentToken()) {
	        case VALUE_NUMBER_INT: return "int";
	        case VALUE_NUMBER_FLOAT: return "float";
	        case VALUE_FALSE: return "boolean";
	        case VALUE_TRUE: return "boolean";
	        case VALUE_STRING: return "string";
	        case VALUE_NULL: return "null";
	        case START_ARRAY: return "array";
	        default: return null;
        }
    }


	/**
	 * @param elementName :
	 * @throws SAXException :
	 */
	private void endElement(final String elementName) throws SAXException {
        contentHandler.endElement(namespaceUri, elementName, elementName);
    }

    /**
     * @author :
     *
     */
    public static class ParserException extends RuntimeException {
        /**
         *
         */
        private static final long serialVersionUID = 2194022343599245018L;

        /**
         * @param message :
         * @param cause :
         */
        public ParserException(final String message, final Throwable cause) {
            super(message, cause);
        }

        /**
         * @param message :
         */
        public ParserException(final String message) {
            super(message);
        }

        /**
         * @param cause :
         */
        public ParserException(final Throwable cause) {
            super(cause);
        }

    }

    /**
     * @author :
     *
     */
    private class DocumentLocator implements Locator {


    	/**
    	 * @return :
    	 */
        public String getPublicId() {
            Object sourceRef = jsonParser.getCurrentLocation().getSourceRef();
            if (sourceRef != null) {
                return sourceRef.toString();
            } else {
                return "";
            }
        }


        /**
         * @return :
         */
        public String getSystemId() {
            return getPublicId();
        }

        /**
         * @return :
         */
        public int getLineNumber() {
            return jsonParser.getCurrentLocation() != null
            		? jsonParser.getCurrentLocation().getLineNr() : -1;
        }

        /**
         * @return :
         */
        public int getColumnNumber() {
            return jsonParser.getCurrentLocation() != null
            		? jsonParser.getCurrentLocation().getColumnNr() : -1;
        }
    }
}
