package netty.rpc.core;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import netty.rpc.model.MessageRequest;
import netty.rpc.model.MessageResponse;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.util.Map;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2018年03月23日
 */
public class MessageRecvInitializeTask implements Runnable {
    
    private MessageRequest request = null;
    private MessageResponse response = null;
    private Map<String, Object> handlerMap = null;
    private ChannelHandlerContext ctx = null;

    public MessageResponse getResponse() {
        return response;
    }

    public MessageRequest getRequest() {
        return request;
    }
    
    public void setRequest(MessageRequest request) {
        this.request = request;
    }
    
    MessageRecvInitializeTask(MessageRequest request, MessageResponse response, Map<String, Object> handlerMap, ChannelHandlerContext ctx) {
        this.request = request;
        this.response = response;
        this.handlerMap = handlerMap;
        this.ctx = ctx;
    }

    public void run() {
        
        response.setMessageId(request.getMessageId());
        try {
            Object result = reflect(request);
            response.setResult(result);
        }catch (Throwable t) {
            response.setError(t.toString());
            t.printStackTrace();
            System.err.println("RPC Server invoke error\n");
        }
        
        ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                System.out.println("RPC Server send message-id response: " + request.getMessageId());
            }
        });
        
    }
    
    private Object reflect(MessageRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);
        String methodName = request.getMethodName();
        Object[] parameters = request.getParametersVal();
        return MethodUtils.invokeMethod(serviceBean, methodName, parameters);
    }
}
