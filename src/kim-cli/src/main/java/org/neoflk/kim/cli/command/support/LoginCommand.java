package org.neoflk.kim.cli.command.support;

import com.beust.jcommander.JCommander;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.neoflk.kim.cli.KimCli;
import org.neoflk.kim.cli.command.AbstractCliCommand;
import org.neoflk.kim.cli.command.KimCommand;
import org.neoflk.kim.cli.command.annotation.Cmd;
import org.neoflk.kim.cli.command.support.args.LoginArgs;
import org.neoflk.kim.cli.initializer.KimChannelInitializer;

/**
 * @author neoflk
 * 创建时间：2020年05月10日
 */

//登录
@Cmd(value = "login",description = "login kim to chat with others,see [login] command for details")
public class LoginCommand extends AbstractCliCommand implements KimCommand {
    @Override
    public void execute(String[] params) {
        LoginArgs loginArgs = new LoginArgs();
        JCommander commander = JCommander.newBuilder()
                .addObject(loginArgs)
                .build();
        try {
            commander.parse(params);
        } catch (Exception ex) {
            if (params.length > 1) {
                commander.usage();
                System.out.print("[kim@"+ KimCli.loginName+"]#");
                return;
            }
        }
        if (params.length == 0) {
            commander.usage();
            System.out.print("[kim@"+ KimCli.loginName+"]#");
        } else {
            try {
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                Bootstrap b = new Bootstrap();
                b.group(workerGroup);
                b.channel(NioSocketChannel.class);
                b.option(ChannelOption.SO_KEEPALIVE, true);
                b.handler(new KimChannelInitializer(holder.get(),loginArgs.getName()==null?params[0]:loginArgs.getName()));
                ChannelFuture f = b.connect(loginArgs.getHost(), loginArgs.getPort()).sync();
            } catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }

    }
}
