package command;


import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
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

    private Timer timer = new HashedWheelTimer();
    private static final String REDIRECT = "REDIRECT";
    private static final String DEFAULT = "DEFAULT";
    private static final String FILENAME = "config.cf";

    private ResourceBundle resource = ResourceBundle.getBundle(FILENAME);

    public abstract void execute(ChannelHandlerContext ctx, Object msg, StatisticCounter statisticCollector);

    private synchronized void checkStatus(ChannelHandlerContext ctx, Object msg, StatisticCounter statisticCollector) {

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            String uri = req.getUri();
            String ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().toString();

            if (!uri.equals(resource.getObject(DEFAULT))) {

                statisticCollector.updateUrlMap(uri);

                if (!uri.contains(resource.getObject(REDIRECT).toString())) {
                    statisticCollector.setQuantityRequest(statisticCollector.getQuantityRequest() + 1);
                    statisticCollector.updateStatusList(ctx, ip, uri);
                    statisticCollector.updateIpMap(ip);
                    statisticCollector.updateUniqueIpSet(ip);

                }
            }
        }
    }

    protected synchronized void sendResponse(ChannelHandlerContext ctx, Object msg, FullHttpResponse response, StatisticCounter statisticCollector) {

        checkStatus(ctx, msg, statisticCollector);

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
                ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            }
        }

    }

}

