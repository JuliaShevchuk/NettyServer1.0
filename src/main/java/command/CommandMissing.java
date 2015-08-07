package command;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import status.StatisticCollector;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */
public class CommandMissing extends Command {


    @Override
    public void execute(ChannelHandlerContext ctx, Object msg, StatisticCollector statisticCollector) {

        checkStatus(ctx, msg, statisticCollector);

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;


            if (HttpHeaders.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
                    "Error!", CharsetUtil.UTF_8)));


            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

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