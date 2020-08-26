package org.neoflk.kim.cli.command.support;

import org.neoflk.kim.cli.KimCli;
import org.neoflk.kim.cli.command.KimCommand;
import org.neoflk.kim.cli.command.CommandSelector;
import org.neoflk.kim.cli.command.annotation.Cmd;

import java.util.Map;

/**
 * @author neoflk
 * 创建时间：2020年05月10日
 */

//帮助命令 
@Cmd(value = "help",description = "show all command that kim supported for users on current")
public class HelpCommand implements KimCommand {

    @Override
    public void execute(String[] params) {
        for (Map.Entry<String,String> cmd : CommandSelector.newCommandSelector().allCmd().entrySet()) {
            System.out.println("------------------------------------------------------------------------------------------");
            String keySpacke = "";
            for (int i=0;i<10-cmd.getKey().length();i++) {
                keySpacke+=" ";
            }
            String valueSpace = "";
            for (int i=0;i<73-cmd.getValue().length();i++) {
                valueSpace+=" ";
            }
            System.out.println("| "+cmd.getKey()+keySpacke+" | "+cmd.getValue()+valueSpace+" |");
        }
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.print("[kim@"+ KimCli.loginName+"]#");
    }
}
