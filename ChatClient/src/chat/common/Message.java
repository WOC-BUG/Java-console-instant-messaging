package chat.common;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sender;
    private String receiver;
    private String content;
    private String messageType;
    private String sendTime;

    // 文件传输相关
    private byte[] fileBytes;
    private int fileLen;
    private String fileSrc;
    private String fileDes;

    public Message() {
    }

    public Message(String sender, String receiver, String content, String messageType, String sendTime) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.messageType = messageType;
        this.sendTime = sendTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getFileSrc() {
        return fileSrc;
    }

    public void setFileSrc(String fileSrc) {
        this.fileSrc = fileSrc;
    }

    public String getFileDes() {
        return fileDes;
    }

    public void setFileDes(String fileDes) {
        this.fileDes = fileDes;
    }
}
