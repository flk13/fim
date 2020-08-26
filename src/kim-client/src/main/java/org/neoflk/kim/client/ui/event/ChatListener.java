package org.neoflk.kim.client.ui.event;

import io.netty.buffer.ByteBuf;
import org.neoflk.kim.client.ClientFrame;
import org.neoflk.kim.common.constants.KimConstants;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.util.KimUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * @author neoflk
 * 创建时间：2020年04月28日
 */
public class ChatListener extends MouseAdapter {

    private ClientFrame kimFrame;

    public ChatListener(ClientFrame kimFrame) {
        this.kimFrame = kimFrame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(kimFrame.getSessionId() == null) {
            return;
        }
        String msg = kimFrame.getInputArea().getText();
        if (KimUtils.isEmptyParam(msg)) {
            return;
        }
        String to = kimFrame.getComboBox().getSelectedItem().toString();
        if (kimFrame.getLoginName().getText().equalsIgnoreCase(to)) {
            JOptionPane.showMessageDialog(kimFrame,"不能给自己发信息");
            return;
        }
        ByteBuf byteBuf = kimFrame.getChannel().alloc().directBuffer();
        Map<String,String> attachments = KimUtils.attachments(KimConstants.TO,to);
        attachments.put(KimConstants.FROM,kimFrame.getLoginName().getText());
        KimRequest requestParam = new KimRequest
                .Builder()
                .type(MessageType.Onchat.code)
                .sessionId(kimFrame.getSessionId())
                .attactments(attachments)
                .body(msg)
                .build();
        byteBuf.writeBytes(KimUtils.toBytes(requestParam));
        kimFrame.getChannel().writeAndFlush(byteBuf);
    }
}
