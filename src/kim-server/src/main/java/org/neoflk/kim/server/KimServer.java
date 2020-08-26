package org.neoflk.kim.server;

import com.google.common.base.Stopwatch;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import org.neoflk.kim.common.codec.KimRequestDecoder;
import org.neoflk.kim.server.handler.KimCheckHandler;
import org.neoflk.kim.server.handler.KimServerHandler;
import org.neoflk.kim.server.handler.http.KimHttpHandler;
import org.neoflk.kim.server.handler.http.HttpRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * @author neoflk
 * 创建时间：2020年04月27日
 */

public class KimServer {

    private static final Logger log = LoggerFactory.getLogger(KimServer.class);
    private static final Properties CONFIG = new Properties();

    static  {
        try {
            InputStream inputStream = KimServer.class.getResource("/application.properties").openStream();
            CONFIG.load(inputStream);
        } catch (Exception ex) {
            log.error("can't find application.properties ",ex);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Thread tcpThread = new Thread(()->{
            startTcpServer(Integer.parseInt(CONFIG.getProperty("kim.server.tcp.port")),stopwatch);
        });
        Thread httpThread = new Thread(()->{
            startHttpServer(Integer.parseInt(CONFIG.getProperty("kim.server.http.port")),stopwatch);
        });
        tcpThread.start();
        httpThread.start();
        try {
            tcpThread.join();
            httpThread.join();
        } catch (Exception ex) {
            log.error(ex.getMessage(),ex);
        }
        log.info("all server started in {}",stopwatch.stop());
    }

    public static void startTcpServer(int port,Stopwatch stopwatch) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup,workGroup)
                .option(ChannelOption.SO_BACKLOG,2014)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    public void initChannel(NioSocketChannel ch){
                        ch.pipeline().addLast(new LineBasedFrameDecoder(4096));
                        ch.pipeline().addLast(new StringDecoder(Charset.forName("utf-8")));
                        ch.pipeline().addLast(new KimRequestDecoder());
                        ch.pipeline().addLast(new KimCheckHandler());
                        ch.pipeline().addLast(new KimServerHandler());
                    }
                });
        try {
            serverBootstrap.bind(port).sync();
            log.info("tcp server started on {}, elapsed time {} ms",port,stopwatch.elapsed().toMillis());
        } catch (Exception ex) {
            log.error("start tcp server error",ex);
        }
    }

    public static void startHttpServer(int port,Stopwatch stopwatch) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        HttpRouter httpRouter = HttpRouter.init(CONFIG);

        serverBootstrap.group(bossGroup,workGroup)
                .option(ChannelOption.SO_BACKLOG,2014)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    public void initChannel(NioSocketChannel ch){
                        ch.pipeline().addLast(new HttpRequestDecoder());
                        ch.pipeline().addLast(new HttpObjectAggregator(65535));
                        ch.pipeline().addLast(new HttpResponseEncoder());
                        ch.pipeline().addLast(new KimHttpHandler(httpRouter));
                    }
                });
        try {
            serverBootstrap.bind(port).sync();
            log.info("http server started on {},elapsed time {} ms",port,stopwatch.elapsed().toMillis());
        } catch (Exception ex) {
            log.error("start http server error",ex);
        }
    }

}