package team.wwg.lansharing.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

import team.wwg.lansharing.util.FileUtil;

public class FileSenderHandle {

	private Selector selector;
	private StringBuilder message;
	int BufferSize = 5 * 1024 * 1024;
	private boolean writeOK = true;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(BufferSize);
	private FileChannel fileChannel;
	private String fileName;
	private boolean fileReadEnd = false;
	private int TheBufferCount = 0;

	public FileSenderHandle(Selector selector) {
		this.selector = selector;

	}

	private void doRread(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		if (writeOK)
			message = new StringBuilder();
		while (true) {
			byteBuffer.clear();
			int r;
			try {
				r = socketChannel.read(byteBuffer);
				if (r == 0)
					break;
				if (r == -1) {
					socketChannel.close();
					key.cancel();
					return;
				}
				message.append(new String(byteBuffer.array(), 0, r));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// �����յ�����Ϣת�����ļ���,��ӳ�䵽�������ϵ�ָ���ļ�
		if (writeOK && invokeMessage(message)) {
			SelectionKey map = key.interestOps(SelectionKey.OP_WRITE);
			map.attach(this);
			selector.wakeup();
			writeOK = false;
		}
	}

	private void doWrite(SelectionKey key) {
		try {

			if (!key.isValid())
				return;
			SocketChannel socketChannel = (SocketChannel) key.channel();
			if (fileChannel == null) {
				fileChannel = new FileInputStream(fileName).getChannel();
				byteBuffer.clear();
			}

			while (key.isWritable()) {

				// ��Ϊ filechannel ��������ʽ�ģ�����һ������£�������� ��ȡ��д��Ϊ0 �����

				while (true) {

					int w = fileChannel.read(byteBuffer);

					// ����ļ���д��,��ص�key��socket
					if (w < 0) {
						if (TheBufferCount == 0) {
							fileReadEnd = true;
						}
						break;
					}
					TheBufferCount += w;
					if (TheBufferCount >= BufferSize) {
						TheBufferCount = 0;
						break;
					}

				}

				byteBuffer.flip();
				// ���ڲ���������ģʽ��˵��ÿһ�ε�д�붼��һ���ɹ���Ҳ����˵��ÿ�ε�д�������ʱ����0�����
				// ��������������жϣ�������ص����㣬������д�룬ֱ���ɹ�д��Ϊֹ
				// ��ᷢ�� filechannel һ��������ǲ������ ��дΪ 0 �����
				// �� socketchannel ��Ϊ�ڷ�����ģʽ����������ÿ�ζ�д������ȷ���Ƿ�ɹ�
				// �򵥴ֱ�������������� ���� ����д�� Ϊ0 ��ʱ�� �ͽ���ѭ����ֱ�����ز�Ϊ0 Ϊֹ

				TheBufferCount = byteBuffer.limit();
				while (TheBufferCount != 0) {

					int zz = socketChannel.write(byteBuffer);
					// while(zz<=0){
					// zz = socketChannel.write(byteBuffer);
					// }
					if (zz == 0) {
					
						key.interestOps(SelectionKey.OP_WRITE);
						byteBuffer.compact();
						selector.wakeup();
						return;
					}

					byteBuffer.compact();
					byteBuffer.flip();
					TheBufferCount = byteBuffer.limit();
				}
				byteBuffer.compact();

				if (fileReadEnd) {
					fileReadEnd = true;
					fileName = null;
					fileChannel.close();
					fileChannel = null;
					writeOK = true;
					socketChannel.close();
					key.cancel();
					JOptionPane.showMessageDialog(null, "�ļ����ͳɹ���ϣ�", "�ļ���Ϣ", JOptionPane.INFORMATION_MESSAGE);
					selector.wakeup();
					return;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		key.interestOps(SelectionKey.OP_WRITE);
		selector.wakeup();
	}

	
	
	public void handle(SelectionKey key) {
		if (key.isReadable()) {
			doRread(key);
		}
		// ��ͻ���д����
		if (key.isWritable()) {
			doWrite(key);
		}
	}

	
	
	private boolean invokeMessage(StringBuilder builder) {
		String m = message.toString();

		m = FileUtil.getRightUri(m);

		try {
			File f = new File(m);
			if (!f.exists())
				return false;
			fileName = m;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
