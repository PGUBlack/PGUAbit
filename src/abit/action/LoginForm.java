package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.LoginBean;
import abit.util.StringUtil;

public class LoginForm extends ActionForm {

    private Integer kodVuza    = null;
    private Integer id         = null;
    private String  userGroup  = null;
    private String  userName   = null;
    private String  password   = null;
    private String  action     = null;

    public void setKodVuza(Integer value)  { kodVuza    = value;        }
    public void setId(Integer value)       { id         = value;        }
    public void setUserGroup(String value) { userGroup  = value.trim(); }
    public void setUserName(String value)  { userName   = value.trim(); }
    public void setPassword(String value)  { password   = value.trim(); }
    public void setAction(String value)    { action     = value.trim(); }

    public Integer getKodVuza()   { return kodVuza;   }
    public Integer getId()        { return id;        }
    public String  getUserGroup() { return userGroup; }
    public String  getUserName()  { return userName;  }
    public String  getPassword()  { return password;  }
    public String  getAction()    { return action;    }
	
    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodVuza    = null;
        id         = null;
        userGroup  = null;
        userName   = null;
        password   = null;
        action     = null;
    }

    public void setBean(LoginBean bean,
                    HttpServletRequest request,
                    ActionErrors errors)
                    throws ServletException {

        if (bean!=null) {
            if (bean.getKodVuza()!=null) kodVuza = bean.getKodVuza();
            if (bean.getId()!=null) id = bean.getId();
            if (bean.getUserGroup()!=null) userGroup = bean.getUserGroup();
	    if (bean.getUserName()!=null) userName = bean.getUserName();
            if (bean.getPassword()!=null) password = bean.getPassword();
        }
    }

    public LoginBean getBean(HttpServletRequest request, ActionErrors errors)
                           throws ServletException {
        LoginBean bean = new LoginBean();

        bean.setKodVuza(kodVuza);
        bean.setId(id);
	if (userGroup!=null && !userGroup.equals("")) {
            bean.setUserGroup(StringUtil.toDB(userGroup));
        }
	if (userName!=null && !userName.equals("")) {
            bean.setUserName(StringUtil.toDB(userName));
        }
	if (password!=null && !password.equals("")) {
            bean.setPassword(StringUtil.toDB(password));
        }
        return bean;
    }
        
}