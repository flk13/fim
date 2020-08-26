package org.neoflk.kim.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.neoflk.kim.client.ui.LoginFrame;
import org.neoflk.kim.client.ui.event.LogoutListener;
import org.neoflk.kim.client.ui.event.ChatListener;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.util.KimUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * @author neoflk
 * 创建时间：2020年04月27日
 */
public class ClientFrame extends JFrame {

    private JTextArea chatArea,inputArea;
    private JComboBox comboBox;
    private JLabel loginName;
    private JButton connectBtn;
    private ChatListener chatListener;
    private String sessionId;
    private Channel channel;

    public ClientFrame() {
        this.setName("聊天主页");
        comboBox = new JComboBox();

        this.setSize(600,492);
        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());
        chatArea = new JTextArea(18,52);
        chatArea.setAutoscrolls(true);
        chatArea.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(chatArea);
        jScrollPane.setBounds(0,0,18,52);
        container.add(jScrollPane);
        inputArea = new JTextArea(4,46);
        chatArea.setLineWrap(true);
        inputArea.setLineWrap(true);

        loginName = new JLabel();

        connectBtn = new JButton("加入聊天");
        JButton exitBtn = new JButton("退出聊天");

        container.add(connectBtn);
        container.add(exitBtn);
        container.add(loginName);
        JLabel sendTo = new JLabel("发送给:");
        container.add(sendTo);
        container.add(comboBox);

        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBounds(0,0,5,46);
        container.add(inputScroll);
        JButton sendBtn = new JButton("发送");

        container.add(sendBtn);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        chatListener = new ChatListener(this);
        sendBtn.addMouseListener(chatListener);
        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String msg = ClientFrame.this.inputArea.getText();
                    if (KimUtils.isEmptyParam(msg)) {
                        return;
                    }
                    ByteBuf byteBuf = channel.alloc().directBuffer();
                    Map<String,String> attachments = KimUtils.attachments("to",ClientFrame.this.comboBox.getSelectedItem().toString());
                    attachments.put("from",ClientFrame.this.getLoginName().getText());
                    KimRequest requestParam = new KimRequest
                            .Builder()
                            .type(MessageType.Onchat.code)
                            .sessionId(ClientFrame.this.getSessionId())
                            .attactments(attachments)
                            .body(msg)
                            .build();
                    byteBuf.writeBytes(KimUtils.toBytes(requestParam));
                    channel.writeAndFlush(byteBuf);
                }
            }
        });
        connectBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!ClientFrame.this.getConnectBtn().isEnabled()) {
                    return;
                }
                new LoginFrame(ClientFrame.this,loginName);

            }
        });
        exitBtn.addMouseListener(new LogoutListener(this,getChannel()));
    }

    public JComboBox getComboBox() {
        return this.comboBox;
    }

    public JTextArea getChatArea() {
        return this.chatArea;
    }

    public JTextArea getInputArea() {
        return inputArea;
    }

    public JLabel getLoginName() {
        return loginName;
    }

    public JButton getConnectBtn() {
        return connectBtn;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public static void main(String[] args) {
        new ClientFrame();
    }

}

