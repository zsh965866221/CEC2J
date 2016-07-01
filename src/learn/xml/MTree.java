package learn.xml;

import java.util.*;

/**
 * Created by zsh96 on 2016/4/18.
 */
public class MTree {
    public static void main(String[] args){
        int[] il=new int[]{1,2,3,4,5};

        Node node=new Node(0);
        MTree mTree=new MTree();
        mTree._insert(node,il,1);
        System.out.println();
    }
    void _insert(Node N, int[] il,int i){
        if(i>0){
            i--;
            for(int n=0;n<il.length;n++){
                if(il[n]==-1)    continue;
                int k=il[n];
                Node nn=new Node(k);
                nn.k=i;
                N.ial.add(nn);
                il[n]=-1;
                _insert(nn,il,i);
                il[n]=k;
            }
        }
    }

}
class Node{
    public int k;
    public int i;
    public Vector<Node> ial;

    public Node(int i) {
        this.i = i;
        ial=new Vector<>();
    }

    @Override
    public String toString() {
        return "Node{" +
                "i=" + i +
                '}';
    }
}