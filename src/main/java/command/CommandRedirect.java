package command;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import status.StatisticCounter;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */
public class CommandRedirect extends Command {

    public static final String HELLO = "HELLO";
    public static final String STATUS = "STATUS";
    private static final String FILENAME = "config.cf";

    private ResourceBundle resource = ResourceBundle.getBundle(FILENAME);

    @Override
    public synchronized void execute(ChannelHandlerContext ctx, Object msg, StatisticCounter statisticCounter) {

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            String uri = req.getUri();

            Matcher matcher = Pattern.compile("(^/redirect\\?url=)(.*)").matcher(uri);

            if (matcher.find()) {
                uri = matcher.group(2);

            }


            if (!uri.equals(resource.getObject(STATUS).toString())
                    && !uri.equals(resource.getObject(HELLO).toString())) {
                checkStatus(ctx, msg, statisticCounter);
            }

            statisticCounter.updateUrlMap(uri);

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.FOUND);
            response.headers().set(HttpHeaders.Names.LOCATION, uri);

            sendResponse(ctx, msg, response, statisticCounter);
        }

    }

}
