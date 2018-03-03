package team.wwg.lansharing.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

public class FileReceiverHandle {
	
	
	private FileOutputStream fos = null;
	private FileChannel filechannel = null;
	
	private int BufferSize = 2 * 1024 * 1024;
	
	private boolean fileReadEnd = false;
	private ByteBuffer buffer = ByteBuffer.allocateDirect(BufferSize);

	
	private File targetFile;
	private int TheBufferCount = 0;

	private Selector selector;
	private String fileUir;
	
	public FileReceiverHandle(Selector selector,File targetFile,String fileuir){
		this.selector = selector;
		this.targetFile = targetFile;
		this.fileUir = fileuir;
	}
	
	
	
	public void handleInput(SelectionKey key) throws IOException {
		if (key.isValid()) {
			SocketChannel sc = (SocketChannel) key.channel();
			if (key.isConnectable()) {
				if (sc.finishConnect()) {
					key.interestOps(SelectionKey.OP_READ);
					System.out.println("ClientRecTask.  key.isConnectable()) { ");
					doWrite(sc);
					selector.wakeup();
				} else {
					System.exit(1);// ����ʧ�ܣ������˳�
				}
			}
			if (key.isReadable()) {
//				System.out.println("ClientRecTask.   key.isReadable() ");
				// �����������Ϣ,��Ϣ�м��������ϵ��ļ���
				receiveFile(sc, targetFile, key);
			}

		}
	}
	
	
	private void doWrite(SocketChannel schannel) throws IOException {
		
		// byte[] bytes = "What time is it now?".getBytes();
		// byte[] bytes = fileUir.getBytes("UTF-8");
		byte[] bytes = fileUir.getBytes();
		ByteBuffer buff = ByteBuffer.allocate(bytes.length);
		buff.put(bytes);
		buff.flip();
		schannel.write(buff);
		// �ж��Ƿ������
		if (!buff.hasRemaining()) {
			System.out.println("SEND SUCCESS!\r\n");
		}
	}
	
	
	private void receiveFile(SocketChannel socketchannel, File file, SelectionKey key) {

		try {
			try {

				if (fos == null) {
					fos = new FileOutputStream(file);
				}

				if (filechannel == null) {
					filechannel = fos.getChannel();
					buffer.clear();
				}

				int size = 0;

				while (key.isReadable()) {

					// ��ᷢ�� filechannel һ��������ǲ������ ��дΪ 0 �����
					// �� socketchannel ��Ϊ�ڷ�����ģʽ����������ÿ�ζ�д������ȷ���Ƿ�ɹ�
					// �򵥴ֱ�������������� ���� ����д�� Ϊ0 ��ʱ�� �ͽ���ѭ����ֱ�����ز�Ϊ0 Ϊֹ

					while (true) {
						size =  socketchannel.read(buffer);
						// System.out.println("size =
						// socketChannel.read(buffer);:::"+size);
//						System.out.println("the read size:" + size);

						TheBufferCount += size;
//						System.out.println("TheBufferCount += size;::::" + TheBufferCount);
						if (TheBufferCount >= BufferSize) {
							TheBufferCount = 0;
							break;
						}
						if (size == 0) {
							key.interestOps(SelectionKey.OP_READ);
							key.selector().wakeup();
							return;
						} else if (size < 0) {
							fileReadEnd = true;
							break;
						}
					}

					buffer.flip();
					int tt = filechannel.write(buffer);
//					System.out.println("filechannel.write(buffer)" + tt);

					buffer.compact();

					// System.out.println("the size :"+size);
					if (fileReadEnd) {
						fileReadEnd = false;
						filechannel.close();
						fos.close();
						socketchannel.close();
						key.cancel();
						key.selector().wakeup();
						JOptionPane.showMessageDialog(null, "�ļ��ѽ�����ϣ�", "�ļ���Ϣ", JOptionPane.INFORMATION_MESSAGE);

						return;
					}

				}

				// key.interestOps(SelectionKey.OP_READ);
				// key.selector().wakeup();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} 
		finally {
			try {
			//	channel.close();
			} catch (Exception ex) {
			}
			try {
			//	fos.close();
			} catch (Exception ex) {
			}
		}
	}
	
}
