package command;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import status.IpCounter;
import status.Statistic;
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
    public void execute(ChannelHandlerContext ctx, Object msg, StatisticCounter statisticCounter) {

        checkStatus(ctx, msg, statisticCounter);

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
                        createOutput(statisticCounter), CharsetUtil.UTF_8)));

        response.headers().set(CONTENT_TYPE, "text/html");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

        sendResponse(ctx, msg, response, statisticCounter);


    }


    private String createOutput(StatisticCounter statisticCounter) {
        StringBuffer buf = new StringBuffer()
                .append("<!DOCTYPE html>")
                .append("<html>")


                .append("<style>" +
                        "h3{" +
                        "font-family: Arial, fantasy;" +
                        "color: green;" +
                        "text-align: center;}")

                .append("h4{" +
                        "font-family: Arial, fantasy;" +
                        "color: green;}")

                .append("table, td, tr {" +
                        "border: 1px solid black;" +
                        "background-color: darkseagreen;}")

                .append("body, tbody, tr, th{" +
                        "background-color:lightcyan}" +
                        "</style>")


                .append("<head>")
                .append("<title>Status</title>")
                .append("</head><body>")

                .append("<h3>STATUS PAGE</h3>")

                .append("Total amount of requests: " + statisticCounter.getQuantityRequest() + "</br>")
                .append("Unique requests: " + statisticCounter.getUniqueIpSet().size() + "</br>")
                .append("Active connection: " + statisticCounter.getActiveConnections() + "</br>")
                .append("<h4>IP STATISTIC</h4>")

                .append("<table>")
                .append("<tr>" +
                        "<td>IP</td>" +
                        "<td>REQUESTS</td>" +
                        "<td>TIME</td>" +
                        "</tr><tbody>");


        for (IpCounter ipCounter : statisticCounter.getIpList()) {
            buf.append("<tr>" +
                    "<th>" + ipCounter.getIp() + "</th>" +
                    "<th>" + ipCounter.getQuantity() + "</th>" +
                    "<th>" + ipCounter.getDate() + "</th>" +
                    "</tr>");
        }
        buf.append("</tbody></table>");


        buf.append("<h4>REDIRECTS</h4>")
                .append("<table/>")
                .append("<tr>" +
                        "<td>URI</td>" +
                        "<td>Redirects</td>" +
                        "</tr><tbody>");

        for (Map.Entry<String, Integer> entry : statisticCounter.getUrlMap().entrySet()) {
            buf.append("<tr>" +
                    "<th>" + entry.getKey() + "</th>" +
                    "<th>" + entry.getValue() + "</th>" +
                    "</tr>");
        }
        buf.append("</tbody></table>");

        buf.append("<h4>LAST CONNECTIONS</h4>")
                .append("<table>")
                .append("<tr>" +
                        "<td>IP</td>" +
                        "<td>URI</td>" +
                        "<td>TIMESTAMP</td>" +
                        "<td>SENT_BYTES</td>" +
                        "<td>RECEIVED_BYTES</td>" +
                        "<td>SPEED</td>" +
                        "</tr><tbody>");

        for (Statistic val : statisticCounter.getStatusQueue()) {
            buf.append("<tr>" +
                    "<th>" + val.getIp() + "</th>" +
                    "<th>" + val.getUrl() + "</th>" +
                    "<th>" + val.getTimestamp() + "</th>" +
                    "<th>" + val.getSentBytes() + "</th>" +
                    "<th>" + val.getReceivedBytes() + "</th>" +
                    "<th>" + val.getSpeed() + "</th>" +
                    "</tr>");
        }
        buf.append("</tbody></table>");

        buf.append("</body></html>");

        return buf.toString();
    }
}