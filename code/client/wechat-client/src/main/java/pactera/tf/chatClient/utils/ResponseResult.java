package pactera.tf.chatClient.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ResponseResult<T> {

	@ApiModelProperty(value = "返回码")
	private String code;

	@ApiModelProperty(value = "返回信息")
	private String message;

	@ApiModelProperty(value = "返回数据")
	private T data;

	public ResponseResult(String code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public ResponseResult(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResponseResult() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	
}
