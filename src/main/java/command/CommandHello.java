package command;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;
import status.StatisticCounter;

import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */

public class CommandHello extends Command {


    public static final String MESSAGE = "Hello World!";

    @Override
    public  void execute(final ChannelHandlerContext ctx, final Object msg, final StatisticCounter statisticCounter) {

        checkStatus(ctx, msg, statisticCounter);

        ctx.executor().schedule(new Runnable() {
            @Override
            public void run() {

                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                        Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(MESSAGE, CharsetUtil.UTF_8)));

                response.headers().set(CONTENT_TYPE, "text/plain");
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

                sendResponse(ctx, msg, response, statisticCounter);

            }
        }, 10, TimeUnit.SECONDS);


    }

    @Override
    protected  void sendResponse(ChannelHandlerContext ctx, Object msg, FullHttpResponse response, StatisticCounter statisticCounter) {

        super.sendResponse(ctx, msg, response, statisticCounter);
        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

    }
}
