package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.ErrorBean;
import abit.util.StringUtil;

public class ErrorForm extends ActionForm {

    private Integer idUser       = null;
    private Integer idDiv        = null;
    private Integer idStatus     = null;
    private String  status       = null;
    private String  abbr         = null;
    private String  name         = null;
    private String  descr        = null;
    private String  tip          = null;
    private String  comment      = null;
    private String  data         = null;
    private String  time         = null;
    private String  action       = null;
    private String  remark       = null;
    private Integer idRemark     = null;
    private String  special1     = null;


    public Integer getIdUser()    { return idUser;    }
    public Integer getIdDiv()     { return idDiv;     }
    public Integer getIdStatus()  { return idStatus;  }
    public String  getStatus()    { return status;    }
    public String  getAbbr()      { return abbr;      }
    public String  getDescr()     { return descr;     }
    public String  getName()      { return name;      }
    public String  getTip()       { return tip;       }
    public String  getComment()   { return comment;   }
    public String  getData()      { return data;      }
    public String  getTime()      { return time;      }
    public String  getAction()    { return action;    }
    public String  getRemark()    { return remark;    }
    public Integer getIdRemark()  { return idRemark;  }
    public String  getSpecial1()  { return special1;  }


    public void setIdUser(Integer value)    { idUser  = value;          }
    public void setIdDiv(Integer value)     { idDiv  = value;           }
    public void setIdStatus(Integer value)  { idStatus    = value;      }
    public void setAbbr(String value)       { abbr   = value.trim();    }
    public void setName(String value)       { name   = value.trim();    }
    public void setDescr(String value)      { descr  = value.trim();    }
    public void setStatus(String value)     { status  = value.trim();   }
    public void setTip(String value)        { tip   = value.trim();     }
    public void setComment(String value)    { comment = value.trim();   }
    public void setData(String value)       { data = value.trim();      }
    public void setTime(String value)       { time  = value.trim();     }
    public void setAction(String value)     { action  = value.trim();   }
    public void setRemark(String value)     { remark  = value.trim();   }
    public void setIdRemark(Integer value)  { idRemark  = value;        }
    public void setSpecial1(String value)   { special1= value.trim();   }


 public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        idUser    =  null;
        idDiv     =  null;
        idStatus  =  null;
        status    =  null;
        abbr      =  null;
        name      =  null;
        descr     =  null;
        tip       =  null;
        comment   =  null;
        data      =  null;
        time      =  null;
        action    =  null;
        remark    =  null;
        idRemark  =  null;
        special1  =  null;
    }

 public void setBean(ErrorBean bean,HttpServletRequest request,ActionErrors errors) throws ServletException {
        if (bean!=null) {

            idUser = bean.getIdUser();

            idDiv = bean.getIdDiv();

            idStatus = bean.getIdStatus();

            idRemark = bean.getIdRemark();

            if (bean.getRemark()!=null) {
                remark = bean.getRemark();
            }
            if(bean.getDescr()!=null) {
               descr = bean.getDescr();
            }
            if(bean.getStatus()!=null) {
               status = bean.getStatus();
            }
            if(bean.getAbbr()!=null) {
               abbr = bean.getAbbr();
            }
            if(bean.getName()!=null) {
               name = bean.getName();
            }
            if(bean.getTip()!=null) {
               tip = bean.getTip();
            }
            if(bean.getComment()!=null) {
               comment = bean.getComment();
            }
            if(bean.getData()!=null) {
               data = bean.getData();
            }
            if(bean.getTime()!=null) {
               time = bean.getTime();
            }
            if(bean.getSpecial1()!=null) {
               special1 = bean.getSpecial1();
            }
        }
    }
    public ErrorBean getBean(HttpServletRequest request, ActionErrors errors)
    throws ServletException {

        ErrorBean bean = new ErrorBean();

        bean.setIdUser(idUser);

        bean.setIdDiv(idDiv);

        bean.setIdStatus(idStatus);

        bean.setIdRemark(idRemark);

        if(status!=null && !status.equals("")) {
           bean.setStatus(StringUtil.toDB(status));
        }
        if(abbr!=null && !abbr.equals("")) {
           bean.setAbbr(StringUtil.toDB(abbr));
        }
        if(name!=null && !name.equals("")) {
           bean.setName(StringUtil.toDB(name));
        }
        if(descr!=null && !descr.equals("")) {
           bean.setDescr(StringUtil.toDB(descr));
        }
        if(tip!=null && !tip.equals("")) {
           bean.setTip(StringUtil.toDB(tip));
        }
        if(comment!=null && !comment.equals("")) {
           bean.setComment(StringUtil.toDB(comment));
        }
        if(data!=null && !data.equals("")) {
           bean.setData(StringUtil.toDB(data));
        }
        if(time!=null && !time.equals("")) {
           bean.setTime(StringUtil.toDB(time));
        }
        if (remark!=null && !remark.equals("")) {
            bean.setRemark(StringUtil.toDB(remark));
        }
        if(special1!=null && !special1.equals("")) {
           bean.setSpecial1(StringUtil.toDB(special1));
        }
        return bean;
    }      
}