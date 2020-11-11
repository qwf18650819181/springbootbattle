package pactera.tf.chat.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pactera.tf.chat.trade.chart.IGroupChatService;
import pactera.tf.chat.trade.chart.ThumbUpChatService;
import pactera.tf.chat.utils.ResponseCode;
import pactera.tf.chat.utils.ResponseResult;
import pactera.tf.chat.utils.ResposeResultUtil;

import java.util.List;

@Api(tags = "点赞")
@RestController
public class ThumbUpChatController {

    @Autowired
    private ThumbUpChatService thumbUpChatService;

    @ApiOperation(value = "好友点赞", notes = "好友点赞")
    @RequestMapping(value = "/ThumpUpChat", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usercode", value = "实际发送用户id", required = true),
            @ApiImplicitParam(name = "thumbUpId", value = "接收用户id", required = true)
    })
    public ResponseResult createGroupChat(@RequestParam String usercode,@RequestParam String thumbUpId){

        return thumbUpChatService.thumbUpChat(usercode,thumbUpId);
    }







}
