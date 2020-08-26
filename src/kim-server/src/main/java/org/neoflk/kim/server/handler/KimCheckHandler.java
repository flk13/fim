package org.neoflk.kim.server.handler;

import io.netty.channel.ChannelHandlerContext;
import org.neoflk.kim.common.KimException;
import org.neoflk.kim.common.constants.KimConstants;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.constants.SupportedCmd;
import org.neoflk.kim.common.handler.KimHandler;
import org.neoflk.kim.common.req.KimRequest;

/**
 * @author neoflk
 * 创建时间：2020年04月30日
 */
public class KimCheckHandler extends KimHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        KimRequest kimRequest = (KimRequest)msg;
        if (!(MessageType.getType(kimRequest.getType()) == MessageType.Command
                && SupportedCmd.LOGIN_CMD.equals(kimRequest.getAttactments().get(KimConstants.COMMAND)))
                && kimRequest.getSessionId() == null) {
            throw new KimException("sessionId is null");
        }

        ctx.fireChannelRead(msg);
    }

}
