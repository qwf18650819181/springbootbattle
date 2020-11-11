package pactera.tf.chat.entity;

public class BuFriendListKey {
    private String usercode;

    private String friendcode;

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode == null ? null : usercode.trim();
    }

    public String getFriendcode() {
        return friendcode;
    }

    public void setFriendcode(String friendcode) {
        this.friendcode = friendcode == null ? null : friendcode.trim();
    }
}