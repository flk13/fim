package org.neoflk.kim.cli.command.support;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.neoflk.kim.cli.command.AbstractCliCommand;
import org.neoflk.kim.cli.command.KimCommand;
import org.neoflk.kim.cli.command.annotation.Cmd;
import org.neoflk.kim.common.constants.KimConstants;
import org.neoflk.kim.common.constants.ClientType;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.constants.SupportedCmd;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.util.KimUtils;

import java.util.Map;

/**
 * @author neoflk
 * 创建时间：2020年05月10日
 */

//展示在线用户
@Cmd(value = "listUser",description = "list all current online users,see [listUser] command for details")
public class ListUserCommand extends AbstractCliCommand implements KimCommand {
    @Override
    public void execute(String[] params) {

        Map<String,String> attachments = KimUtils.attachments(KimConstants.COMMAND, SupportedCmd.USERLIST_CMD);
        attachments.put(KimConstants.CLIENT_TYPE, ClientType.KIM_CLI);

        KimRequest kimRequest = new KimRequest.Builder()
                .type(MessageType.Command.code)
                .sessionId((String)holder.get().get(KimConstants.SESSION_ID))
                .attactments(attachments)
                .build();
        Channel channel = (Channel) holder.get().get(KimConstants.CHANNEL);
        ByteBuf byteBuf = channel.alloc().directBuffer();
        byteBuf.writeBytes(KimUtils.toBytes(kimRequest));
        channel.writeAndFlush(byteBuf);
    }
}
