package command;


import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import status.StatisticCounter;

import java.net.InetSocketAddress;
import java.util.ResourceBundle;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 * Created by Julia on 26.05.2015.
 */
public abstract class Command {


    private static final String DEFAULT = "DEFAULT";
    private static final String FILENAME = "config.cf";


    public abstract void execute(ChannelHandlerContext ctx, Object msg, StatisticCounter statisticCounter);

    protected synchronized void checkStatus(ChannelHandlerContext ctx, Object msg, StatisticCounter statisticCounter) {

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            String uri = req.getUri();
            String ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().toString();

            if (!uri.equals(ResourceBundle.getBundle(FILENAME)
                    .getObject(DEFAULT))) {


                statisticCounter.setQuantityRequest(statisticCounter.getQuantityRequest() + 1);
                statisticCounter.updateStatusList(ctx, ip, uri);
                statisticCounter.updateIpList(ip);
                statisticCounter.updateUniqueIpSet(ip);

            }
        }
    }

    protected synchronized void sendResponse(ChannelHandlerContext ctx, Object msg, FullHttpResponse response,
                                             StatisticCounter statisticCounter) {

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            if (HttpHeaders.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }

            boolean keepAlive = HttpHeaders.isKeepAlive(req);

            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                ctx.write(response);

            }
        }

    }

}

