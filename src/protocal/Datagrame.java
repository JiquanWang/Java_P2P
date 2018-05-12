package protocal;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;

public class Datagrame implements Serializable{
	private int identifier; // ���ı�ʶ����ȷ�����������͵�����
	private String username; //���ĵķ��ͷ�username
	private String password; //���ķ��ͷ�password
	private int port; //���ͷ��˿�
	private File f;//���ͷ����͵��ļ�
	private String filename;//�����ļ����ļ���
	private long filesize;//�����ļ��Ĵ�С
	private String message;//��Ҫ���͵���Ϣ
	private long ts;
	
	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public File getF() {
		return f;
	}
	public void setF(File f) {
		this.f = f;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getFilesize() {
		return filesize;
	}
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	
	
}
