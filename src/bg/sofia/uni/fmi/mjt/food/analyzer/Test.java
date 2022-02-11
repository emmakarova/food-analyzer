package bg.sofia.uni.fmi.mjt.food.analyzer;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        try(Scanner s = new Scanner(System.in)) {
            String r = s.nextLine();
            System.out.println(r);
            int a = 2 / 0;
        }
        catch (Exception e) {
            Scanner n = new Scanner(System.in);
            String r = n.nextLine();
            System.out.println(r);
        }
    }
}
