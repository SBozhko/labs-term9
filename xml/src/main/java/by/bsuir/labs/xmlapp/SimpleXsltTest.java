package by.bsuir.labs.xmlapp;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class SimpleXsltTest {
    private static final String XML_SRC_MAIN_RESOURCES = "xml/src/main/resources/";

    public static void main(String[] args) {

        String dataXML = XML_SRC_MAIN_RESOURCES + "in.xml";
        String inputXSL = XML_SRC_MAIN_RESOURCES + "conv.xsl";
        String outputHTML = XML_SRC_MAIN_RESOURCES + "out.html";

        SimpleXsltTest st = new SimpleXsltTest();
        try {
            st.transform(dataXML, inputXSL, outputHTML);
        } catch (TransformerException e) {
            System.err.println("TransformerException");
            System.err.println(e);
        }
    }

    public void transform(String dataXML, String inputXSL, String outputHTML) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        StreamSource xslStream = new StreamSource(inputXSL);
        Transformer transformer = factory.newTransformer(xslStream);
        StreamSource in = new StreamSource(dataXML);
        StreamResult out = new StreamResult(outputHTML);
        transformer.transform(in, out);
        System.out.println("The generated HTML file is:" + outputHTML);
    }
}
