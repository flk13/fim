package org.neoflk.kim.server.handler.http.support;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.neoflk.kim.server.handler.http.RouteHandler;
import org.neoflk.kim.server.handler.http.annotation.Router;
import org.neoflk.kim.server.storage.DataCenter;


/**
 * @author neoflk
 * 创建时间：2020年05月08日
 */
@Router("/onlineUser")
public class OnlineUserHandler implements RouteHandler {
    @Override
    public void handle(ChannelHandlerContext ctx, FullHttpRequest request) {

        ByteBuf byteBuf = ctx.alloc().directBuffer();
        byteBuf.writeBytes(new Gson().toJson(DataCenter.USER_CHANNEL_MAP.keySet()).getBytes());

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK);
        response.content().writeBytes(byteBuf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
