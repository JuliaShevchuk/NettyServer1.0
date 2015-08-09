package command;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import status.StatisticCollector;

import java.net.InetSocketAddress;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 * Created by Julia on 26.05.2015.
 */
public abstract class Command {

    protected  final String DEFAULT_URI = "/favicon.ico";

    public  abstract void execute(ChannelHandlerContext ctx, Object msg, StatisticCollector statisticCollector);

    public void checkStatus(ChannelHandlerContext ctx, Object msg, StatisticCollector statisticCollector) {

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            if (HttpHeaders.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }

            String uri = req.getUri();
            String ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().toString();

            if(!req.getUri().equals(DEFAULT_URI)){
                statisticCollector.setQuantityRequest(statisticCollector.getQuantityRequest()+1);
                statisticCollector.updateStatusList(ip, uri);
                statisticCollector.updateIpMap(ip);

            }

        }
    }

}
