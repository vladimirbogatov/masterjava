package ru.javaops.masterjava;

import ru.javaops.masterjava.xml.util.MainXml;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * User: gkislin
 * Date: 05.08.2015
 *
 * @link http://caloriesmng.herokuapp.com/
 * @link https://github.com/JavaOPs/topjava
 */
public class Main {
    public static void main(String[] args) throws JAXBException, IOException {
        MainXml mainXml = new MainXml();
        mainXml.printUsersOfGroup("topjava01", "payload.xml");
    }
}
