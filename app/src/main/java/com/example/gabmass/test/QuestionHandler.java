package com.example.gabmass.test;

/**
 * Created by gabmass on 30/12/2016.
 */

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class QuestionHandler extends DefaultHandler {

    private ArrayList<Question> questionList;
    private ArrayList<String> choiceList;
    private Question question;
    private String currentValue = "";
    private boolean currentElement = false;
    private int correctIndex;

    public QuestionHandler() { //no arguments constructor
    }

    public ArrayList<Question> parse(InputStream is) throws Exception {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(is, this);
        return questionList;
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;

        if (qName.equalsIgnoreCase("Questions")) {
            questionList = new ArrayList<>();
        } else if (qName.equalsIgnoreCase("Question")) {
            question = new Question();
            correctIndex = (Integer.parseInt(attributes.getValue("Correct")) - 1);
            choiceList = new ArrayList<>();
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;

        if (qName.equalsIgnoreCase("Text"))
            question.setText(currentValue.trim());

        else if (qName.equalsIgnoreCase("Choice")) {
            choiceList.add(currentValue.trim());
        } else if (qName.equalsIgnoreCase("Question")) {
            question.setAnswer(choiceList.get(correctIndex));
            question.setChoiceList(choiceList);
            questionList.add(question);
        }
        currentValue = "";
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement) {
            currentValue = currentValue + new String(ch, start, length);
        }
    }
}
