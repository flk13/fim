package org.neoflk.kim.server.command;

import io.netty.channel.Channel;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.resp.KimResponse;

/**
 * @author neoflk
 * 创建时间：2020年04月30日
 */
public interface ICommand {

    KimResponse execute(Channel channel, KimRequest kimRequest);

}
