package pactera.tf.chat.utils;

import pactera.tf.chat.exception.BaseErrorInfoInterface;

public class ResposeResultUtil {

	/**
	 * 成功返回成功实体
	 * 
	 * @param <T>
	 * @param data
	 * @return
	 */
	public static <T> ResponseResult<T> success(T data) {

		ResponseResult<T> result = new ResponseResult<T>();
		result.setCode(ResponseCode.SUCCESS.getCode());
		result.setMessage(ResponseCode.SUCCESS.getMsg());
		result.setData(data);
		return result;
	}

	/**
	 * 创建一个响应实体类
	 * 
	 * @param <T>
	 * @param code
	 * @param data
	 * @return
	 */
	public static <T> ResponseResult<T> createResponse(ResponseCode code, T data) {
		ResponseResult<T> result = new ResponseResult<T>();
		result.setCode(code.getCode());
		result.setMessage(code.getMsg());
		result.setData(data);
		return result;
	}

	/**
	 * 创建一个响应实体类
	 * 
	 * @param <T>
	 * @param code
	 * @return
	 */
	public static <T> ResponseResult<T> createResponse(ResponseCode code) {
		ResponseResult<T> result = new ResponseResult<T>();
		result.setCode(code.getCode());
		result.setMessage(code.getMsg());
		return result;
	}

	/**
	 * 错误返回实体
	 * 
	 * @param <T>
	 * @param code
	 * @param msg
	 * @return
	 */
	public static <T> ResponseResult<T> error(String code, String msg) {
		ResponseResult<T> result = new ResponseResult<T>();
		result.setCode(code);
		result.setMessage(msg);
		return result;

	}
	
	public static <T> ResponseResult<T> error(BaseErrorInfoInterface errorInfo) {
		ResponseResult<T> result = new ResponseResult<T>();
		result.setCode(errorInfo.getCode());
		result.setMessage(errorInfo.getMsg());
		result.setData(null);
		return result;
	}

}
