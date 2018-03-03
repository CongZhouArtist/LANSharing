package team.wwg.lansharing.service;

import java.io.IOException;
import java.nio.channels.MulticastChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import team.wwg.lansharing.manager.MessageManager;
import team.wwg.lansharing.manager.TaskManager;
import team.wwg.lansharing.task.BaseTask;
import team.wwg.lansharing.task.MulticastReceiver;
import team.wwg.lansharing.task.TCPFileServerTask;
import team.wwg.lansharing.task.UDPReceiverTask;
import team.wwg.lansharing.task.UDPSenderTask;
import team.wwg.lansharing.util.CreateExcutorServiceUtil;

public class ServerControl implements Runnable{
	
	private TaskManager taskManager = null;
	private ExecutorService executor = null; 
	private Selector selector = null;
	public ServerControl() {
		
//		this.selector = selector;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		executor = CreateExcutorServiceUtil.createExecutorService();
		try {
			selector = Selector.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TCPFileServerTask tcpFlieServerTask =new TCPFileServerTask(selector);
		
		MulticastReceiver multicastReceiver = new MulticastReceiver(selector);
		
		UDPReceiverTask udpReceiverTask = new UDPReceiverTask(selector);
		
		UDPSenderTask udpSenderTask = new UDPSenderTask(selector);

		
		taskManager = TaskManager.getInstance();
	}

	@Override
	public void run() {
		int ret;
		System.out.println("servertask  running");
		while(true){
			TaskManager.getInstance().register();
			try {
				ret = selector.select(0);
				if (ret == 0) {
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readyKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				if (key.isValid()) {
					BaseTask task = taskManager.getCompleteTask(key.channel());
					key.interestOps(0);
					if (null != task) {
						if (task.isInterrupted()) {

						}
						executor.execute(task);
					}
				}
			}
		}
	}
}
