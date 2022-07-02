package ru.javaops.masterjava;

import ru.javaops.masterjava.xml.util.MainXml;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * User: gkislin
 * Date: 05.08.2015
 *
 * @link http://caloriesmng.herokuapp.com/
 * @link https://github.com/JavaOPs/topjava
 */
public class Main {
    public static void main(String[] args) throws JAXBException, IOException, XMLStreamException {
        MainXml mainXml = new MainXml();
        mainXml.printUsersOfGroupJaxb("topjava01", "payload.xml");
        System.out.println("-----------Stax--------------");
        mainXml.printUserOfGroupStax("topjava01", "payload.xml");

    }
}
