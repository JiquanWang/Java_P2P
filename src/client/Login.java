package client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.User;
import protocal.Datagrame;

import javax.swing.JSeparator;


public class Login extends JFrame {
	
	private JTextField tuname;
	private JPasswordField tpawd;
	private JTextField tport;
	private Datagrame dg = new Datagrame();
	
	public Login() {
		setTitle("\u56FE\u50CF\u52A0\u5BC6\u7CFB\u7EDF\u5BA2\u6237\u7AEF");
		getContentPane().setLayout(null);
		
		JLabel subtitlelab = new JLabel("\u5728\u7EBF\u804A\u5929\uFF0C\u6587\u4EF6\u4F20\u8F93");
		subtitlelab.setFont(new Font("΢���ź�", Font.ITALIC, 17));
		subtitlelab.setBounds(191, 65, 165, 27);
		getContentPane().add(subtitlelab);
		
		JLabel unamelab = new JLabel("\u7528\u6237\u540D\uFF1A");
		unamelab.setFont(new Font("����", Font.PLAIN, 20));
		unamelab.setBounds(123, 135, 80, 24);
		getContentPane().add(unamelab);
		
		JLabel pawdlab = new JLabel("\u5BC6  \u7801\uFF1A");
		pawdlab.setFont(new Font("����", Font.PLAIN, 20));
		pawdlab.setBounds(123, 182, 80, 24);
		getContentPane().add(pawdlab);
		
		tuname = new JTextField();
		tuname.setBounds(213, 138, 176, 24);
		getContentPane().add(tuname);
		tuname.setColumns(10);
		
		tpawd = new JPasswordField();
		tpawd.setBounds(213, 185, 176, 24);
		getContentPane().add(tpawd);
		
		JButton registerbtn = new JButton("\u6CE8\u518C");    //ע���Action
		registerbtn.setFont(new Font("΢���ź�", Font.BOLD, 16));
		registerbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Register rg = new Register();
				int width = 540;
				int height = 400;
				Toolkit tk = rg.getToolkit();
				Dimension dm = tk.getScreenSize();   //��ȡ���Դ��ڴ�С
				rg.setVisible(true);
				rg.setSize(width, height);		
				rg.setLocation((int) (dm.getWidth() - width) / 2,  (int) (dm.getHeight() - height) / 2);
			}
		});
		registerbtn.setBounds(112, 288, 99, 38);
		getContentPane().add(registerbtn);
		
		JButton loginbtn = new JButton("\u767B\u5F55");   //��¼��Action
		loginbtn.setFont(new Font("΢���ź�", Font.BOLD, 16));
		loginbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String username = String.valueOf(tuname.getText());
				String password = String.valueOf(tpawd.getPassword());
				String port = tport.getText();
				User user = new User();
				
				if(!username.isEmpty() && !password.isEmpty()){
					Socket s;
					try {
						int portnum;
						System.out.println(port);
						if(port == null || port.isEmpty() || port.equals("")){
							//���û�û����д�˿�ʱ,���Ϊ������һ��8000 - 10000��Ķ˿�
							Random random = new Random();
							portnum = random.nextInt(2000) + 8000;
						}
						else{
							portnum = Integer.parseInt(port);
						}
						
						s = new Socket(InetAddress.getLocalHost(), 8098);
						
						//׼�����ģ������û��������룬�˿ڣ������¼
						user.setUsername(username);
						user.setPort(portnum);
						dg.setIdentifier(1);
						dg.setUsername(username);
						dg.setPassword(password);
						dg.setPort(portnum);
						
						//��IO
						OutputStream os = s.getOutputStream();
						InputStream is = s.getInputStream();
						
						//��user�����͸�������
						ObjectOutputStream oos = new ObjectOutputStream(os);
						oos.writeObject(dg);
						s.shutdownOutput();//�رշ��ͣ����߷��������ͽ���
						
						//�ȴ��ظ�
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						byte result =  (byte) br.read();
						System.out.println(result);
						
						//�����¼
						if(result == 1){
//JOptionPane.showMessageDialog(null,"��¼�ɹ���");
							dispose();
							MainFrame mf = new MainFrame(user);
							int width = 985;
							int height = 530;
							Toolkit tk = mf.getToolkit();
							Dimension dm = tk.getScreenSize();   //��ȡ���Դ��ڴ�С
							mf.setVisible(true);
							mf.setSize(width, height);		
							mf.setLocation((int) (dm.getWidth() - width) / 2,  (int) (dm.getHeight() - height) / 2);
						}
						else{
							JOptionPane.showMessageDialog(null,"�û��������벻���ڣ�");
						}
						oos.close();
						os.close();
						is.close();
						s.close();
						
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
				}
			}
		});
		loginbtn.setBounds(309, 288, 99, 38);
		getContentPane().add(loginbtn);
		
		JLabel titlelab = new JLabel("Co   Co");
		titlelab.setFont(new Font("΢���ź�", Font.BOLD, 40));
		titlelab.setBounds(198, 10, 142, 56);
		getContentPane().add(titlelab);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 102, 523, 18);
		getContentPane().add(separator);
		
		JLabel portlab = new JLabel("\u8BF7\u9009\u62E9\u767B\u5F55\u7684\u7AEF\u53E3\uFF1A");
		portlab.setFont(new Font("����", Font.PLAIN, 16));
		portlab.setBounds(123, 233, 154, 24);
		getContentPane().add(portlab);
		
		tport = new JTextField();
		tport.setColumns(10);
		tport.setBounds(275, 235, 114, 24);
		getContentPane().add(tport);
	}
	
	public static void main(String[] args) {
		Login lg = new Login();
		int width = 540;
		int height = 400;
		Toolkit tk = lg.getToolkit();
		Dimension dm = tk.getScreenSize();   //��ȡ���Դ��ڴ�С
		lg.setVisible(true);
		lg.setSize(width, height);		
		lg.setLocation((int) (dm.getWidth() - width) / 2,  (int) (dm.getHeight() - height) / 2);
	}
}
