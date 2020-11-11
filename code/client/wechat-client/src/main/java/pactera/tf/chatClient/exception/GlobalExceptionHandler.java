package pactera.tf.chatClient.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import pactera.tf.chatClient.utils.ResponseCode;
import pactera.tf.chatClient.utils.ResponseResult;
import pactera.tf.chatClient.utils.ResposeResultUtil;

@ControllerAdvice
@SuppressWarnings("rawtypes")
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


	/**
	 * 处理空指针的异常
	 * 
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = NullPointerException.class)
	@ResponseBody
	public ResponseResult exceptionHandler(HttpServletRequest req, NullPointerException e) {
		logger.error("发生空指针异常！原因是:", e);
		return ResposeResultUtil.error(ResponseCode.BODY_NOT_MATCH);
	}
	
	/**
	 * 统一处理 参数校验 异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException  e){
        FieldError fieldError = e.getBindingResult().getFieldError();
        assert fieldError != null;
//        JSONObject object=new JSONObject();
        String msg=fieldError.getDefaultMessage();
		return ResposeResultUtil.error("1005", msg);
    }

	/**
	 * 处理其他异常
	 * 
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResponseResult exceptionHandler(HttpServletRequest req, Exception e) {
		logger.error("未知异常！原因是:", e);
		return ResposeResultUtil.error(ResponseCode.SERVER_ERROR);
	}
}
