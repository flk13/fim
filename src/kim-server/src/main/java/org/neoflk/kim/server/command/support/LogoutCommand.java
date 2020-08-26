package org.neoflk.kim.server.command.support;

import io.netty.channel.Channel;
import org.neoflk.kim.common.constants.KimConstants;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.constants.SupportedCmd;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.resp.KimResponse;
import org.neoflk.kim.common.util.KimUtils;
import org.neoflk.kim.server.command.AbstractCommand;
import org.neoflk.kim.server.command.ICommand;
import org.neoflk.kim.server.command.annotation.Command;
import org.neoflk.kim.server.storage.DataCenter;

/**
 * @author neoflk
 * 创建时间：2020年04月30日
 */
@Command(SupportedCmd.LOGOUT_CMD)
public class LogoutCommand extends AbstractCommand implements ICommand {
    @Override
    public KimResponse execute(Channel channel,KimRequest kimRequest) {
        DataCenter.remove(kimRequest.getSessionId());

        String logoutMsg = "【系统消息】["+kimRequest.getAttactments().get("from")+"] 退出Kim聊天室 ";
        log.info(logoutMsg);
        return new KimResponse.Builder()
                .code(200)
                .type(MessageType.Command.code)
                .attactments(KimUtils.attachments(KimConstants.COMMAND, SupportedCmd.LOGOUT_CMD))
                .sessionId(kimRequest.getSessionId())
                .body(logoutMsg)
                .build();
    }
}
