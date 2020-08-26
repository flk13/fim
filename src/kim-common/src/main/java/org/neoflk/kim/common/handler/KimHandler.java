package org.neoflk.kim.common.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.neoflk.kim.common.req.KimRequest;
import org.neoflk.kim.common.resp.KimResponse;
import org.neoflk.kim.common.util.KimUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author neoflk
 * 创建时间：2020年04月29日
 */
public class KimHandler extends ChannelInboundHandlerAdapter {

    protected static final String USER_DELIMITER = ",";

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected static final Gson GSON = new GsonBuilder().create();

    protected void writeRequest(ChannelHandlerContext ctx, KimRequest kimRequest) {
        this.writeRequest(ctx,null,kimRequest);
    }

    protected void writeRequest(ChannelHandlerContext ctx,Channel ch,KimRequest kimRequest) {
        ByteBuf byteBuf = ctx.alloc().directBuffer();
        byteBuf.writeBytes(KimUtils.toBytes(kimRequest));
        if (ch != null) {
            ch.writeAndFlush(byteBuf);
        } else {
            ctx.channel().writeAndFlush(byteBuf);
        }
    }

    protected void writeResponse(ChannelHandlerContext ctx, KimResponse kimResponse) {
        this.writeResponse(ctx,null,kimResponse);
    }

    protected void writeResponse(ChannelHandlerContext ctx,Channel ch,KimResponse kimResponse) {
        ByteBuf byteBuf = ctx.alloc().directBuffer();
        byteBuf.writeBytes(KimUtils.toBytes(kimResponse));
        if (ch != null) {
            ch.writeAndFlush(byteBuf);
        } else {
            ctx.channel().writeAndFlush(byteBuf);
        }
    }

}
