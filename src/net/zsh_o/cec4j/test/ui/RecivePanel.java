package net.zsh_o.cec4j.test.ui;

import net.zsh_o.cec4j.*;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class RecivePanel extends JPanel {
	private Peer peer;

	public boolean isgroup=false;
	public boolean ispeer=false;

	public Group group;
	public RemotePeer remotePeer;

	public JTextArea textAreaRecive;
	public JTextArea textAreaSend;

 	public Pipe pipe;
	/**
	 * Create the panel.
	 */
	public RecivePanel(Peer peer,Group group) {
		this.peer=peer;
		this.group=group;
		isgroup=true;
		init();
	}
	public RecivePanel(Peer peer,RemotePeer remotePeer) {
		this.peer=peer;
		this.remotePeer=remotePeer;
		ispeer=true;
		//初始化管道
		if(peer.getSendPipeList().containsKey(remotePeer)){//不存在此管道,创建
			pipe=peer.getSendPipeList().get(remotePeer);
		}else{
			try {
				pipe=new Pipe(peer,remotePeer,10);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}

		init();
	}

	private void init(){
		setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.6);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane);

		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		textAreaRecive = new JTextArea();
		textAreaRecive.setFont(new Font("宋体", Font.PLAIN, 16));//设置字体，确保自宽一致
		textAreaRecive.setEditable(false);
		scrollPane.setViewportView(textAreaRecive);

		JPanel panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		SpringLayout sl_panel_2 = new SpringLayout();
		panel_2.setLayout(sl_panel_2);

		JPanel panel_3 = new JPanel();
		sl_panel_2.putConstraint(SpringLayout.NORTH, panel_3, 0, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.WEST, panel_3, 0, SpringLayout.WEST, panel_2);
		panel_2.add(panel_3);

		JPanel panel_4 = new JPanel();
		sl_panel_2.putConstraint(SpringLayout.SOUTH, panel_3, -1, SpringLayout.NORTH, panel_4);
		sl_panel_2.putConstraint(SpringLayout.EAST, panel_3, 0, SpringLayout.EAST, panel_4);
		sl_panel_2.putConstraint(SpringLayout.NORTH, panel_4, -34, SpringLayout.SOUTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.WEST, panel_4, 0, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, panel_4, 0, SpringLayout.SOUTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, panel_4, 0, SpringLayout.EAST, panel_2);
		panel_3.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_3.add(scrollPane_1, BorderLayout.CENTER);

		textAreaSend = new JTextArea();
		{
			//添加键盘监听Shift+Enter
			textAreaSend.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					super.keyTyped(e);
				}

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_ENTER&&e.isShiftDown()){
						send();
					}
					super.keyPressed(e);
				}

				@Override
				public void keyReleased(KeyEvent e) {
					super.keyReleased(e);
				}
			});
		}
		scrollPane_1.setViewportView(textAreaSend);
		panel_2.add(panel_4);
		SpringLayout sl_panel_4 = new SpringLayout();
		panel_4.setLayout(sl_panel_4);

		JButton btnSend = new JButton("\u53D1\u9001");
		sl_panel_4.putConstraint(SpringLayout.NORTH, btnSend, 0, SpringLayout.NORTH, panel_4);
		sl_panel_4.putConstraint(SpringLayout.WEST, btnSend, -151, SpringLayout.EAST, panel_4);
		sl_panel_4.putConstraint(SpringLayout.SOUTH, btnSend, 34, SpringLayout.NORTH, panel_4);
		sl_panel_4.putConstraint(SpringLayout.EAST, btnSend, 0, SpringLayout.EAST, panel_4);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});
		panel_4.add(btnSend);
	}
	public void setText(String s){
		textAreaRecive.setText(textAreaRecive.getText()+"\n"+s);
		textAreaRecive.setCaretPosition(textAreaRecive.getText().length());
	}
	public void send(){
		if(isgroup){
			try {
				peer.sendGroupMessage(group,new Message(DocumentHelper.parseText("<cec4j><port>"+peer.getServerPort()+"</port><message><group>"+group.getUuid()+"</group><data>"+textAreaSend.getText()+"</data></message></cec4j>")));
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
		}
		if(ispeer){
			try {
				pipe.sendMessage(new Message(DocumentHelper.parseText("<cec4j>"+textAreaSend.getText()+"</cec4j>")));
				textAreaRecive.setText(textAreaRecive.getText()+"\n"+"["+peer.myself.toString()+"]:"+textAreaSend.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
		}
		textAreaSend.setText("");
	}

}
