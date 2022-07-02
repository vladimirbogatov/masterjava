package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.Project;
import ru.javaops.masterjava.xml.schema.User;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainXml {

    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public MainXml() {
    }

    private List<User> userList(String groupName, String xmlFileName) throws IOException, JAXBException {
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource(xmlFileName).openStream());
        List<User> users = payload.getUsers().getUser();
        return users.stream().filter(user -> isUserMemberOfGroup(user, groupName)).sorted((Comparator.comparing(User::getEmail))).collect(Collectors.toList());
    }

    private boolean isUserMemberOfGroup(User user, String groupName) {
        return user.getGroupRefs().stream().anyMatch(o -> {
            try {
                return getFieldNameValue(o).equals(groupName);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getFieldNameValue(Object o) throws NoSuchFieldException, IllegalAccessException {
        Project.Group group = (Project.Group) o;
        return group.getName();
    }

    public void printUsersOfGroupJaxb(String groupName, String xmlFileName) throws JAXBException, IOException {
        userList(groupName, xmlFileName).forEach(System.out::println);
    }

    public void printUserOfGroupStax(String groupName, String xmlFileName) throws IOException, XMLStreamException {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource(xmlFileName).openStream())) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    if ("User".equals(reader.getLocalName())) {
                        String user = reader.getElementText();
                        reader.nextTag();
                        String email = reader.getAttributeValue(null, "flag");
                    }
                }
            }
        }
    }
}
