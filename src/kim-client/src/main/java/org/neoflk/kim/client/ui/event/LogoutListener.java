package org.neoflk.kim.client.ui.event;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.neoflk.kim.client.ClientFrame;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.util.KimUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author neoflk
 * 创建时间：2020年04月30日
 */
public class LogoutListener extends MouseAdapter {

    private ClientFrame kimChatHome;

    public LogoutListener(ClientFrame kimChatHome, Channel channel) {
        this.kimChatHome = kimChatHome;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (kimChatHome.getSessionId() == null) {
            return;
        }
        ByteBuf byteBuf = kimChatHome.getChannel().alloc().directBuffer();
        KimRequest request = new KimRequest.Builder()
                .type(MessageType.Command.code)
                .sessionId(kimChatHome.getSessionId())
                .attactments(KimUtils.attachments("from",kimChatHome.getLoginName().getText()))
                .build();
        byteBuf.writeBytes(KimUtils.toBytes(request));
        try {
            ChannelFuture future = kimChatHome.getChannel().writeAndFlush(byteBuf).await();
        } catch (Exception ex){} finally {
            System.exit(0);
        }

    }
}
