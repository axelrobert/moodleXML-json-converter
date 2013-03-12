package dcll.mdrlv.jsontoxml;

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


import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * Helper class that can be used for JSON -> XML transformation.
 * <pre>
 * Transformer transformer = 
 * 		TransformerFactory.newInstance()
 *		.newTransformer();
 *	InputSource source = new InputSource(...);
 *	Result result = ...;
 *	transformer.transform(new SAXSource(new JsonXmlReader(
 *		namespace),source), result);
 * </pre>
 */
/**
 * @author Pierre
 *
 */
/**
 * @author Pierre
 *
 */
public class JSONXmlReader implements XMLReader {

    /**
     * ContentHandler .
     */
    private ContentHandler contentHandler;

    /**
     * namespaceUri .
     */
    private final String namespaceUri;

    /**
     * addTypeAttributes.
     */
    private final boolean addTypeAttributes;


    /**
     *
     */
    public JSONXmlReader() {
        this("");
    }

    /**
     * @param aNamespaceUri :
     */
    public JSONXmlReader(final String aNamespaceUri) {
    	this(aNamespaceUri, false);
    }

    /**
     * @param aNamespaceUri :
     * @param aAddTypeAttributes :
     */
    public JSONXmlReader(final String aNamespaceUri,
    		final boolean aAddTypeAttributes) {
    	this.namespaceUri = aNamespaceUri;
		this.addTypeAttributes = aAddTypeAttributes;
	}

	/**
	 * @return :
	 * @param name :
	 * @throws SAXNotRecognizedException :
	 * @throws SAXNotSupportedException :
	 */
	public final boolean getFeature(final String name)
			throws SAXNotRecognizedException,
			SAXNotSupportedException {
        throw new UnsupportedOperationException();
    }

	/**
	 * @param name :
	 * @param value :
	 * @throws SAXNotRecognizedException :
	 * @throws SAXNotSupportedException :
	 */
    public final void setFeature(final String name, final boolean value)
    			throws SAXNotRecognizedException,
    			SAXNotSupportedException {
    }

    /**
     * @return :
     * @param name :
     * @throws SAXNotRecognizedException :
	 * @throws SAXNotSupportedException :
     */
    public final Object getProperty(final String name)
    		throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new UnsupportedOperationException();
    }

    /**
     * @param name :
     * @param value :
     * @throws SAXNotRecognizedException :
	 * @throws SAXNotSupportedException :
     */
    public final void setProperty(final String name, final Object value)
    		throws SAXNotRecognizedException, SAXNotSupportedException {
        //ignore
    }

    /**
     * @param resolver :
     */
    public final void setEntityResolver(final EntityResolver resolver) {
    	 //ignore
    }

    /**
     * @return :
     */
    public final EntityResolver getEntityResolver() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param handler :
     */
    public final void setDTDHandler(final DTDHandler handler) {
        //ignore
    }

    /**
     * @return :
     */
    public final DTDHandler getDTDHandler() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param handler :
     */
    public final void setContentHandler(final ContentHandler handler) {
        this.contentHandler = handler;
    }

    /**
     * @return :
     */
    public final ContentHandler getContentHandler() {
        return contentHandler;
    }

    /**
     * @param handler :
     */
    public void setErrorHandler(final ErrorHandler handler) {
    	 //ignore

    }

    /**
     * @return :
     */
    public final ErrorHandler getErrorHandler() {
        throw new UnsupportedOperationException();
    }


    /**
     * @param input :
     * @throws IOException :
     * @throws SAXException :
     */
    public final void parse(final InputSource input)
    		throws IOException, SAXException {
        JsonParser jsonParser = new JsonFactory().
        		createJsonParser(input.getCharacterStream());
        new JSONSaxAdapter(jsonParser, contentHandler,
        		namespaceUri, addTypeAttributes).parse();

    }


    /**
     * @param systemId :
     * @throws IOException :
     * @throws SAXException :
     */
    public final void parse(final String systemId)
    		throws IOException, SAXException {
        throw new UnsupportedOperationException();
    }

    /**
     * @return :
     */
    public final String getNamespaceUri() {
        return namespaceUri;
    }


}
