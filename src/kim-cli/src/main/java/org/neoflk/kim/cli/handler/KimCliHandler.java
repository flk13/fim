package org.neoflk.kim.cli.handler;

import io.netty.channel.ChannelHandlerContext;
import org.neoflk.kim.cli.KimCli;
import org.neoflk.kim.common.constants.KimConstants;
import org.neoflk.kim.common.constants.ClientType;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.constants.SupportedCmd;
import org.neoflk.kim.common.handler.KimHandler;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.resp.KimResponse;
import org.neoflk.kim.common.util.KimUtils;

import java.util.List;
import java.util.Map;


/**
 * @author neoflk
 * 创建时间：2020年05月10日
 */

//脚手架所有处理机制
public class KimCliHandler extends KimHandler {

    private String nickname;
    private Map<String,Object> holder;

    public KimCliHandler(Map<String,Object> holder,String nickname) {
        this.nickname = nickname;
        this.holder = holder;
    }

    //netty的处理机制
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Map<String,String> attachments = KimUtils.attachments(KimConstants.LOGIN_NAME, nickname);
        attachments.put(KimConstants.COMMAND,SupportedCmd.LOGIN_CMD);
        attachments.put(KimConstants.CLIENT_TYPE, ClientType.KIM_CLI);
        KimRequest kimRequest = new KimRequest.Builder()
                .type(MessageType.Command.code)
                .attactments(attachments)
                .build();
        writeRequest(ctx,kimRequest);
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        KimResponse kimResponse = (KimResponse)msg;
        switch (MessageType.getType(kimResponse.getType())) {
            case Command:
                handleCommand(ctx,kimResponse);
                break;
            case Onchat:
                String from = kimResponse.getAttactments().get(KimConstants.FROM);
                String name = KimCli.loginName.equals(from)? "me":from;
                if (!KimCli.loginName.equals(from)) {
                    System.out.println();
                }
                System.out.println("["+name+"]:"+kimResponse.getBody().toString());
                System.out.print("[kim@"+KimCli.loginName+"]#");
                break;
            case Heartbeat:
                break;
        }
        ctx.fireChannelRead(msg);

    }


    private void handleCommand(ChannelHandlerContext ctx,KimResponse kimResponse) {
        Map<String,String> attachments = kimResponse.getAttactments();
        switch (attachments.get(KimConstants.COMMAND)) {
            case SupportedCmd.LOGIN_CMD:
                holder.put(KimConstants.CHANNEL,ctx.channel());
                holder.put(KimConstants.LOGIN_NAME,nickname);
                holder.put(KimConstants.SESSION_ID,kimResponse.getSessionId());
                if (KimConstants.NO_LOGIN.equals(KimCli.loginName)) {
                    KimCli.loginName = kimResponse.getAttactments().get(KimConstants.LOGIN_NAME);
                }

                System.out.println(kimResponse.getBody().toString());
                System.out.print("[kim@"+KimCli.loginName+"]#");
                break;
            case SupportedCmd.USERLIST_CMD:
                List<String> users = (List)kimResponse.getBody();
                System.out.println("当前在线用户数:["+users.size()+"],在线用户列表：");
                System.out.println("--------------------");
                for (String user : users) {
                    String charSpace = "";
                    for(int i=0;i<(18-charLength(user));i++) {
                        charSpace+= " ";
                    }
                    System.out.println("| "+user+charSpace+"|");
                    System.out.println("--------------------");
                }
                System.out.print("[kim@"+KimCli.loginName+"]#");
                break;
            case SupportedCmd.LOGOUT_CMD:
                System.out.println(kimResponse.getBody().toString());
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println();
        System.out.println(cause.getMessage());
        ctx.channel().close();
        System.exit(0);
    }

    private int charLength(String user) {
        int strLeng = user.length();
        int charLeng = user.getBytes().length;
        if (strLeng == charLeng) {
            return strLeng;
        }
        int y = (charLeng - strLeng) / 2;
        int x = strLeng - y;
        return 3 * y + x;
    }

}
