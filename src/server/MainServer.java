package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.threadClient;
import model.User;
import protocal.Datagrame;

public class MainServer {
	
	private final static int TCP_port = 8098;
	
	//������ά��һ���û��������û����������ڶ˿�
	public final static HashMap<String,Integer> users = new HashMap<String,Integer>();
	public final static HashMap<String,Long> hb = new HashMap<String, Long>();

	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket(TCP_port);
		System.out.println("********����������********");
		Socket socket = new Socket();
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();  
        cachedThreadPool.execute(new Runnable() {  
            public void run() {  
            	while(true){
            		long now = System.currentTimeMillis();
            		//System.out.println("������"+now);
            		Iterator iter = MainServer.hb.entrySet().iterator();
            		//�������������
            		while(iter.hasNext()){
            			Entry entry = (Entry)iter.next();
            			String auser = (String)entry.getKey();
            			long past = (long)entry.getValue();
            			//System.out.println("�û���"+auser+"�ϴ�һ���͵�������Ϊ��"+past);
            			if((now-past)>50000){
            				System.out.println("�û�"+auser+"����ʱ��δ�������������Զ��Ͽ�����");
            				MainServer.hb.remove(auser);
            			}
            		}
            		try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }  
        });   
		while(true){
			socket = ss.accept();
			new threadServer(socket).start();
		}

	}

}
