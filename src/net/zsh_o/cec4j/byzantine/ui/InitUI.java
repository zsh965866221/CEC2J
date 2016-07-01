package net.zsh_o.cec4j.byzantine.ui;

import net.zsh_o.cec4j.test.ui.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by zsh96 on 2016/4/17.
 */
public class InitUI extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField textAddress;
    private JTextField textMultiPort;
    private JTextField textServerPort;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            InitUI dialog = new InitUI();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public InitUI() {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        textAddress = new JTextField();
        textAddress.setBounds(210, 27, 113, 24);
        textAddress.setText("224.0.2.2");
        contentPanel.add(textAddress);
        textAddress.setColumns(10);

        JLabel lblNewLabel = new JLabel("\u7EC4\u64AD\u5730\u5740:");
        lblNewLabel.setBounds(103, 30, 72, 18);
        contentPanel.add(lblNewLabel);

        JLabel label = new JLabel("\u7EC4\u64AD\u7AEF\u53E3:");
        label.setBounds(103, 80, 72, 18);
        contentPanel.add(label);

        textMultiPort = new JTextField();
        textMultiPort.setText("9000");
        textMultiPort.setColumns(10);
        textMultiPort.setBounds(210, 77, 113, 24);
        contentPanel.add(textMultiPort);

        JLabel label_1 = new JLabel("\u670D\u52A1\u7AEF\u53E3:");
        label_1.setBounds(103, 130, 72, 18);
        contentPanel.add(label_1);

        textServerPort = new JTextField();
        textServerPort.setText("9001");
        textServerPort.setColumns(10);
        textServerPort.setBounds(210, 127, 113, 24);
        contentPanel.add(textServerPort);

        JButton btnOK = new JButton("\u786E\u5B9A");
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String saddress=textAddress.getText();
                String sMultiPort=textMultiPort.getText();
                String sServerPort=textServerPort.getText();
                try {
                    if(!saddress.equals("")&&!sMultiPort.equals("")&&!sServerPort.equals("")){
                        btnOK.setVisible(false);
                        Main m=new Main(InetAddress.getByName(saddress),Integer.valueOf(sMultiPort),Integer.valueOf(sServerPort));
                        ConfigureNode configureNodeFrame=new ConfigureNode();
                        Thread.sleep(2000);
                        java.util.Timer timer=new java.util.Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                configureNodeFrame.textField.setText(String.valueOf(m.getRemotePeerNumber()));
                            }
                        },1000,3000);
                        m.frame.setTitle(m.peer.myself.toString());
                        configureNodeFrame.setTitle(m.peer.myself.toString());
//                        m.initTreeModel();
                        setVisible(false);
                        configureNodeFrame.setVisible(true);
//                        m.frame.setVisible(true);
                    }
                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        btnOK.setBounds(144, 189, 113, 27);
        contentPanel.add(btnOK);
    }
}