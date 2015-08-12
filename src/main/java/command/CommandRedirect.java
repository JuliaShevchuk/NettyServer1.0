package command;


import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import status.StatisticCounter;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */
public class CommandRedirect extends Command {


    @Override
    public synchronized void execute(ChannelHandlerContext ctx, Object msg, StatisticCounter statisticCounter) {


        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            String url = req.getUri().substring(15);

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.FOUND);
            response.headers().set(HttpHeaders.Names.LOCATION, url);

            sendResponse(ctx, msg, response, statisticCounter);
        }

    }
}