package net.zsh_o.cec4j.byzantine.ui;

import javafx.event.ActionEvent;
import net.zsh_o.cec4j.byzantine.Byzantine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by zsh96 on 2016/4/17.
 */

public class ConfigureNode extends JFrame {

    private JPanel contentPane;
    public JTextField textField;
    public JTextField textField_1;
    private JTextField textField_2;

    JCheckBox chckbxNewCheckBox;
    JCheckBox chckbxNewCheckBox_1;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ConfigureNode frame = new ConfigureNode();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ConfigureNode() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 590, 404);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textField = new JTextField();
        textField.setBounds(293, 75, 37, 24);
        textField.setEditable(false);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel = new JLabel("\u5F53\u524D\u8282\u70B9\u6570\uFF1A");
        lblNewLabel.setBounds(197, 72, 90, 30);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("\u53DB\u5F92\uFF1A");
        lblNewLabel_1.setBounds(240, 178, 64, 19);
        contentPane.add(lblNewLabel_1);

        chckbxNewCheckBox = new JCheckBox("");
        chckbxNewCheckBox.setBounds(293, 178, 25, 25);
        chckbxNewCheckBox.setHorizontalAlignment(SwingConstants.LEFT);

        contentPane.add(chckbxNewCheckBox);

        JButton btnNewButton = new JButton("\u786E\u5B9A");
        btnNewButton.setBounds(250, 291, 102, 30);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnNewButton.setVisible(false);
                //初始化开始拜占庭
                Byzantine byzantine=new Byzantine(Main.MAIN.peer,chckbxNewCheckBox_1.isSelected(),chckbxNewCheckBox.isSelected(),Integer.valueOf(textField_2.getText()));
                byzantine.setSendMessage(textField_1.getText());
                Main.MAIN.byzantine=byzantine;

                setVisible(false);
                Main.MAIN.frame.setVisible(true);
            }
        });
        contentPane.add(btnNewButton);

        JLabel label = new JLabel("\u6307\u4EE4\uFF1A");
        label.setBounds(242, 218, 45, 18);
        contentPane.add(label);

        textField_1 = new JTextField();
        textField_1.setBounds(293, 215, 86, 24);
        contentPane.add(textField_1);
        textField_1.setEditable(false);
        textField_1.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("\u5C06\u519B\uFF1A");
        lblNewLabel_2.setBounds(242, 136, 45, 18);
        contentPane.add(lblNewLabel_2);

        chckbxNewCheckBox_1 = new JCheckBox("");
        {
            chckbxNewCheckBox_1.addChangeListener(new ChangeListener(){

                @Override
                public void stateChanged(ChangeEvent arg0) {
                    // TODO Auto-generated method stub
                    if(chckbxNewCheckBox_1.isSelected()){
                        textField_1.setEditable(true);
                    }else{
                        textField_1.setEditable(false);
                    }

                }});
            chckbxNewCheckBox.addChangeListener(new ChangeListener(){

                @Override
                public void stateChanged(ChangeEvent arg0) {
                    // TODO Auto-generated method stub
                    if(!chckbxNewCheckBox.isSelected()&&chckbxNewCheckBox_1.isSelected()){
                        textField_1.setEditable(true);
                    }else{
                        textField_1.setEditable(false);
                    }

                }});
        }
        chckbxNewCheckBox_1.setBounds(294, 136, 25, 25);
        contentPane.add(chckbxNewCheckBox_1);

        JLabel lblNewLabel_3 = new JLabel("\u53DB\u5F92\u6570\uFF1A");
        lblNewLabel_3.setBounds(228, 252, 64, 18);
        contentPane.add(lblNewLabel_3);

        textField_2 = new JTextField();
        textField_2.setBounds(293, 249, 45, 24);
        textField_2.setText("1");
        contentPane.add(textField_2);
        textField_2.setColumns(10);
    }
}
