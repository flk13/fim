package org.neoflk.kim.cli.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.neoflk.kim.cli.handler.KimCliHandler;
import org.neoflk.kim.common.codec.KimResponseDecoder;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author neoflk
 * 创建时间：2020年05月10日
 */
public class KimChannelInitializer extends ChannelInitializer<SocketChannel> {

    private String loginName;
    private SocketChannel channel;
    private Map<String,Object> holder;

    public KimChannelInitializer(Map<String,Object> holder,String loginName) {
        this.holder = holder;
        this.loginName = loginName;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new LineBasedFrameDecoder(4096));
        ch.pipeline().addLast(new StringDecoder(Charset.forName("utf-8")));
        ch.pipeline().addLast(new KimResponseDecoder());
        ch.pipeline().addLast(new KimCliHandler(holder,loginName));
        this.channel = ch;

    }
}
