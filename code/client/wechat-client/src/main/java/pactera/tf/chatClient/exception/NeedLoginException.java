package pactera.tf.chatClient.exception;

public class NeedLoginException extends Exception implements BaseErrorInfoInterface{

	@Override
	public String getCode() {
		return "-1";
	}

	@Override
	public String getMsg() {
		return "请先登录，再操作";
	}

}
