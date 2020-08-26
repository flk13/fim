package org.neoflk.kim.cli.command;

import io.netty.channel.socket.SocketChannel;

/**
 * @author neoflk
 * 创建时间：2020年05月10日
 */

//netty的channelholder
public class ChannelHolder {

    ThreadLocal<SocketChannel> holder = new ThreadLocal<>();

    public void set(SocketChannel socketChannel) {
        holder.set(socketChannel);
    }

    public SocketChannel get() {
        return holder.get();
    }

}
