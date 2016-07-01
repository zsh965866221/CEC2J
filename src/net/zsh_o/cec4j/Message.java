package net.zsh_o.cec4j;

import org.dom4j.Document;

/**
 * Created by zsh96 on 2016/4/11.
 */
public class Message {
    private Document document;

    public Message(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
