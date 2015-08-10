package command;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import status.StatisticCounter;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */
public class CommandRedirect extends Command {


    @Override
    public void execute(ChannelHandlerContext ctx, Object msg, StatisticCounter statisticCollector) {

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            String uri = req.getUri();

            String url = uri.substring(14);

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.LOCATION, url);

            sendResponse(ctx, msg, response);

        }
        checkStatus(ctx, msg, statisticCollector);
    }
}