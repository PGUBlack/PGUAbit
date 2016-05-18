package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import org.apache.struts.upload.FormFile;
import abit.util.StringUtil;

public class FisConnectForm extends ActionForm {

    private Integer   kodFakulteta    = null;
    private String    baseAddr        = null;
    private String    action          = null;
    private String    password        = null;
    private String    user            = null;
    private String    method          = null;
    private String    timeOut         = null;
    private String    resultPath      = null;
    private String    reqXML          = null;
    private String    codeX           = null;
    private FormFile  sourceFile      = null;

    public Integer  getKodFakulteta() { return kodFakulteta;   }
    public String   getBaseAddr()     { return baseAddr;       }
    public String   getAction()       { return action;         }
    public String   getPassword()     { return password;       }
    public String   getUser()         { return user;           }
    public String   getMethod()       { return method;         }
    public String   getTimeOut()      { return timeOut;        }
    public String   getResultPath()   { return resultPath;     }
    public String   getReqXML()       { return reqXML;         }
    public String   getCodeX()        { return codeX;          }
    public FormFile getSourceFile()   { return sourceFile;     }

    public void setKodFakulteta(Integer value)   { kodFakulteta  = value;        }
    public void setBaseAddr(String value)        { baseAddr      = value.trim(); }
    public void setAction(String value)          { action        = value.trim(); }
    public void setPassword(String value)        { password      = value.trim(); }
    public void setUser(String value)            { user          = value.trim(); }
    public void setMethod(String value)          { method        = value.trim(); }
    public void setTimeOut(String value)         { timeOut       = value.trim(); }
    public void setResultPath(String value)      { resultPath    = value.trim(); }
    public void setReqXML(String value)          { reqXML        = value.trim(); }
    public void setCodeX(String value)           { codeX         = value.trim(); }
    public void setSourceFile(FormFile value)    { sourceFile    = value;        }

 public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        baseAddr          =  null;
        action            =  null;
        password          =  null;
        user              =  null;
        kodFakulteta      =  null;
        method            =  null;
        timeOut           =  null;
        resultPath        =  null;
        reqXML            =  null;
        codeX             =  null;
        sourceFile        =  null;
    }

    public void setBean(AbiturientBean bean,
                    HttpServletRequest request,
                    ActionErrors errors)
                    throws ServletException {
        if (bean!=null) {

            kodFakulteta = bean.getKodFakulteta();

            if (bean.getResultPath()!=null) {
                resultPath = bean.getResultPath();
            }
            if (bean.getUser()!=null) {
                user = bean.getUser();
            }
            if (bean.getBaseAddr()!=null) {
                baseAddr = bean.getBaseAddr();
            }
            if (bean.getReqXML()!=null) {
                reqXML = bean.getReqXML();
            }
            if (bean.getCodeX()!=null) {
                codeX = bean.getCodeX();
            }
            if (bean.getMethod()!=null) {
                method = bean.getMethod();
            }
            if (bean.getTimeOut()!=null) {
                timeOut = bean.getTimeOut();
            }
            if (bean.getPassword()!=null) {
                password = bean.getPassword();
            }
        }
    }
    public AbiturientBean getBean(HttpServletRequest request, ActionErrors errors)
                           throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodFakulteta(kodFakulteta);

        if (resultPath!=null && !resultPath.equals("")) {
            bean.setResultPath(StringUtil.toDB(resultPath));
        }
        if (user!=null && !user.equals("")) {
            bean.setUser(StringUtil.toDB(user));
        }
        if (baseAddr!=null && !baseAddr.equals("")) {
            bean.setBaseAddr(StringUtil.toDB(baseAddr));
        }
        if (reqXML!=null && !reqXML.equals("")) {
            bean.setReqXML(reqXML);
        }
        if (codeX!=null && !codeX.equals("")) {
            bean.setCodeX(codeX);
        }
        if (method!=null && !method.equals("")) {
            bean.setMethod(StringUtil.toDB(method));
        }
        if (timeOut!=null && !timeOut.equals("")) {
            bean.setTimeOut(StringUtil.toDB(timeOut));
        }
        if (password!=null && !password.equals("")) {
            bean.setPassword(StringUtil.toDB(password));
        }
        return bean;
    }      
}