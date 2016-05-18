package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class NewsForm extends ActionForm {

    private Integer idNews       = null;
    private String  description  = null;
    private String  data         = null;
    private String  special2     = null;
    private String  action       = null;

    public Integer  getIdNews()      { return idNews;       }
    public String   getDescription() { return description;  }
    public String   getData()        { return data;         }
    public String   getSpecial2()    { return special2;     }
    public String   getAction()      { return action;       }

    public void setIdNews(Integer value)      { idNews       = value;        }
    public void setDescription(String value)  { description  = value.trim(); }
    public void setData(String value)         { data         = value.trim(); }
    public void setSpecial2(String value)     { special2     = value.trim(); }
    public void setAction(String value)       { action       = value.trim(); }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        idNews       = null;
        data         = null;
        special2     = null;
        description  = null;
        action       = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

             idNews = bean.getIdNews();

             if ( bean.getSpecial2() != null) {
                  special2 = bean.getSpecial2();
             }
             if ( bean.getData() != null) {
                  data = bean.getData();
             }
             if ( bean.getDescription() != null) {
                  description = bean.getDescription();
             }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setIdNews(idNews);

        if (data!=null && !data.equals("")) {
            bean.setData(StringUtil.toDB(data));
        }
        if (special2!=null && !special2.equals("")) {
            bean.setSpecial2(StringUtil.toDB(special2));
        }
        if (description!=null && !description.equals("")) {
            bean.setDescription(StringUtil.toDB(description));
        }
        return bean;
    }      
}