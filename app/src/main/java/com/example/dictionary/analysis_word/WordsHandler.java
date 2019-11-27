package com.example.dictionary.analysis_word;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WordsHandler extends DefaultHandler {
    private String nodeName;//记录当前节点
    private Words words;
    private StringBuilder posAcceptation;//词义
    private StringBuilder sent;

    public  Words getWords(){
        return words;
    }

    @Override
    public void startDocument() throws SAXException {
        words = new Words();
        posAcceptation = new StringBuilder();
        sent = new StringBuilder();
    }

    @Override
    public void endDocument() throws SAXException {
        words.setPosAcceptation(posAcceptation.toString().trim());
        words.setSent(sent.toString().trim());
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        nodeName = localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //读完整个节点之后换行
        if("acceptation".equals(localName)){
            posAcceptation.append("\n");
        }
        else if ("orig".equals(localName)){
            sent.append("\n");
        }
        else if("trans".equals(localName)){
            sent.append("\n");
            sent.append("\n");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String a = new String(ch,start,length);
        //去掉原文本中的换行
        for(int i =start;i<start+length;i++){
            if(ch[i]=='\n'){
                return;
            }
        }
        //将节点内容存入Words
        if("key".equals(nodeName)){
            words.setKey(a.trim());
        }
        else if("ps".equals(nodeName)){
            if(words.getPsE().length()<=0){
                words.setPsE(a.trim());
            }
            else
                words.setPsA(a.trim());
        }
        else if("pron".equals(nodeName)) {
            if(words.getPronE().length()<=0)
                words.setPronE(a.trim());
            else
                words.setPronA(a.trim());
        }
        else if("pos".equals(nodeName)){
            posAcceptation.append(a);
        }
        else if("acceptation".equals(nodeName)){
            posAcceptation.append(a);
        }
        else if("orig".equals(nodeName)){
            sent.append(a);
        }
        else if("trans".equals(nodeName)){
            sent.append(a);
        }
        else if("fy".equals(nodeName)){
            words.setFy(a);
            words.setIsChinese(true);
        }
    }
}
