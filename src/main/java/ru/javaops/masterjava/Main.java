package ru.javaops.masterjava;

import ru.javaops.masterjava.service.MailService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * User: gkislin
 * Date: 05.08.2015
 *
 * @link http://caloriesmng.herokuapp.com/
 * @link https://github.com/JavaOPs/topjava
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.format("Hello MasterJava!");
        Set<String> emails = IntStream.range(1, 10).boxed().map(str->"email_" + str).collect(Collectors.toSet());
        String template = "Hello...";
        MailService mailService = new MailService();
        System.out.println(mailService.sendToList(template, emails));
    }
}
