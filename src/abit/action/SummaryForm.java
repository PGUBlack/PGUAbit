package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import java.util.ArrayList;
import abit.util.StringUtil;

public class SummaryForm extends ActionForm {

    private String  nazvanie               = null;
    private String  abbreviaturaFakulteta  = null;
    private ArrayList notes                = null;
    private Integer kodGruppy              = null;
    private Integer kodPredmeta            = null;
    private String  gruppa                 = null;
    private String  special1               = null;
    private String  special2               = null;
    private String  special3               = null;
    private String  action                 = null;


    public String  getNazvanie()              { return nazvanie;              }
    public String  getAbbreviaturaFakulteta() { return abbreviaturaFakulteta; }
    public ArrayList getNotes()               { return notes;                 }
    public Integer getKodGruppy()             { return kodGruppy;             }
    public Integer getKodPredmeta()           { return kodPredmeta;           }
    public String  getGruppa()                { return gruppa;                }
    public String  getSpecial1()              { return special1;              }
    public String  getSpecial2()              { return special2;              }
    public String  getSpecial3()              { return special3;              }
    public String  getAction()                { return action;                }


    public void setNazvanie(String value)              { nazvanie               = value.trim(); }
    public void setAbbreviaturaFakulteta(String value) { abbreviaturaFakulteta  = value.trim(); }
    public void setNotes(ArrayList value)              { notes                  = value;        }
    public void setKodGruppy(Integer value)            { kodGruppy              = value;        }
    public void setKodPredmeta(Integer value)          { kodPredmeta            = value;        }
    public void setGruppa(String value)                { gruppa                 = value.trim(); }
    public void setSpecial1(String value)              { special1               = value.trim(); }
    public void setSpecial2(String value)              { special2               = value.trim(); }
    public void setSpecial3(String value)              { special3               = value.trim(); }
    public void setAction(String value)                { action                 = value.trim(); }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        nazvanie              = null;
        abbreviaturaFakulteta = null;
        notes                 = null;
        kodGruppy             = null;
        kodPredmeta           = null;
        special1              = null;
        special2              = null;
        special3              = null;
        gruppa                = null;
        action                = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

    if ( bean != null) {

       kodGruppy = bean.getKodGruppy();

       kodPredmeta = bean.getKodPredmeta();

       if ( bean.getNazvanie() != null ) {
	  nazvanie = bean.getNazvanie();
       }

       if ( bean.getAbbreviaturaFakulteta() != null ) {
	  abbreviaturaFakulteta = bean.getAbbreviaturaFakulteta();
       }

       if ( bean.getNotes() != null ) {
	  notes = bean.getNotes();
       }

       if ( bean.getSpecial1() != null ) {
          special1 = bean.getSpecial1();
       }

       if ( bean.getSpecial2() != null ) {
          special2 = bean.getSpecial2();
       }

       if ( bean.getSpecial3() != null ) {
          special3 = bean.getSpecial3();
       }
   }
}

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        bean.setKodGruppy(kodGruppy);

        bean.setKodPredmeta(kodPredmeta);

        if ( nazvanie!=null && !nazvanie.equals("") ) {
           bean.setNazvanie(StringUtil.toDB(nazvanie));
        }

        if ( abbreviaturaFakulteta!=null && !abbreviaturaFakulteta.equals("") ) {
           bean.setAbbreviaturaFakulteta(StringUtil.toDB(abbreviaturaFakulteta));
        }

        if ( notes!=null && !notes.equals("") ) {
           bean.setNotes(notes);
        }

        if (gruppa!=null && !gruppa.equals("")) {
           bean.setGruppa(StringUtil.toDB(gruppa));
        }

        if ( special1!=null && !special1.equals("") ) {
           bean.setSpecial1(StringUtil.toDB(special1));
        }

        if ( special2!=null && !special2.equals("") ) {
           bean.setSpecial2(StringUtil.toDB(special2));
        }

        if ( special3!=null && !special3.equals("") ) {
           bean.setSpecial3(StringUtil.toDB(special3));
        }

        return bean;
    }      
}