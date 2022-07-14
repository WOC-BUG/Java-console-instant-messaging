package chat.common;

public interface MessageType {
    String MESSAGE_LOGIN_SUCCESS = "1";
    String MESSAGE_LOGIN_FAIL = "2";
    String MESSAGE_COMMON_MESSAGE = "3";
    String MESSAGE_GET_OLINE_FRIEND = "4";
    String MESSAGE_RETURN_OLINE_FRIEND = "5";
    String MESSAGE_CLIENT_EXIT = "6";
    String MESSAGE_TO_ALL = "7";    // 群发消息
    String MESSAGE_FILE = "8"; // 文件消息
}