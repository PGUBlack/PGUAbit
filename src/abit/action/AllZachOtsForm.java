package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class AllZachOtsForm extends ActionForm {

    private String  special1            = null;
    private String  summa               = null;
    private String  abbreviatura        = null;
    private String  gruppa              = null;
    private String  nomerLichnogoDela   = null;
    private String  familija 	          = null;
    private String  imja	          = null;
    private String  otchestvo	          = null;
    private String  shifrMedali         = null;
    private String  shifrFakulteta      = null;
    private String  predmet             = null;
    private Integer otsenka             = null;
    private String  action              = null;
    
    
    
    
    public String  getSpecial1()              { return special1;              }
    public String  getSumma()                 { return summa;                 }
    public String  getAbbreviatura()          { return abbreviatura;          }
    public String  getGruppa()                { return gruppa;                }
    public String  getAction()                { return action;                }
    public String  getNomerLichnogoDela()     { return nomerLichnogoDela;     }
    public String  getFamilija()              { return familija;              }
    public String  getImja()                  { return imja;                  }
    public String  getOtchestvo()             { return otchestvo;             }
    public String  getShifrMedali()           { return shifrMedali;           }
    public String  getShifrFakulteta()        { return shifrFakulteta;        }
    public String  getPredmet()               { return predmet;               }
    public Integer  getOtsenka()              { return otsenka;               }


    public void setSpecial1(String value)              { special1               = value.trim(); }
    public void setSumma(String value)                 { summa                  = value.trim(); }
    public void setAction(String value)                { action                 = value.trim(); }
    public void setAbbreviatura(String value)          {abbreviatura            = value.trim(); }
    public void setGruppa(String value)                {gruppa                  = value.trim(); }
    public void setNomerLichnogoDela(String value)     {nomerLichnogoDela       = value.trim(); }
    public void setFamilija(String value)              {familija                = value.trim(); }
    public void setImja(String value)                  {imja                    = value.trim(); }
    public void setOtchestvo(String value)             {otchestvo               = value.trim(); }
    public void setShifrMedali(String value)           {shifrMedali             = value.trim(); }
    public void setShifrFakulteta(String value)        {shifrFakulteta          = value.trim(); }
    public void setPredmet(String value)               {predmet                 = value.trim(); }
    public void setOtsenka(Integer value)              {otsenka                 = value;        }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
	special1              = null;
      summa                 = null;
	abbreviatura          = null;
	gruppa                = null;
      nomerLichnogoDela     = null;
      familija 	          = null;
      imja	                = null;
      otchestvo	          = null;
      shifrMedali           = null;
      shifrFakulteta        = null;
      predmet               = null;
      otsenka               = null;
      action                = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {


        if ( bean != null) {

            if ( bean.getSpecial1() != null ) {
                special1 = bean.getSpecial1();
            }
            if ( bean.getSumma() != null ) {
                summa = bean.getSumma();
            } 
            if (bean.getAbbreviatura()!=null) {
                abbreviatura = bean.getAbbreviatura();
            }
            if (bean.getGruppa()!=null) {
                gruppa = bean.getGruppa();
            }
            if (bean.getNomerLichnogoDela()!=null) {
                nomerLichnogoDela = bean.getNomerLichnogoDela();
            }
            if (bean.getFamilija()!=null) {
                familija = bean.getFamilija();
            }
            if (bean.getImja()!=null) {
               imja = bean.getImja();
            }
            if (bean.getOtchestvo()!=null) {
                otchestvo = bean.getOtchestvo();
            }
            if (bean.getShifrMedali()!=null) {
                shifrMedali = bean.getShifrMedali();
            }
            if (bean.getShifrFakulteta()!=null) {
                shifrFakulteta = bean.getShifrFakulteta();
            }
            if (bean.getPredmet()!=null) {
                predmet = bean.getPredmet();
            }                 
            otsenka=bean.getOtsenka();

        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

        if ( special1!=null && !special1.equals("") ) {
            bean.setSpecial1(StringUtil.toDB(special1));
        }
        if ( summa!=null && !summa.equals("") ) {
            bean.setSumma(StringUtil.toDB(summa));
        }
        if (abbreviatura!=null && !abbreviatura.equals("")) {
            bean.setAbbreviatura(StringUtil.toDB(abbreviatura));
        }
        if (gruppa!=null && !gruppa.equals("")) {
            bean.setGruppa(StringUtil.toDB(gruppa));
        }
        if (nomerLichnogoDela!=null && !nomerLichnogoDela.equals("")) {
            bean.setNomerLichnogoDela(StringUtil.toDB(nomerLichnogoDela));
        }
        if (familija!=null && !familija.equals("")) {
            bean.setFamilija(StringUtil.toDB(familija));
        }
        if (otchestvo!=null && !otchestvo.equals("")) {
            bean.setOtchestvo(StringUtil.toDB(otchestvo));
        }
        if (shifrMedali!=null && !shifrMedali.equals("")) {
            bean.setShifrMedali(StringUtil.toDB(shifrMedali));
        }
        if (shifrFakulteta!=null && !shifrFakulteta.equals("")) {
            bean.setShifrFakulteta(StringUtil.toDB(shifrFakulteta));
        }
        if (predmet!=null && !predmet.equals("")) {
            bean.setPredmet(StringUtil.toDB(predmet));
        }
        
        bean.setOtsenka(otsenka);

        return bean;
    }      
}