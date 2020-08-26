package org.neoflk.kim.cli.command.support.converter;

import com.beust.jcommander.IStringConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author neoflk
 * 创建时间：2020年05月10日
 */

//将返回的字符串以数组形式存储
public class StringListConverter implements IStringConverter<List<String>> {
    @Override
    public List<String> convert(String to) {
        String[] users = to.split(",");
        return Arrays.asList(users);
    }

}
