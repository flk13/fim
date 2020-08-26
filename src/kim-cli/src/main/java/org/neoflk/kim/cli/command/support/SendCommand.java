package org.neoflk.kim.cli.command.support;

import com.beust.jcommander.JCommander;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.SocketChannel;
import org.neoflk.kim.cli.KimCli;
import org.neoflk.kim.cli.command.AbstractCliCommand;
import org.neoflk.kim.cli.command.KimCommand;
import org.neoflk.kim.cli.command.annotation.Cmd;
import org.neoflk.kim.cli.command.support.args.SendArgs;
import org.neoflk.kim.common.constants.KimConstants;
import org.neoflk.kim.common.constants.ClientType;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.util.KimUtils;

import java.util.Map;
import java.util.StringJoiner;

/**
 * @author neoflk
 * 创建时间：2020年05月10日
 */

//发送消息
@Cmd(value = "send",description = "send message to other online users,see [send] command for details")
public class SendCommand extends AbstractCliCommand implements KimCommand {
    @Override
    public void execute(String[] params) {
        if(params.length > 4) {
            StringJoiner messsage = new StringJoiner(" ");
            String[] inputs = new String[4];
            for (int i=0;i<params.length;i++) {
                if (i>=3) {
                    messsage.add(params[i]);
                } else {
                    inputs[i] = params[i];
                }
            }
            inputs[3] = messsage.toString();
            params = inputs;
        }
        SendArgs sendArgs = new SendArgs();
        JCommander commander = JCommander.newBuilder()
                .addObject(sendArgs)
                .build();
        commander.setProgramName("send");
        try {
            commander.parse(params);
        } catch (Exception ex) {
            if (params.length > 1) {
                usage(commander);
                return;
            }
        }
        if (params.length == 0) {
            usage(commander);
        } else {
            SocketChannel channel = (SocketChannel) holder.get().get(KimConstants.CHANNEL);
            if (channel == null) {
                System.out.println("please login before you send message,see [login] command for details");
                return;
            }
            ByteBuf byteBuf = channel.alloc().directBuffer();
            Map<String,String> attachments = KimUtils.attachments(KimConstants.TO,sendArgs.getTo());
            attachments.put(KimConstants.FROM,(String)holder.get().get(KimConstants.LOGIN_NAME));
            attachments.put(KimConstants.CLIENT_TYPE, ClientType.KIM_CLI);
            attachments.put(KimConstants.TO,sendArgs.getTo());
            KimRequest requestParam = new KimRequest
                    .Builder()
                    .type(MessageType.Onchat.code)
                    .sessionId((String)holder.get().get(KimConstants.SESSION_ID))
                    .attactments(attachments)
                    .body(sendArgs.getMsg()== null? params[0]:sendArgs.getMsg())
                    .build();
            byteBuf.writeBytes(KimUtils.toBytes(requestParam));
            channel.writeAndFlush(byteBuf);
        }

    }

    private void usage(JCommander commander) {
        commander.usage();
        System.out.println("for example: send 'hello world !'");
        System.out.print("[kim@"+ KimCli.loginName+"]#");
    }
}
