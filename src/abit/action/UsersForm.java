package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class UsersForm extends ActionForm {

    private Integer kodAbiturienta       = null;
    private Integer kodGruppy            = null;
    private String  description          = null;
    private String  familija             = null;
    private String  imja                 = null;
    private String  otchestvo            = null;
    private String  userName             = null;
    private String  password             = null;
    private String  password2            = null;
    private String  special3             = null;
    private String  action               = null;

    public Integer getKodAbiturienta()  { return kodAbiturienta;       }
    public Integer getKodGruppy()       { return kodGruppy;            }
    public String  getDescription()     { return description;          }
    public String  getFamilija()        { return familija;             }
    public String  getImja()            { return imja;                 }
    public String  getOtchestvo()       { return otchestvo;            }
    public String  getUserName()        { return userName;             }
    public String  getPassword()        { return password;             }
    public String  getPassword2()       { return password2;            }
    public String  getSpecial3()        { return special3;             }
    public String  getAction()          { return action;               }

    public void setKodAbiturienta(Integer value)   { kodAbiturienta  = value;        }
    public void setKodGruppy(Integer value)        { kodGruppy       = value;        }
    public void setDescription(String value)       { description     = value.trim(); }
    public void setFamilija(String value)          { familija        = value.trim(); }
    public void setImja(String value)              { imja            = value.trim(); }
    public void setOtchestvo(String value)         { otchestvo       = value.trim(); }
    public void setUserName(String value)          { userName        = value.trim(); }
    public void setPassword(String value)          { password        = value.trim(); }
    public void setPassword2(String value)         { password2       = value.trim(); }
    public void setSpecial3(String value)          { special3        = value.trim(); }
    public void setAction(String value)            { action          = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        kodAbiturienta  = null;
        kodGruppy       = null;
        description     = null;
        familija        = null;
        imja            = null;
        otchestvo       = null;
        userName        = null;
        password        = null;
        password2       = null;
        special3        = null;
        action          = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

                kodAbiturienta               = bean.getKodAbiturienta();
                kodGruppy                    = bean.getKodGruppy();

            if ( bean.getDescription() != null ) {
                description = bean.getDescription();
            }
            if ( bean.getFamilija() != null ) {
                familija = bean.getFamilija();
            }
            if ( bean.getImja() != null ) {
                imja = bean.getImja();
            }
            if ( bean.getOtchestvo() != null ) {
                otchestvo = bean.getOtchestvo();
            }
            if ( bean.getUserName() != null ) {
                userName = bean.getUserName();
            }
            if ( bean.getPassword() != null ) {
                password = bean.getPassword();
            }
            if ( bean.getSpecial3() != null ) {
                password2 = bean.getPassword2();
            }
            if ( bean.getSpecial3() != null ) {
                special3 = bean.getSpecial3();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

            bean.setKodAbiturienta(kodAbiturienta);
            bean.setKodGruppy(kodGruppy);
        if ( description!=null && !description.equals("") ) {
            bean.setDescription(StringUtil.toDB(description));
        }
        if ( familija!=null && !familija.equals("") ) {
            bean.setFamilija(StringUtil.toDB(familija));
        }
        if ( imja!=null && !imja.equals("") ) {
            bean.setImja(StringUtil.toDB(imja));
        }
        if ( otchestvo!=null && !otchestvo.equals("") ) {
            bean.setOtchestvo(StringUtil.toDB(otchestvo));
        }
        if ( userName!=null && !userName.equals("") ) {
            bean.setUserName(StringUtil.toDB(userName));
        }
        if ( password!=null && !password.equals("") ) {
            bean.setPassword(StringUtil.toDB(password));
        }
        if ( password2!=null && !password2.equals("") ) {
            bean.setPassword2(StringUtil.toDB(password2));
        }
        if ( special3!=null && !special3.equals("") ) {
            bean.setSpecial3(StringUtil.toDB(special3));
        }
        return bean;
    }      
}