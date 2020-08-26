package org.neoflk.kim.cli.command.support.args;

import com.beust.jcommander.Parameter;
import org.neoflk.kim.common.util.KimUtils;

/**
 * @author neoflk
 * 创建时间：2020年05月10日
 */

//登录命令行
public class LoginArgs {

    //命令行提示
    @Parameter(names = {"--host","-h"},description = "remote server address,default localhost")
    private String host;
    @Parameter(names = {"--port","-p"},description = "remote server port,default 7566")
    private Integer port;
    @Parameter(names = {"--name","-n"},description = "your login nickname")
    private String name;

    //查询服务器
    public String getHost() {
        if (KimUtils.isEmptyParam(host)) {
            host = "localhost";
        }
        return host;
    }

    //查询服务器端口
    public int getPort() {
        if (port == null) {
            port = 7566;
        }
        return port;
    }

    //查询昵称
    public String getName() {
        return name;
    }
}
