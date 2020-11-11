package pactera.tf.chat.entity;

public class BuGroupDo {
    private Integer id;

    private String nickname;

    private Integer cnt;

    private String mastercode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getMastercode() {
        return mastercode;
    }

    public void setMastercode(String mastercode) {
        this.mastercode = mastercode == null ? null : mastercode.trim();
    }
}