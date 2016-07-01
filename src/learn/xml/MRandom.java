package learn.xml;

import java.util.Random;
import java.util.Scanner;

/**
 * Created by zsh96 on 2016/4/18.
 */
public class MRandom {
    public static void main(String[] args){
        Scanner scanner=new Scanner(System.in);
        while(true){
            System.out.println(getTraitorCommand(1,5));
            scanner.nextLine();
        }
    }

    public static String getTraitorCommand(int a,int b){
        Random random=new Random();
        return String.valueOf(a+random.nextInt(b-a+1));

    }
}
