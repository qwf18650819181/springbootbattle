package pactera.tf.chat.entity;

public class BuMessageDo {
    private int id;

    private String receivecode;

    private String realsendcode;

    private String sendcode;

    private String message;

    private String msgtype;

    private String isreceive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceivecode() {
        return receivecode;
    }

    public void setReceivecode(String receivecode) {
        this.receivecode = receivecode == null ? null : receivecode.trim();
    }

    public String getRealsendcode() {
        return realsendcode;
    }

    public void setRealsendcode(String realsendcode) {
        this.realsendcode = realsendcode == null ? null : realsendcode.trim();
    }

    public String getSendcode() {
        return sendcode;
    }

    public void setSendcode(String sendcode) {
        this.sendcode = sendcode == null ? null : sendcode.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype == null ? null : msgtype.trim();
    }

    public String getIsreceive() {
        return isreceive;
    }

    public void setIsreceive(String isreceive) {
        this.isreceive = isreceive == null ? null : isreceive.trim();
    }
}