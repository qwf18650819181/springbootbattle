package pactera.tf.chat.trade.chart.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import pactera.tf.chat.component.message.MessageBo;
import pactera.tf.chat.entity.BuGroupDo;
import pactera.tf.chat.entity.BuMessageDo;
import pactera.tf.chat.mapper.BuMessageMapper;
import pactera.tf.chat.trade.chart.IBuMessageService;

@Service
public class BuMessageServiceImpl implements IBuMessageService{

	@Autowired
	BuMessageMapper mapper;
	private static final Logger logger = LoggerFactory.getLogger(BuMessageServiceImpl.class);
	
	
	@Override
	public boolean createMessage(List<String> userCodes, MessageBo message) {
        List<BuMessageDo> messageDoList = new ArrayList<>();
        boolean flag=true;
        for (int i =0 ;i < userCodes.size();i++){
            flag=createMessage(userCodes.get(i), message);
            if(!flag) {
            	return false;
            }
        }
        return true;      
        
	}

	@Override
	public boolean createMessage(String userCode,MessageBo message) {
		try {
			BuMessageDo buMessageDo = new BuMessageDo();
	        buMessageDo.setReceivecode(userCode);
	        buMessageDo.setRealsendcode(message.getRealFrom());
	        buMessageDo.setSendcode(message.getFrom());
	        buMessageDo.setMessage(message.getMessage());
	        buMessageDo.setMsgtype(String.valueOf(message.getMsgtype().getCode()));
	        buMessageDo.setIsreceive("N");
	        mapper.insert(buMessageDo);
	        return true;
		}catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
	}

}
