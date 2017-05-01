package com.rens.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
	private ByteBuf clientMessage;

	public SimpleClientHandler() {

		byte[] req = "任硕Call-User-Service".getBytes();
		clientMessage = Unpooled.buffer(req.length);
		clientMessage.writeBytes(req);
	}

	
	private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
					CharsetUtil.UTF_8));

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) { // 2
			IdleStateEvent event = (IdleStateEvent) evt;
			String type = "";
			if (event.state() == IdleState.READER_IDLE) {
				type = "read idle";
			} else if (event.state() == IdleState.WRITER_IDLE) {
				type = "write idle";
			} else if (event.state() == IdleState.ALL_IDLE) {
				ctx.channel().write("ping\n");
			}
			ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(
					ChannelFutureListener.CLOSE_ON_FAILURE); // 3
			System.out.println(ctx.channel().remoteAddress() + "超时类型：" + type);
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		ctx.writeAndFlush(clientMessage);

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];

		buf.readBytes(req);

		String message = new String(req, "UTF-8");

		System.out.println("Netty-Client:Receive Message," + message);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {

		ctx.close();
	}
}
