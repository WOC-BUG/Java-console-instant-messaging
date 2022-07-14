package chat.utils;

import java.util.Scanner;

public class Utility {
    private static Scanner scanner = new Scanner(System.in);

    public static char readMenuSelection() {
        char c;
        for (; ; ) {
            String str = readKeyBoard(1, false);
            c = str.charAt(0);
            if (c != '1' && c != '2' && c != '3'
                    && c != '4' && c != '5' && c != '6'
                    && c != '7' && c != '8' && c != '9') {
                System.out.println("请重新输入！");
            } else break;
        }
        return c;
    }

    public static char readChar() {
        String str = readKeyBoard(1, false);
        return str.charAt(0);
    }

    public static char readChar(char defaultValue) {
        String str = readKeyBoard(1, false);
        return str.length() == 0 ? defaultValue : str.charAt(0);
    }

    public static char readConfirmSelection() {
        System.out.println("请输入选择(Y/N)：");
        char c;
        for (; ; ) {
            String str = readKeyBoard(1, false).toUpperCase();
            c = str.charAt(0);
            if (c == 'Y' || c == 'N') {
                break;
            }
            System.out.println("输入错误，请重新输入");
        }
        return c;
    }

    public static String readString(int limit) {
        return readKeyBoard(limit, true);
    }

    public static String readString(int limit, String defaultValue) {
        String str = readKeyBoard(limit, true);
        return str.equals("") ? defaultValue : str;
    }

    public static int readInt(int defaultValue) {
        int n;
        for (; ; ) {
            String str = readKeyBoard(10, true);
            if (str.equals(""))
                return defaultValue;

            try {
                n = Integer.parseInt(str);
                break;
            } catch (NumberFormatException e) {
                System.out.println("数字输入错误，请重新输入！");
            }
        }
        return n;
    }

    public static String readKeyBoard(int limit, boolean flag) {
        String line = "";
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            if (line.length() == 0) {
                if (flag)
                    return line;
                else continue;
            }

            if (line.length() < 1 || line.length() > limit) {
                System.out.println("输入长度范围必须在[i," + limit + "]！");
                continue;
            }
            break;
        }
        return line;
    }
}
