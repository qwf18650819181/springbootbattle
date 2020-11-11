package pactera.tf.chat.kafka;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pactera.tf.chat.trade.KafKaSender;

@Component
public class Scheduler {
	 
	 @Autowired
	 KafKaSender kafKaSender;
	 
	 @Autowired
	 RedisTemplate redisTemplate;
	
	 @Scheduled(cron="0 0 * * * ?") //每小时整点执行一次
	 public void timeReport() {  
		 Set<String> keys = redisTemplate.keys("*_login");
		 List<String> allUserList=new ArrayList<String>();
		 for(String key:keys) {
			 String usercode =key.replace("_login", "");
			 allUserList.add(usercode);
		 }
		 LocalDateTime dt = LocalDateTime.now();
		 int year = dt.getYear();//年
		 int monthValue = dt.getMonthValue();//月
		 int dayOfMonth = dt.getDayOfMonth();//日
		 int hour = dt.getHour();//时
		 StringBuffer msg=new StringBuffer("现在是");
		 msg.append(year).append("年").append(monthValue).append("月").
		 append(dayOfMonth).append("日").append(hour).append("点整");
		 String message=msg.toString();
		 //判断有用户在线才发送整点报时
		 if(allUserList!=null&&allUserList.size()>0) {
			 for(String code:allUserList) {
				 kafKaSender.sendTimeReport(code,message); 
			 }
		 }
	 }
	
}
