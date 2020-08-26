package org.neoflk.kim.server.command.support;

import io.netty.channel.Channel;
import org.neoflk.kim.common.constants.KimConstants;
import org.neoflk.kim.common.constants.ClientType;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.constants.SupportedCmd;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.resp.KimResponse;
import org.neoflk.kim.common.util.KimUtils;
import org.neoflk.kim.server.command.AbstractCommand;
import org.neoflk.kim.server.command.ICommand;
import org.neoflk.kim.server.command.annotation.Command;
import org.neoflk.kim.server.storage.DataCenter;

import java.util.Map;

/**
 * @author neoflk
 * 创建时间：2020年04月30日
 */
@Command(SupportedCmd.LOGIN_CMD)
public class LoginCommand extends AbstractCommand implements ICommand {

    @Override
    public KimResponse execute(Channel channel,KimRequest kimRequest) {
        String uuid = KimUtils.uuid();
        String loginName = kimRequest.getAttactments().get("loginName");
        String welcome = "【系统消息】欢迎 ["+loginName+"] 进入Kim聊天室 ";
        boolean isCliUser = ClientType.KIM_CLI.equals(kimRequest.getAttactments().get(KimConstants.CLIENT_TYPE));
        DataCenter.add(isCliUser,uuid,loginName,channel);

        Map<String,String> attachments = KimUtils.attachments(KimConstants.COMMAND, SupportedCmd.LOGIN_CMD);
        attachments.put(KimConstants.LOGIN_NAME,loginName);

        KimResponse kimResponse = new KimResponse.Builder()
                .code(200)
                .type(MessageType.Command.code)
                .attactments(attachments)
                .sessionId(uuid)
                .body(welcome)
                .build();
        return kimResponse;
    }
}
