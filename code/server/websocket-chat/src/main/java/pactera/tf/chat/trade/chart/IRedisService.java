package pactera.tf.chat.trade.chart;

public interface IRedisService {
	/**
	 * 点赞
	 * @param usercode  点赞用户
	 * @param likedUserId 被点赞用户
	 */
	void likeUser(String likedUserId, String usercode);
	
	/**
	 * 取消点赞
	 * @param likedUserId
	 * @param usercode
	 */
	void unlikeUser(String likedUserId, String usercode);
}	
