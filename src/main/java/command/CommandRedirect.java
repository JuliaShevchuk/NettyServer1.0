package command;


import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import status.StatisticCollector;

import java.net.InetSocketAddress;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */
public class CommandRedirect extends Command {


    @Override
    public void execute(ChannelHandlerContext ctx, Object msg, StatisticCollector statisticCollector) {

        checkStatus(ctx, msg, statisticCollector);

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            String uri = req.getUri();


            String url = uri.substring(15);
            statisticCollector.updateUrlMap(url);

            System.out.println(statisticCollector.getUrlMap().toString());

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.FOUND);
            response.headers().set(HttpHeaders.Names.LOCATION, url);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

        }

    }
}