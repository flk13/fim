package org.neoflk.kim.server.handler;

import io.netty.channel.ChannelHandlerContext;
import org.neoflk.kim.common.constants.KimConstants;
import org.neoflk.kim.common.constants.ClientType;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.handler.KimHandler;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.resp.KimResponse;
import org.neoflk.kim.common.util.KimUtils;
import org.neoflk.kim.server.command.CommandInvoker;
import org.neoflk.kim.server.storage.DataCenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author neoflk
 * 创建时间：2020年04月25日
 */
public class KimServerHandler extends KimHandler {

    protected CommandInvoker commandInvoker;

    public KimServerHandler() {
        commandInvoker = new CommandInvoker();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        KimRequest kimRequest = (KimRequest)msg;

        log.info(GSON.toJson(kimRequest));
        commandInvoker.setChannel(ctx.channel());
        commandInvoker.setKimRequest(kimRequest);

        switch (MessageType.getType(kimRequest.getType())) {
            case Command:
                String command = kimRequest.getAttactments().get(KimConstants.COMMAND);
                KimResponse kimResponse = commandInvoker.invoker(command);
                if (ClientType.KIM_CLI.equals(kimRequest.getAttactments().get(KimConstants.CLIENT_TYPE))) {
                    writeResponse(ctx,DataCenter.SESSION_CHANNEL_MAP.get(KimConstants.SESSION_ID),kimResponse);
                } else {
                    for (String user : DataCenter.USER_CHANNEL_MAP.keySet()) {
                        if (!DataCenter.USER_CLI.contains(user)) {
                            writeResponse(ctx,DataCenter.USER_CHANNEL_MAP.get(user),kimResponse);
                        }
                    }
                }
                break;
            case Onchat:
                String to = kimRequest.getAttactments().get("to");
                List<String> users = new ArrayList<>();
                if ("ALL".equalsIgnoreCase(to)) {
                    users.addAll(DataCenter.USER_CHANNEL_MAP.keySet());
                } else {
                    users.addAll(Arrays.asList(to.split(USER_DELIMITER)));
                    users.add(kimRequest.getAttactments().get("from"));
                }
                kimResponse = new KimResponse.Builder()
                        .code(200)
                        .body(kimRequest.getBody())
                        .type(MessageType.Onchat.code)
                        .sessionId(kimRequest.getSessionId())
                        .attactments(KimUtils.attachments(KimConstants.FROM,kimRequest.getAttactments().get(KimConstants.FROM)))
                        .build();
                for (String user : users) {
                    writeResponse(ctx,DataCenter.USER_CHANNEL_MAP.get(user),kimResponse);
                }
                break;
            case Heartbeat:
                break;
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        log.error(cause.getMessage(),cause);
    }
}
