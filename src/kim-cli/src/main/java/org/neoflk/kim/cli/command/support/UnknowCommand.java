package org.neoflk.kim.cli.command.support;

import org.neoflk.kim.cli.KimCli;
import org.neoflk.kim.cli.command.KimCommand;
import org.neoflk.kim.cli.command.annotation.Cmd;

/**
 * @author neoflk
 * 创建时间：2020年05月10日
 */

//定义未知命令提示 
@Cmd(value = "unknow",description = "unknow command,that means not supprted by kim in current")
public class UnknowCommand implements KimCommand {
    @Override
    public void execute(String[] params) {
        System.out.println("unknow command");
        System.out.print("[kim@"+ KimCli.loginName+"]#");
    }
}
