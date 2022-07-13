package chat.common;

public interface MessageType {
    String MESSAGE_LOGIN_SUCCESS = "1"; // 登录成功
    String MESSAGE_LOGIN_FAIL = "2";    // 登录失败
    String MESSAGE_COMMON_MESSAGE = "3";    // 普通消息
    String MESSAGE_GET_OLINE_FRIEND = "4";  // 获取在线好友列表
    String MESSAGE_RETURN_OLINE_FRIEND = "5";   // 返回在线好友列表
    String MESSAGE_CLIENT_EXIT = "6";   // 客户端请求退出
}
