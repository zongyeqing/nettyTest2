package com.zong.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2017年04月28日
 */
public class MyHandler extends SimpleChannelInboundHandler {
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        handlerHttpRequest(channelHandlerContext,msg);
    }

    private void handlerHttpRequest(ChannelHandlerContext channelHandlerContext, Object msg) {
        
    }
}
