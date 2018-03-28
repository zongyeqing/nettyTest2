package netty.rpc.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.rpc.serialize.support.RpcSerializeProtocol;

import java.net.InetSocketAddress;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2018年03月16日
 */
public class MessageSendInitializeTask implements Runnable{
    
    private EventLoopGroup eventLoopGroup;
    private InetSocketAddress serverAddress;
    private RpcServerLoader loader;
    private RpcSerializeProtocol seriabizedProtocol;

    MessageSendInitializeTask(EventLoopGroup eventLoopGroup, InetSocketAddress serverAddress, RpcServerLoader loader, RpcSerializeProtocol seriabizedProtocol) {
        this.eventLoopGroup = eventLoopGroup;
        this.serverAddress =serverAddress;
        this.loader = loader;
        this.seriabizedProtocol = seriabizedProtocol;
    }
    
    @Override
    public void run() {
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup)
        .channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new MessageSendChannelInitializer());

        ChannelFuture channelFuture = b.connect(serverAddress);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture channelFuture) {
                if (channelFuture.isSuccess()) {
                    MessageSendHandler handler = channelFuture.channel().pipeline().get(MessageSendHandler.class);
                    MessageSendInitializeTask.this.loader.setSendMessageHandler(handler);
                }
            }
        });
    }
}
