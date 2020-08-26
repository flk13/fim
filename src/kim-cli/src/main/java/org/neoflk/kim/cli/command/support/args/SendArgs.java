package org.neoflk.kim.cli.command.support.args;

import com.beust.jcommander.Parameter;
import org.neoflk.kim.common.util.KimUtils;

/**
 * @author neoflk
 * 创建时间：2020年05月10日
 */

//发送命令
public class SendArgs {

    //命令提示
    @Parameter(names = {"--to","-t"},description = "message send to the user who was specified,default to ALL if not specified")
    private String to;
    @Parameter(names = {"--msg","-m"},description = "the message you want to send,user single quotes('') to wrap the message")
    private String msg;

    //如果发送对象为空，默认发送全体
    public String getTo() {
        if (KimUtils.isEmptyParam(to)) {
            to = "ALL";
        }
        return to;
    }

    //返回msg
    public String getMsg() {
        return msg;
    }
}
