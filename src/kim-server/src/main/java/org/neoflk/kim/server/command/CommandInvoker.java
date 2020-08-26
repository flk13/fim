package org.neoflk.kim.server.command;

import io.netty.channel.Channel;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.resp.KimResponse;

/**
 * @author neoflk
 * 创建时间：2020年04月30日
 */
public class CommandInvoker {

    private Channel channel;
    private KimRequest kimRequest;
    private static CommandChooser commandChooser = new CommandChooser();;

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setKimRequest(KimRequest kimRequest) {
        this.kimRequest =kimRequest;
    }

    public KimResponse invoker(String cmd) {
        return commandChooser.choose(cmd).execute(channel,kimRequest);
    }

}
