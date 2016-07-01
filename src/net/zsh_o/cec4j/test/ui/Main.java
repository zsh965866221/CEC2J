package net.zsh_o.cec4j.test.ui;

import net.zsh_o.cec4j.Group;
import net.zsh_o.cec4j.listener.ZGroupAdvListener;
import net.zsh_o.cec4j.listener.ZGroupMessageListener;
import net.zsh_o.cec4j.listener.ZPipeAdvListener;
import net.zsh_o.cec4j.listener.ZPipeListener;
import net.zsh_o.cec4j.Peer;
import net.zsh_o.cec4j.Pipe;
import net.zsh_o.cec4j.RemotePeer;
import net.zsh_o.cec4j.test.ui.util.tabbedPane.Tab;
import net.zsh_o.cec4j.test.ui.util.tabbedPane.TabbedPane;
import net.zsh_o.cec4j.test.ui.util.tabbedPane.TabbedPaneListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JSplitPane;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JTree;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class Main {

	public JFrame frame;
	
	public static Main MAIN;
	public Peer peer;

	public TabbedPane tabbedPane;
	public JTree tree;

	public DefaultTreeModel treeModel;
	/**
	 * Create the application.
	 */
	public Main(InetAddress address,int multiPort,int serverPort) {
		initialize();
		MAIN=this;
		try {
			peer=new Peer(serverPort,address,multiPort);
			peer.advServer.registeListener(new ZGroupAdvListener(peer));
			peer.advServer.registeListener(new ZPipeAdvListener());
			peer.advServer.registeListener(new ZGroupMessageListener());
			peer.pipeServer.registeListener(new ZPipeListener());
			peer.advServer.start();
			peer.pipeServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 965, 679);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JSplitPane splitPane = new JSplitPane();
		springLayout.putConstraint(SpringLayout.SOUTH, splitPane, 644, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, splitPane, 959, SpringLayout.WEST, frame.getContentPane());
		splitPane.setResizeWeight(0.1);
		springLayout.putConstraint(SpringLayout.NORTH, splitPane, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, splitPane, 0, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(splitPane);

		tree = new JTree();
		splitPane.setLeftComponent(tree);
		{
//			initTreeModel();

			tree.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount()==2){
						TreePath path=tree.getPathForLocation(e.getX(),e.getY());
						DefaultMutableTreeNode node=null;
						if(path.getLastPathComponent()!=null){
							node = (DefaultMutableTreeNode) path.getLastPathComponent();
						}
						if(node!=null){
							Object object=node.getUserObject();
							if(object instanceof Group){//打开组群
								Group group=(Group)object;

								boolean existed=false;
								for(int i=0;i<tabbedPane.getTabCount();i++){
									RecivePanel panel=(RecivePanel)tabbedPane.getComponentAt(i);
									if(panel.isgroup){
										if(group==panel.group){//group唯一,所以可以用==
											existed=true;
											tabbedPane.setSelectedIndex(i);
										}
									}
								}
								if(!existed){
									RecivePanel panel=new RecivePanel(peer,group);
									tabbedPane.addTab("组群:"+group.toString(),null,panel);
									tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
								}
							}
							if(object instanceof RemotePeer){
								RemotePeer remotePeer=(RemotePeer)object;

								boolean existed=false;
								for(int i=0;i<tabbedPane.getTabCount();i++){
									RecivePanel panel=(RecivePanel)tabbedPane.getComponentAt(i);
									if(panel.ispeer){
										if(remotePeer==panel.remotePeer){//group唯一,所以可以用==
											existed=true;
											tabbedPane.setSelectedIndex(i);
										}
									}
								}
								if(!existed&&!remotePeer.equals(peer.myself)){
									RecivePanel panel=new RecivePanel(peer,remotePeer);
									tabbedPane.addTab("个人:"+remotePeer.toString(),null,panel);
									tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
								}
							}
						}
					}

					super.mouseClicked(e);
				}
			});
		}
		
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new TabbedPane();
		tabbedPane.setCloseButtonEnabled(true);
		panel.add(tabbedPane, BorderLayout.CENTER);
		{
			tabbedPane.addTabbedPaneListener(new TabbedPaneListener(){

				@Override
				public void tabRemoved(Tab tab, Component component, int index) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void tabAdded(Tab tab, Component component, int index) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void tabSelected(Tab tab, Component component, int index) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void allTabsRemoved() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean canTabClose(Tab tab, Component component) {
					// TODO Auto-generated method stub
					return false;
				}
				
			});

		}
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Iterator iterator=peer.getSendPipeList().entrySet().iterator();
				while(iterator.hasNext()){
					Map.Entry entry=(Map.Entry) iterator.next();
					Pipe pipe= (Pipe)entry.getValue();
					try {
						pipe.getSocket().getInputStream().close();
						pipe.getSocket().getOutputStream().close();
						pipe.getSocket().close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
				iterator=peer.getRecivePipeList().entrySet().iterator();
				while(iterator.hasNext()){
					Map.Entry entry=(Map.Entry) iterator.next();
					Pipe pipe= (Pipe)entry.getValue();
					try {
						pipe.getSocket().getInputStream().close();
						pipe.getSocket().getOutputStream().close();
						pipe.getSocket().close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				super.windowClosing(e);
			}
		});
	}
	public DefaultTreeModel initTreeModel(){
		DefaultMutableTreeNode rootNode=new DefaultMutableTreeNode("组群列表");
		Iterator iter=peer.getGroupList().entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry=(Map.Entry)iter.next();
			Group group=(Group) entry.getValue();
			DefaultMutableTreeNode node=new DefaultMutableTreeNode(group);
			rootNode.add(node);
			Iterator iter1=group.getPeerList().entrySet().iterator();
			while(iter1.hasNext()){
				Map.Entry entry1=(Map.Entry)iter1.next();
				RemotePeer remotePeer=(RemotePeer)entry1.getValue();
				node.add(new DefaultMutableTreeNode(remotePeer));
				}
			}
		DefaultTreeModel treeModel=new DefaultTreeModel(rootNode);
		tree.setModel(treeModel);
//		tree.repaint();
		tree.updateUI();
		expandTree(tree);
		return treeModel;
		
	}
	public RecivePanel getPanel(Group group){
		for(int i=0;i<tabbedPane.getTabCount();i++){
			RecivePanel panel=(RecivePanel)tabbedPane.getComponentAt(i);
			if(panel.isgroup){
				if(group==panel.group){//group唯一,所以可以用==
					tabbedPane.setSelectedIndex(i);
					return panel;
				}
			}
		}
		return null;
	}
	public RecivePanel getPanel(RemotePeer remotePeer){
		for(int i=0;i<tabbedPane.getTabCount();i++){
			RecivePanel panel=(RecivePanel)tabbedPane.getComponentAt(i);
			if(panel.ispeer){
				if(remotePeer==panel.remotePeer){//group唯一,所以可以用==
					tabbedPane.setSelectedIndex(i);
					return panel;
				}
			}
		}
		return null;
	}

	public static void expandTree(JTree tree) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll(tree, new TreePath(root), true);
	}


	private static void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements(); ) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}
}
