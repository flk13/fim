package org.neoflk.kim.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.neoflk.kim.client.ClientFrame;
import org.neoflk.kim.common.constants.KimConstants;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.constants.SupportedCmd;
import org.neoflk.kim.common.handler.KimHandler;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.resp.KimResponse;
import org.neoflk.kim.common.util.KimUtils;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * @author neoflk
 * 创建时间：2020年04月28日
 */
public class KimClientHandler extends KimHandler {

    private ClientFrame kimFrame;
    private String nickname;
    private SocketChannel channel;

    public KimClientHandler(SocketChannel channel, ClientFrame kimFrame, String nickname) {
        this.channel = channel;
        this.kimFrame = kimFrame;
        this.nickname = nickname;
        kimFrame.setChannel(channel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Map<String,String> attachments = KimUtils.attachments(KimConstants.LOGIN_NAME, nickname);
        attachments.put(KimConstants.COMMAND,SupportedCmd.LOGIN_CMD);
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
        log.info(GSON.toJson(kimResponse));
        Map<String,String> attachments = kimResponse.getAttactments();
        switch (MessageType.getType(kimResponse.getType())) {
            case Command:
                handleCommand(ctx,kimResponse);
                break;
            case Onchat:
                String from = attachments.get(KimConstants.FROM);
                String name = from.equalsIgnoreCase(kimFrame.getLoginName().getText())? "我": from;
                kimFrame.getChatArea().append("【"+name+"】\n"+kimResponse.getBody().toString()+"\n");
                break;
            case Heartbeat:
                break;
        }
        ctx.fireChannelRead(msg);

    }

    private void handleCommand(ChannelHandlerContext ctx,KimResponse kimResponse) {
        switch (kimResponse.getAttactments().get(KimConstants.COMMAND)) {
            case SupportedCmd.LOGIN_CMD:
                kimFrame.getChatArea().append(kimResponse.getBody().toString()+"\n");
                kimFrame.setSessionId(kimResponse.getSessionId());

                KimRequest kimRequest = new KimRequest.Builder()
                        .type(MessageType.Command.code)
                        .sessionId(kimResponse.getSessionId())
                        .attactments(KimUtils.attachments(KimConstants.COMMAND, SupportedCmd.USERLIST_CMD))
                        .build();
                writeRequest(ctx,kimRequest);
                break;
            case SupportedCmd.USERLIST_CMD:
                List<String> users = (List)kimResponse.getBody();
                JComboBox comboBox = kimFrame.getComboBox();
                comboBox.removeAllItems();
                comboBox.addItem("ALL");
                for (String user : users) {
                    comboBox.addItem(user);
                }
                break;
            case SupportedCmd.LOGOUT_CMD:
                kimFrame.getChatArea().append(kimResponse.getBody().toString()+"\n");
                break;
        }
    }

}
