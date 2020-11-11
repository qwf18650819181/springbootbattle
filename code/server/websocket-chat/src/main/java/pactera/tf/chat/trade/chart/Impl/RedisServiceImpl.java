package pactera.tf.chat.trade.chart.Impl;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import pactera.tf.chat.trade.chart.IRedisService;
import pactera.tf.chat.utils.FastjsonUtil;

/**
 * redis服务实现类
 * 
 * @author Admin
 *
 */
//@SuppressWarnings({ "rawtypes", "unchecked","unlikely-arg-type" })
//@Service
//public class RedisServiceImpl implements IRedisService {
//
//	private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
//
//	/**
//	 * 用户点赞总数key
//	 */
//	@Value("${total.like.count.key}")
//	private String TOTAL_LIKE_COUNT_KEY;
//
//	/**
//	 * 点赞用户key
//	 */
//	@Value("${user.like.key}")
//	private String USER_LIKE_KEY;
//
//	/**
//	 * 用户被点赞的key
//	 */
//	@Value("${liked.user.key}")
//	private String LIKED_USER_KEY;
//
//	@Resource
//	private RedisTemplate redisTemplate;
//
//	/**
//	 * 指定序列化方式
//	 */
//	@PostConstruct
//	public void init() {
//		RedisSerializer redisSerializer = new StringRedisSerializer();
//		redisTemplate.setKeySerializer(redisSerializer);
//		redisTemplate.setValueSerializer(redisSerializer);
//		redisTemplate.setHashKeySerializer(redisSerializer);
//		redisTemplate.setHashValueSerializer(redisSerializer);
//	}
//
//	@Override
//	public void likeUser(String likedUserId, String usercode) {
//		// TODO Auto-generated method stub
//		validateParam(likedUserId, usercode); // 参数验证
//
//		logger.info("点赞数据存入redis开始，likedUserId:{}，usercode:{}", likedUserId, usercode);
//
//		// 只有未点赞的用户才可以进行点赞
//		likeUserValidate(likedUserId, usercode);
//		// 1.用户总点赞数+1
//		redisTemplate.opsForHash().increment(TOTAL_LIKE_COUNT_KEY, String.valueOf(likedUserId), 1);
//		synchronized (this) {
//			// 2.用户喜欢的人+1
//			String userLikeResult = (String) redisTemplate.opsForHash().get(USER_LIKE_KEY, String.valueOf(usercode));
//			Set<String> userIdSet = userLikeResult == null ? new HashSet<>()
//					: FastjsonUtil.deserializeToSet(userLikeResult, String.class);
//			userIdSet.add(likedUserId);
//			redisTemplate.opsForHash().put(USER_LIKE_KEY, String.valueOf(usercode), FastjsonUtil.serialize(userIdSet));
//
//			// 3.被点赞用户数+1
//			String userLikedResult = (String) redisTemplate.opsForHash().get(LIKED_USER_KEY,
//					String.valueOf(likedUserId));
//			Set<String> likeUserIdSet = userLikedResult == null ? new HashSet<>()
//					: FastjsonUtil.deserializeToSet(userLikedResult, String.class);
//			likeUserIdSet.add(usercode);
//			redisTemplate.opsForHash().put(LIKED_USER_KEY, String.valueOf(likedUserId),
//					FastjsonUtil.serialize(likeUserIdSet));
//			logger.info("取消点赞数据存入redis结束，likedUserId:{}，usercode:{}", likedUserId, usercode);
//		}
//	}
//	
//	/**
//	 * 取消点赞数
//	 * @param likedUserId
//	 * @param usercode
//	 */
//	@Override
//	public void unlikeUser(String likedUserId, String usercode) {
//		validateParam(likedUserId, usercode); // 参数验证
//
//		logger.info("取消点赞数据存入redis开始，likedUserId:{}，usercode:{}", likedUserId, usercode);
//		// 1.用户总点赞数-1
//		synchronized (this) {
//			// 只有点赞的用户才可以取消点赞
//			unlikeUserValidate(likedUserId, usercode);
//			Long totalLikeCount = Long.parseLong(
//					(String) redisTemplate.opsForHash().get(TOTAL_LIKE_COUNT_KEY, String.valueOf(likedUserId)));
//			redisTemplate.opsForHash().put(TOTAL_LIKE_COUNT_KEY, String.valueOf(likedUserId),
//					String.valueOf(--totalLikeCount));
//
//			// 2.用户喜欢的人-1
//			String userLikeResult = (String) redisTemplate.opsForHash().get(USER_LIKE_KEY, String.valueOf(usercode));
//			Set<Long> userIdSet = FastjsonUtil.deserializeToSet(userLikeResult, Long.class);
//			userIdSet.remove(likedUserId);
//			redisTemplate.opsForHash().put(USER_LIKE_KEY, String.valueOf(usercode), FastjsonUtil.serialize(userIdSet));
//
//			// 3.取消用户顶赞某个人的点赞数
//			String userLikedResult = (String) redisTemplate.opsForHash().get(LIKED_USER_KEY,
//					String.valueOf(likedUserId));
//			Set<Long> likeUserIdSet = FastjsonUtil.deserializeToSet(userLikedResult, Long.class);
//			likeUserIdSet.remove(usercode);
//			redisTemplate.opsForHash().put(LIKED_USER_KEY, String.valueOf(likedUserId),
//					FastjsonUtil.serialize(likeUserIdSet));
//		}
//
//		logger.info("取消点赞数据存入redis结束，likedUserId:{}，usercode:{}", likedUserId, usercode);
//	}
//
//	/**
//	 * 入参验证
//	 *
//	 * @param params
//	 */
//	private void validateParam(String... params) {
//		for (String param : params) {
//			if (null == param) {
//				logger.error("入参存在null值");
//
//			}
//		}
//	}
//
//	/**
//	 * 点赞逻辑
//	 * @param likedUserId
//	 * @param usercode
//	 */
//	private void likeUserValidate(String likedUserId, String usercode) {
//		Set<String> likedUserIdSet = FastjsonUtil.deserializeToSet(
//				(String) redisTemplate.opsForHash().get(USER_LIKE_KEY, String.valueOf(usercode)), String.class);
//		Set<String> usercodeSet = FastjsonUtil.deserializeToSet(
//				(String) redisTemplate.opsForHash().get(LIKED_USER_KEY, String.valueOf(likedUserId)), String.class);
//		if (likedUserIdSet == null) {
//			return;
//		}
//		if (usercodeSet == null) {
//			return;
//		} else {
//			if (likedUserIdSet.contains(likedUserId) || usercodeSet.contains(usercode)) {
//				logger.error("该用户已被当前用户点赞，重复点赞，likedUserId:{}，usercode:{}", likedUserId, usercode);
//			}
//		}
//	}
//
//	/**
//	 * 取消点赞逻辑
//	 * @param likedUserId
//	 * @param usercode
//	 */
//	private void unlikeUserValidate(String likedUserId, String usercode) {
//		Set<String> articleIdSet = FastjsonUtil.deserializeToSet(
//				(String) redisTemplate.opsForHash().get(USER_LIKE_KEY, String.valueOf(usercode)), String.class);
//		Set<String> likePostIdSet = FastjsonUtil.deserializeToSet(
//				(String) redisTemplate.opsForHash().get(LIKED_USER_KEY, String.valueOf(likedUserId)), String.class);
//		if (articleIdSet == null || !articleIdSet.contains(likedUserId) || likePostIdSet == null
//				|| !likePostIdSet.contains(usercode)) {
//			logger.error("该用户未被当前用户点赞，不可以进行取消点赞操作，likedUserId:{}，usercode:{}", likedUserId, usercode);
//		}
//	}
//}
