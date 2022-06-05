package com.fresh.xy.sample.sleuth;

/**
 * trance: 表示请求链路; 一个tranceId标记一条链路; 有若干个spans组成,形成树状结构
 *
 * span: 基本数据结构 {
 *        tranceId, spanId, desc, timestamped events, tags, parentSpanId, and process IDs
 *  }
 *
 * spans: 若干个span组成,拥有相同的spanId; 如tags的四个取值的span组成一个spans
 *
 * tags: span中的tags取值
 *  cs:客户端已发送。客户提出了要求。此注释指示跨度的开始
 *  sr:接收到服务器：服务器端收到了请求并开始处理它。从此时间戳中减去cs时间戳可显示网络延迟
 *  ss:服务器已发送。在请求处理完成时进行注释（当响应被发送回客户端时）。从此时间戳中减去sr时间戳将显示服务器端处理请求所需的时间
 *  cr:收到客户。表示跨度结束。客户端已成功收到服务器端的响应。从此时间戳中减去cs时间戳将显示客户端从服务器接收响应所需的整个时间
 *
 * context: 在span之间传递的上下文,包括一些字段信息: tranceId, spanIds, key-value: baggage-*(http header中)/baggage_*(messages)
 *
 *
 */
public class FlSleuthSample {

}
