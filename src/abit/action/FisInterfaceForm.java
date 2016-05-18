package abit.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class FisInterfaceForm extends ActionForm{
	private  String address;
	private String    action          = null;
	private  String method;
	private String login;
	private  String password;
	private  Integer timeOut;
	private Integer codeX;
	private  FormFile sourceFile;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}
	public Integer getCodeX() {
		return codeX;
	}
	public void setCodeX(Integer codeX) {
		this.codeX = codeX;
	}
	public FormFile getSourceFile() {
		return sourceFile;
	}
	public void setSourceFile(FormFile sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	

}
