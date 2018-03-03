package team.wwg.lansharing.task;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.util.Iterator;

import team.wwg.lansharing.manager.MessageManager;
import team.wwg.lansharing.msg.WaitingSendMsg;

public class UDPSenderTask extends UDPChannelTask {

	MessageManager messageManager = null;
	
	public UDPSenderTask(Selector selector) {
		super(selector);
		initUDPSenderChannel();
	}

	private void initUDPSenderChannel() {
		messageManager = MessageManager.getInstance();
		messageManager.setKey(key);
	}
	
	public void register(){
		try {
			this.key = this.selectableChannel.register(selector, 0);
			messageManager.setKey(key);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void run() {
		
		Iterator<WaitingSendMsg> msgIterator = messageManager.getWaitSendMsgQueue().iterator();
		while(msgIterator.hasNext()){
			WaitingSendMsg msg = msgIterator.next();
			byteBuffer = ByteBuffer.wrap(msg.getMessage());
			try {
				((DatagramChannel) this.selectableChannel).send(byteBuffer, new InetSocketAddress(InetAddress.getByName(msg.getIPAddress()),msg.getPort()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			msgIterator.remove();
		}
	}
}
