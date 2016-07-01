package net.zsh_o.cec4j.byzantine.ui;

import com.sun.istack.internal.Nullable;
import net.zsh_o.cec4j.RemotePeer;
import net.zsh_o.cec4j.byzantine.Byzantine;
import net.zsh_o.cec4j.byzantine.PeerCommand;
import net.zsh_o.cec4j.byzantine.listener.BGroupAdvListener;
import net.zsh_o.cec4j.byzantine.listener.BGroupMessageListener;
import net.zsh_o.cec4j.byzantine.listener.BPipeAdvListener;
import net.zsh_o.cec4j.byzantine.listener.BPipeListener;
import net.zsh_o.cec4j.listener.ZGroupAdvListener;
import net.zsh_o.cec4j.listener.ZGroupMessageListener;
import net.zsh_o.cec4j.listener.ZPipeAdvListener;
import net.zsh_o.cec4j.listener.ZPipeListener;
import net.zsh_o.cec4j.Peer;
import net.zsh_o.cec4j.Pipe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

/**
 * Created by zsh96 on 2016/4/17.
 */
public class Main {
    public static Main MAIN;
    public Peer peer;
    public Byzantine byzantine;

    public JFrame frame;
    private JPanel contentPane;
    public JTree tree;
    public JTextArea textArea;

    public Main(InetAddress address, int multiPort, int serverPort) {
        initialize();
        MAIN=this;
        try {
            textArea.setText(textArea.getText()+"\n"+"初始化系统...");
            peer=new Peer(serverPort,address,multiPort);
            peer.advServer.registeListener(new BGroupAdvListener(peer));
            peer.advServer.registeListener(new BPipeAdvListener());
            peer.advServer.registeListener(new BGroupMessageListener());
            peer.pipeServer.registeListener(new BPipeListener());
            peer.advServer.start();
            peer.pipeServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     */
    void initialize(){
        frame = new JFrame();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 803, 667);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        frame.setContentPane(contentPane);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.4);
        contentPane.add(splitPane, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane();
        splitPane.setLeftComponent(scrollPane);

        tree = new JTree();
        tree.setShowsRootHandles(true);
//        tree.setEnabled(false);
        scrollPane.setViewportView(tree);

        JScrollPane scrollPane_1 = new JScrollPane();
        splitPane.setRightComponent(scrollPane_1);

        textArea = new JTextArea();
        textArea.setFont(new Font("宋体", Font.PLAIN, 16));//设置字体，确保自宽一致
        textArea.setEditable(false);
        scrollPane_1.setViewportView(textArea);

        frame.addWindowListener(new WindowAdapter() {//当程序关闭,关闭所有套接字
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

    public int getRemotePeerNumber(){
        return peer.getDefaultGroup().getPeerList().size();
    }

    /*
    * 刷新树--每当pipe接收到指令就刷新树
    * 树:用来保存每轮接受到的信息
    * */
    public DefaultTreeModel initTreeModel(@Nullable String finalCommand){
        DefaultMutableTreeNode rootNode=new DefaultMutableTreeNode("指令:");
        if(finalCommand==null){
            rootNode=new DefaultMutableTreeNode("指令:");
        }else{
            rootNode=new DefaultMutableTreeNode("最终指令: ==>"+finalCommand);
        }
        HashMap<RemotePeer,PeerCommand> pcts=byzantine.commandsTree;//里面只有一个元素为司令
        PeerCommand commander = pcts.entrySet().iterator().next().getValue();
        DefaultMutableTreeNode dmt=new DefaultMutableTreeNode(commander);
        rootNode.add(dmt);
        _visitInsert(commander,dmt);

        DefaultTreeModel treeModel=new DefaultTreeModel(rootNode);
        tree.setModel(treeModel);
		tree.repaint();
//        tree.updateUI();
        expandTree(tree);
        return treeModel;
    }
    void _visitInsert(PeerCommand pc,DefaultMutableTreeNode node){
        if(!pc.peerTreeSet.isEmpty()){
            Iterator iterator=pc.peerTreeSet.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry entry= (Map.Entry) iterator.next();
                PeerCommand p= (PeerCommand) entry.getValue();
                DefaultMutableTreeNode tn=new DefaultMutableTreeNode(p);
                node.add(tn);
                _visitInsert(p,tn);
            }
        }
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
