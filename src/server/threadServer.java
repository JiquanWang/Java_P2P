package server;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.PrivateKey;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import client.MainFrame;
import model.User;
import protocal.Datagrame;
import util.UpLoad;
import util.UserDao;


public class threadServer extends Thread{
	private Socket s;
	UserDao ud = new UserDao();
	
	public threadServer(Socket s){
		this.s = s;
	}
	
	public void respSign(Datagrame dg) throws Exception{
		UserDao ud = new UserDao();
		String username = dg.getUsername();
		String password = dg.getPassword();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		
		//�鿴�û����Ƿ��Ѿ�����
		boolean flag  = ud.ifUserIDExist(username);
		
		if(!flag){
			//����false����ʾ�����ڣ�����ע��
			ud.register(username,password);
			bw.write(1);
		}
		else{
			bw.write(2);
		}
				
		bw.flush();
		s.shutdownOutput();
	}
	
	public void respLogin(Datagrame dg) throws Exception{
		UserDao ud = new UserDao();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		
		String username = dg.getUsername();
		String password = dg.getPassword();
		int port = dg.getPort();
		
		//��ѯ���ݿ⣬�ܷ��¼
		int i = ud.login(username, password);
		if(i == 1){
			//��֪���������û������û���¼
			Iterator iter = MainServer.users.entrySet().iterator();
			while(iter.hasNext()){
				Entry entry = (Entry)iter.next();
				String auser = (String)entry.getKey();
				int aport = (int)entry.getValue();
				if(aport != 0){
					Socket asocket = new Socket(InetAddress.getLocalHost(),aport);
					Datagrame tellall = new Datagrame();
					tellall.setIdentifier(4);
					tellall.setMessage("�û�"+username+"����������ȥ���������ļ���");
					ObjectOutputStream tellallos = new ObjectOutputStream(asocket.getOutputStream());
					tellallos.writeObject(tellall);
					asocket.shutdownOutput();
					asocket.close();
				}
			}
			//���·�����ά�����û���
			MainServer.users.put(username, port);
			
			//����Ŀ�´������û��ļ���
			File dirFile = new File(System.getProperty("user.dir")+"\\"+username);
			boolean fexists = dirFile.exists();
			if(!fexists){
				dirFile.mkdir();
			}
			//�ظ��ͻ���1����ʾ�����¼
			bw.write(1);
		}
		else{
			//��ֹ��¼
			bw.write(2);
		}
		bw.flush();
		s.shutdownOutput();
	}
	
	public void respLogout(Datagrame dg) throws Exception{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		
		String username = dg.getUsername();
		//��֪���������û������û���¼
		Iterator iter = MainServer.users.entrySet().iterator();
		while(iter.hasNext()){
			Entry entry = (Entry)iter.next();
			String auser = (String)entry.getKey();
			int aport = (int)entry.getValue();
			if(aport != 0){
				Socket asocket = new Socket(InetAddress.getLocalHost(),aport);
				Datagrame tellall = new Datagrame();
				tellall.setIdentifier(4);
				tellall.setMessage("�û�"+username+"������");
				ObjectOutputStream tellallos = new ObjectOutputStream(asocket.getOutputStream());
				tellallos.writeObject(tellall);
				asocket.shutdownOutput();
				asocket.close();
			}
		}
		//���·�����ά�����û���
		MainServer.users.put(username, 0);
	}
	
	public void changets(Datagrame dg){
		MainServer.hb.put(dg.getUsername(), dg.getTs());
	}
	public void respUpload(Datagrame dg) throws Exception{
		UpLoad ul = new UpLoad();
		String username = dg.getUsername();
		String filename = dg.getFilename();
		File f = dg.getF();
		long filesize = dg.getFilesize();
		byte[] data = ul.readFile(f);
		ul.byte2file(data, System.getProperty("user.dir")+"\\"+username+"\\"+filename);
	}
	
	public void respUserIndex(Datagrame dg) throws IOException {
		ArrayList<User> users = new ArrayList<User>();
		//�������������û�,����userlist
		Iterator iter = MainServer.users.entrySet().iterator();
		while(iter.hasNext()){
			Entry entry = (Entry)iter.next();
			String auser = (String)entry.getKey();
			int aport = (int)entry.getValue();
			if(aport != 0){
				User u = new User();
				u.setUsername(auser);
				u.setPort(aport);
				users.add(u);
			}
		}
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(users);
		s.shutdownOutput();
	}
	
	
	public void run(){
		InputStream is = null;
		OutputStream os = null;
		try {
			//����SocketIO
			is = s.getInputStream();
			os = s.getOutputStream();
			
			//�������ݱ�����
			ObjectInputStream ois = new ObjectInputStream(is);
			Datagrame dg = null;
			try {
				dg = (Datagrame)ois.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int identifier = dg.getIdentifier();
			
			switch(identifier){
			case 0:
				//��ʶ��Ϊ0ʱ����ʶע�ᱨ��
				System.out.println("The client needs to sign up");
				try {
					this.respSign(dg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				//��ʶ��Ϊ1ʱ����¼����
				System.out.println("The client needs to log in");
				try {
					this.respLogin(dg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				//��ʶ��Ϊ2ʱ�������ļ�
				System.out.println("The client needs to upLoad file");
				try {
					this.respUpload(dg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 3:
				//��ʶ��Ϊ3ʱ����ѯ�û�Ŀ¼
				System.out.println("The client needs to search users");
				try {
					this.respUserIndex(dg);
				} catch(Exception e) {
					e.printStackTrace();
				}
				break;
			case 7:
				//��ʶ��Ϊ7ʱ���û��˳�
				System.out.println("The client needs to quit");
				try {
					this.respLogout(dg);
				} catch(Exception e) {
					e.printStackTrace();
				}
				break;
			case 9:
				//��ʶ��Ϊ8ʱ�������û�״̬
				try {
					this.changets(dg);
				} catch(Exception e) {
					e.printStackTrace();
				}
				break;
			}		
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
