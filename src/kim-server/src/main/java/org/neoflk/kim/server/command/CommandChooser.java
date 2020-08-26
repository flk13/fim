package org.neoflk.kim.server.command;

import org.neoflk.kim.common.KimException;
import org.neoflk.kim.common.util.PackageScanner;
import org.neoflk.kim.server.command.annotation.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author neoflk
 * 创建时间：2020年04月30日
 */
public class CommandChooser {

    private static final Logger log = LoggerFactory.getLogger(CommandChooser.class);

    public static final Map<String,ICommand> supportCmds = new ConcurrentHashMap<>();

    static {
        List<Class<?>> classes = PackageScanner.scan(PackageScanner.PROJECT_KIM_SERVER,"org.neoflk.kim.server.command.support");
        for (Class clz : classes) {
            if (clz.isAnnotationPresent(Command.class)) {
                Command command = (Command) clz.getDeclaredAnnotation(Command.class);
                try {
                    supportCmds.put(command.value(),(ICommand)clz.newInstance());
                } catch (InstantiationException e) {
                    log.error(e.getMessage(),e);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    public ICommand choose(String cmd) {
        check(cmd);
        return supportCmds.get(cmd);
    }


    public static void addCmd(String name,ICommand command) {
        supportCmds.put(name,command);
    }

    public void check(String cmd) {
        if (!supportCmds.containsKey(cmd)) {
            throw new KimException("unsupport command ["+cmd+"]");
        }
    }

}
