package chat.view;

import chat.service.UserClientService;
import chat.utils.Utility;

public class QQClient {
    private boolean loop = true;
    private String key = "";

    private UserClientService userClientService = new UserClientService();

    public static void main(String[] args) {
        QQClient qqClient = new QQClient();
        qqClient.mainMenu();
        System.out.println("客户端退出系统......");
    }

    private void mainMenu() {

        while (loop) {
            System.out.println("============欢迎登陆聊天系统============");
            System.out.println("\t\t\t 1 登陆系统");
            System.out.println("\t\t\t 9 退出系统");
            System.out.println("=====================================");
            System.out.println("请输入你的选择：");

            key = Utility.readString(1);
            switch (key) {
                case "1":
                    System.out.println("------------登陆系统------------");
                    System.out.println("请输入用户账号：");
                    String userID = Utility.readString(50);
                    System.out.println("请输入密码：");
                    String password = Utility.readString(50);

                    // 登录服务器
                    if (userClientService.checkUser(userID, password)) {
                        System.out.println("============欢迎<" + userID + ">登陆============");

                        // 进入二级菜单
                        while (loop) {
                            System.out.println("============聊天系统二级菜单============");
                            System.out.println("\t\t\t 1 显示在线用户列表");
                            System.out.println("\t\t\t 2 群发消息");
                            System.out.println("\t\t\t 3 私聊消息");
                            System.out.println("\t\t\t 4 发送文件");
                            System.out.println("\t\t\t 9 退出系统");
                            System.out.println("=====================================");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    System.out.println("---------显示用户在线列表---------");
                                    userClientService.getOnlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("------------群发消息------------");
                                    break;
                                case "3":
                                    System.out.println("------------私聊消息------------");
                                    break;
                                case "4":
                                    System.out.println("------------发送文件------------");
                                    break;
                                case "9":
                                    System.out.println("------------退出系统------------");
                                    // 给服务器发送退出系统的消息
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    } else {
                        System.out.println("登录失败！");
                    }
                    break;
                case "9":
                    System.out.println("------------退出系统------------");
                    loop = false;
                    break;
            }
        }
    }
}
