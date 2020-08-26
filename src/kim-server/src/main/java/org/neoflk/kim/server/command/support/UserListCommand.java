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
@Command(SupportedCmd.USERLIST_CMD)
public class UserListCommand extends AbstractCommand implements ICommand {
    @Override
    public KimResponse execute(Channel channel, KimRequest kimRequest) {
        return new KimResponse.Builder()
                .code(200)
                .type(MessageType.Command.code)
                .attactments(KimUtils.attachments(KimConstants.COMMAND, SupportedCmd.USERLIST_CMD))
                .sessionId(kimRequest.getSessionId())
                .body(DataCenter.USER_CHANNEL_MAP.keySet())
                .build();
    }
}
