package org.neoflk.kim.client.connect;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.neoflk.kim.client.handler.KimClientHandler;
import org.neoflk.kim.client.ClientFrame;
import org.neoflk.kim.common.codec.KimResponseDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author neoflk
 * 创建时间：2020年04月27日
 */
public class KimClient {

    private static final Logger log = LoggerFactory.getLogger(KimClient.class);

    private ClientFrame kimChatHome;

    public KimClient(ClientFrame kimChatHome) {
        this.kimChatHome = kimChatHome;
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public void connect(String host,int port,String nickname,int retrytimes) throws Exception {
        try {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LineBasedFrameDecoder(4096));
                    ch.pipeline().addLast(new StringDecoder(Charset.forName("utf-8")));
                    ch.pipeline().addLast(new KimResponseDecoder());
                    ch.pipeline().addLast(new KimClientHandler(ch,kimChatHome,nickname));
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            log.info("client started...");
        } finally {
//
        }
    }

}
