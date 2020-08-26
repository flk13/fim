package org.neoflk.kim.common.codec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.neoflk.kim.common.req.KimRequest;

import java.util.List;

/**
 * @author neoflk
 * 创建时间：2020年04月29日
 */
@Sharable
public class KimRequestDecoder extends MessageToMessageDecoder<String> {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        out.add(GSON.fromJson(msg, KimRequest.class));
    }
}
