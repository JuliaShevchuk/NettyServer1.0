package command;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import status.StatisticCounter;

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
    public void execute(final ChannelHandlerContext ctx, final Object msg, StatisticCounter statisticCollector) {

        /*ctx.executor().schedule(new Runnable() {
            @Override
            public void run() {*/
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
                        MESSAGE, CharsetUtil.UTF_8)));

                response.headers().set(CONTENT_TYPE, "text/plain");
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

                sendResponse(ctx, msg, response);

        //}, 10, TimeUnit.SECONDS);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        checkStatus(ctx, msg, statisticCollector);

    }
}
