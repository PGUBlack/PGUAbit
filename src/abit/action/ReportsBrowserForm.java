package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.ReportsBrowserBean;
import abit.util.StringUtil;

public class ReportsBrowserForm extends ActionForm {

    private Integer number    = null;
    private String  special1  = null;
    private String  special2  = null;
    private String  name  = null;
    private String  fileName  = null;
    private String  sign  = null;
    private String  date  = null;
    private String  time  = null;
    private String  id  = null;
    private String  viewToAll  = null;
    private String  author  = null;
    private String  action    = null;

    public Integer getNumber()    { return number;   }
    public String  getSpecial1()  { return special1; }
    public String  getSpecial2()  { return special2; }
    public String  getName()  { return name; }
    public String  getFileName()  { return fileName; }
    public String  getSign()  { return sign; }
    public String  getDate()  { return date; }
    public String  getTime()  { return time; }
    public String  getId()  { return id; }
    public String  getAuthor()  { return author; }
    public String  getViewToAll()  { return viewToAll; }
    public String  getAction()    { return action;   }

    public void setNumber(Integer value)   { number   = value;        }
    public void setSpecial1(String value)  { special1 = value.trim(); }
    public void setSpecial2(String value)  { special2 = value.trim(); }
    public void setName(String value)  { name = value.trim(); }
    public void setFileName(String value)  { fileName = value.trim(); }
    public void setsign(String value)  { sign = value.trim(); }
    public void setDate(String value)  { date = value.trim(); }
    public void setTime(String value)  { time = value.trim(); }
    public void setId(String value)  { id = value.trim(); }
    public void setAuthor(String value)  { author = value.trim(); }
    public void setViewToAll(String value)  { viewToAll = value.trim(); }
    public void setAction(String value)    { action   = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        number    = null;
        special1  = null;
        special2  = null;
        name  = null;
        fileName  = null;
        sign  = null;
        date  = null;
        time  = null;
        author  = null;
        viewToAll  = null;
        id  = null;
        action    = null;
    }

    public void setBean( ReportsBrowserBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

            number = bean.getNumber();

            if ( bean.getSpecial1() != null ) {
                special1 = bean.getSpecial1();
            }
            if ( bean.getSpecial2() != null ) {
                special2 = bean.getSpecial2();
            }
            if ( bean.getName() != null ) {
                name = bean.getName();
            }
            if ( bean.getFileName() != null ) {
                fileName = bean.getFileName();
            }
            if ( bean.getSign() != null ) {
                sign = bean.getSign();
            }
            if ( bean.getDate() != null ) {
                date = bean.getDate();
            }
            if ( bean.getTime() != null ) {
                time = bean.getTime();
            }
            if ( bean.getAuthor() != null ) {
                author = bean.getAuthor();
            }
            if ( bean.getViewToAll() != null ) {
                viewToAll = bean.getViewToAll();
            }
            if ( bean.getId() != null ) {
                id = bean.getId();
            }
        }
    }

    public ReportsBrowserBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        ReportsBrowserBean bean = new ReportsBrowserBean();      

        bean.setNumber(number);

        if ( special1!=null && !special1.equals("") ) {
            bean.setSpecial1(StringUtil.toDB(special1));
        }
        if ( special2!=null && !special2.equals("") ) {
            bean.setSpecial2(StringUtil.toDB(special2));
        }
        if ( name!=null && !name.equals("") ) {
            bean.setName(StringUtil.toDB(name));
        }
        if ( fileName!=null && !fileName.equals("") ) {
            bean.setFileName(StringUtil.toDB(fileName));
        }
        if ( sign!=null && !sign.equals("") ) {
            bean.setSign(StringUtil.toDB(sign));
        }
        if ( date!=null && !date.equals("") ) {
            bean.setDate(StringUtil.toDB(date));
        }
        if ( time!=null && !time.equals("") ) {
            bean.setTime(StringUtil.toDB(time));
        }
        if ( author!=null && !author.equals("") ) {
            bean.setAuthor(StringUtil.toDB(author));
        }
        if ( viewToAll!=null && !viewToAll.equals("") ) {
            bean.setViewToAll(StringUtil.toDB(viewToAll));
        }
        if ( id!=null && !id.equals("") ) {
            bean.setId(StringUtil.toDB(id));
        }
        return bean;
    }      
}