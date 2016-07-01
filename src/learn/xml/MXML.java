package learn.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

/**
 * Created by zsh96 on 2016/4/10.
 */
public class MXML {
    public static void main(String[] args) throws DocumentException {
        String text="<a1><a2>SSSS</a2></a1>";
        Document document1= DocumentHelper.parseText(text);
        Element e=document1.getRootElement();
        System.out.println(e.element("a2").getData());
        Document document = DocumentHelper.createDocument();// 创建根节点
        Element root = document.addElement("csdn");
        root=root.addElement("hahaha");
        Element java = root.addElement("java");
        java.setText("java班");
        Element ios = root.addElement("ios");
        ios.setText("ios班");
        root.addElement("peer").setText("1");
        root.addElement("peer").setText("2");
        root.addElement("peer").setText("3");
        List es=root.elements("peer");
        System.out.println(document.asXML());

        Element javaele=document.getRootElement().element("java");

    }
}
