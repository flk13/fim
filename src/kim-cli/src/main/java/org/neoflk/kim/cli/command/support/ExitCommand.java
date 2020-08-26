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

//退出命令 
@Cmd(value = "exit",description = "exit kim-cli,see exit command for details")
public class ExitCommand extends AbstractCliCommand implements KimCommand {
    @Override
    public void execute(String[] params) {
        Map<String,String> attachments = KimUtils.attachments(KimConstants.FROM,(String)holder.get().get(KimConstants.LOGIN_NAME));
        attachments.put(KimConstants.CLIENT_TYPE, ClientType.KIM_CLI);
        attachments.put(KimConstants.COMMAND, SupportedCmd.LOGOUT_CMD);

        Channel channel = (Channel) holder.get().get("channel");
        ByteBuf byteBuf = channel.alloc().directBuffer();
        KimRequest request = new KimRequest.Builder()
                .type(MessageType.Command.code)
                .sessionId((String)holder.get().get("sessionId"))
                .attactments(attachments)
                .build();
        byteBuf.writeBytes(KimUtils.toBytes(request));
        channel.writeAndFlush(byteBuf);
        System.exit(0);
    }
}
