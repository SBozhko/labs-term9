package by.bsuir.labs.xmlapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XpathSample {

    private static final String PATH_NAME = "xml/src/main/resources/in.xml";

    public static void main(String[] args) {

        try {
            System.out.println("Loading...");
            FileInputStream file = new FileInputStream(new File(PATH_NAME));

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            Document xmlDocument = builder.parse(file);

            XPath xPath = XPathFactory.newInstance().newXPath();

            System.out.println("Done");

            System.out.println("\nStep 1: find book with id = bk108 and print it");
            String expression = "/catalog/book[@id='bk108']";
            System.out.println("Expression for search: " + expression);
            Node node = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            if (node != null) {
                System.out.println("Book was found!");
                NodeList nodes = node.getChildNodes();

                printNodeList(nodes);

            } else {
                System.out.println("Book not found");
            }

            System.out.println("\nStep 2: find books with price <= 7 and print their titles");

            expression = "/catalog/book[price <= 7]/title";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            System.out.println("Expression: " + expression);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node titleNode = nodeList.item(i);
                System.out.println((i + 1) + ") " + titleNode.getNodeName() + " : " + titleNode.getFirstChild().getNodeValue());
            }

            System.out.println("\nStep 3: find last book");
            expression = "/catalog/book[last()]";
            System.out.println(expression);
            Node book = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
            printNodeList(book.getChildNodes());

            System.out.println("Finished");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private static void printNodeList(NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);

            if (item.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println(nodes.item(i).getNodeName() + " : " + nodes.item(i).getFirstChild().getNodeValue());
            }
        }
    }

}
