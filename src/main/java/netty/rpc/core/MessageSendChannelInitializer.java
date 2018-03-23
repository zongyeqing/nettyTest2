package netty.rpc.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2018年03月16日
 */
public class MessageSendChannelInitializer extends ChannelInitializer {
    
    /**
     * ObjectDecoder 底层默认继承半包解码器LengthFieldBasedFrameDecoder处理粘包问题的时候，
     * 消息头开始即为长度字段，占据4个字节。这里出于保持兼容的考虑
     */
    final public static int MESSAGE_LENGTH = 4;
    
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //ObjectDecoder的基类半包解码器LengthFieldBasedFrameDecoder的报文格式保持兼容。因为底层的父类LengthFieldBasedFrameDecoder
        //的初始化参数即为super(maxObjectSize, 0, 4, 0, 4);
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MessageSendChannelInitializer.MESSAGE_LENGTH, 0, MessageSendChannelInitializer.MESSAGE_LENGTH));
        //利用LengthFieldPrepender回填补充ObjectDecoder消息报文头
        pipeline.addLast(new LengthFieldPrepender(MessageSendChannelInitializer.MESSAGE_LENGTH));
        pipeline.addLast(new ObjectEncoder());
        //考虑到并发性能，采用weakCachingConcurrentResolver缓存策略。一般情况使用:cacheDisabled即可
        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        pipeline.addLast(new MessageSendHandler());
    }
}
