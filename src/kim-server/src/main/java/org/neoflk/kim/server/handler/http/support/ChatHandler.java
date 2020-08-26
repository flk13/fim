package org.neoflk.kim.server.handler.http.support;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.neoflk.kim.common.constants.KimConstants;
import org.neoflk.kim.common.constants.MessageType;
import org.neoflk.kim.common.resp.KimResponse;
import org.neoflk.kim.common.util.KimUtils;
import org.neoflk.kim.server.handler.http.RouteHandler;
import org.neoflk.kim.server.handler.http.annotation.Router;
import org.neoflk.kim.server.storage.DataCenter;


/**
 * @author neoflk
 * 创建时间：2020年05月08日
 */
@Router("/chat")
public class ChatHandler implements RouteHandler {
    @Override
    public void handle(ChannelHandlerContext ctx, FullHttpRequest request) {
        ByteBuf byteBuf = request.content();
        byte[] bts = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bts);

        for (Channel ch : DataCenter.USER_CHANNEL_MAP.values()) {
           try {
               ByteBuf byteBuf1 = ch.alloc().directBuffer();
               KimResponse kimResponse = new KimResponse.Builder()
                       .code(200)
                       .type(MessageType.Onchat.code)
                       .attactments(KimUtils.attachments(KimConstants.LOGIN_NAME,"admin"))
                       .body(new String(bts,"utf8"))
                       .build();
               byteBuf1.writeBytes(KimUtils.toBytes(kimResponse));
               ch.writeAndFlush(byteBuf1);
           } catch (Exception ex) {}
        }

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK);
        response.content().writeBytes(byteBuf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
