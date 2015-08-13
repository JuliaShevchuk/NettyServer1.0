package command;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import status.IpCounter;
import status.Request;
import status.StatisticCounter;
import java.util.Formatter;
import java.util.Map;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by yuliya.shevchuk on 03.08.2015.
 */
public class CommandStatus extends Command {

    @Override
    public synchronized void execute(ChannelHandlerContext ctx, Object msg, StatisticCounter statisticCollector) {

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
                createOutput(statisticCollector), CharsetUtil.UTF_8)));

        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

        sendResponse(ctx, msg, response,statisticCollector);

    }

    private synchronized String createOutput(StatisticCounter statisticCollector) {
        StringBuffer stringBuf = new StringBuffer();
        stringBuf.append("\n\n" + new Formatter().format("%60s%n", "STATUS PAGE\n"));
        stringBuf.append("Total amount of requests: " + statisticCollector.getQuantityRequest());
        stringBuf.append("\nUnique requests: " + statisticCollector.getUniqueIpSet().size());
        stringBuf.append("\nActive connection: " + statisticCollector.getActiveConnections() + "\n\n\n");

        stringBuf.append(new Formatter().format("%60s%n%-18s%-15s%-25s%n", "IP STATISTIC", "IP", "Requests", "Time"));

        for (Map.Entry<String, IpCounter> entry : statisticCollector.getIpMap().entrySet()) {
            stringBuf.append(new Formatter().format("%-18s%-2s%n", entry.getKey(), entry.getValue().toString()));
        }

        stringBuf.append("\n" + new Formatter().format("%60s%n%-17s%-25s%n", "REDIRECTS\n", "URI", "Redirects").toString());

        for (Map.Entry<String, Integer> entry : statisticCollector.getUrlMap().entrySet()) {
            stringBuf.append(new Formatter().format("%-17s%-2s%n", entry.getKey(), entry.getValue()));
        }


        stringBuf.append("\n\n\n" + new Formatter().format("%60s%n%-18s%-18s%-25s%-20s%-20s%-20s%n",
                        "LAST CONNECTIONS\n", "IP", "URI", "TIMESTAMP", "SENT_BYTES", "RECEIVED_BYTES", "SPEED"));

        for (Request val : statisticCollector.getStatusQueue()) {
            stringBuf.append(val.toString());
        }
        return stringBuf.toString();
    }
}