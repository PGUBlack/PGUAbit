package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class ApelljatsijaForm extends ActionForm {

    private String  special1               = null;
    private String  special2               = null;
    private String  special3               = null;
    private String  special4               = null;
    private String  special5               = null;
    private String  number               = null;
    private String  apelljatsija               = null;
    private String  dataJekzamena               = null;
    private Integer kodSpetsialnosti       = null;
    private String  abbreviatura           = null;
    private String  nomerLichnogoDela      = null;
    private String  familija 	           = null;
    private String  imja	           = null;
    private String  otchestvo	           = null;
    private String  shifrMedali            = null;
    private String  shifrKursov            = null;
    private String  predmet                = null;
    private Integer otsenka                = null;
    private Integer kodPredmeta              = null;
    private String  action                 = null;
    
    
    
    
    public String  getSpecial1()              { return special1;              }
    public String  getSpecial2()              { return special2;              }
    public String  getSpecial3()              { return special3;              }
    public String  getSpecial4()              { return special4;              }
    public String  getSpecial5()              { return special5;              }
    public String  getNumber()              { return number;              }
    public String  getApelljatsija()              { return apelljatsija;              }
    public String  getDataJekzamena()              { return dataJekzamena;              }
    public Integer getKodSpetsialnosti()      { return kodSpetsialnosti;      }
    public String  getAbbreviatura()          { return abbreviatura;          }
    public String  getAction()                { return action;                }
    public String  getNomerLichnogoDela()     { return nomerLichnogoDela;     }
    public String  getFamilija()              { return familija;              }
    public String  getImja()                  { return imja;                  }
    public String  getOtchestvo()             { return otchestvo;             }
    public String  getShifrMedali()           { return shifrMedali;           }
    public String  getShifrKursov()           { return shifrKursov;           }
    public String  getPredmet()               { return predmet;               }
    public Integer getOtsenka()               { return otsenka;               }
    public Integer getKodPredmeta()             { return kodPredmeta;             }


    public void setSpecial1(String value)              { special1               = value.trim(); }
    public void setSpecial2(String value)              { special2               = value.trim(); }
    public void setSpecial3(String value)              { special3               = value.trim(); }
    public void setSpecial4(String value)              { special4               = value.trim(); }
    public void setSpecial5(String value)              { special5               = value.trim(); }
    public void setNumber(String value)              { number               = value.trim(); }
    public void setApelljatsija(String value)              { apelljatsija               = value.trim(); }
    public void setDataJekzamena(String value)              { dataJekzamena               = value.trim(); }
    public void setAction(String value)                { action                 = value.trim(); }
    public void setKodSpetsialnosti(Integer value)     {kodSpetsialnosti        = value;        }
    public void setAbbreviatura(String value)          {abbreviatura            = value.trim(); }
    public void setNomerLichnogoDela(String value)     {nomerLichnogoDela       = value.trim(); }
    public void setFamilija(String value)              {familija                = value.trim(); }
    public void setImja(String value)                  {imja             	= value.trim(); }
    public void setOtchestvo(String value)             {otchestvo       	= value.trim(); }
    public void setShifrMedali(String value)           {shifrMedali      	= value.trim(); }
    public void setShifrKursov(String value)           {shifrKursov      	= value.trim(); }
    public void setPredmet(String value)               {predmet          	= value.trim(); }
    public void setOtsenka(Integer value)              {otsenka          	= value;        }
    public void setKodPredmeta(Integer value)            {kodPredmeta          	= value;        }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
	special1              = null;
        special2              = null;
        special3              = null;
        special4              = null;
        special5              = null;
        number              = null;
        apelljatsija              = null;
        dataJekzamena              = null;
	kodSpetsialnosti      = null;
	abbreviatura          = null;
        nomerLichnogoDela     = null;
        familija 	      = null;
        imja	              = null;
        otchestvo	      = null;
        shifrMedali           = null;
        shifrKursov           = null;
        predmet               = null;
        otsenka               = null;
        kodPredmeta             = null;
        action                = null;

    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

            if ( bean.getSpecial1() != null ) {
                special1 = bean.getSpecial1();
            }
            if ( bean.getSpecial2() != null ) {
                special2 = bean.getSpecial2();
            }
            if ( bean.getSpecial3() != null ) {
                special3 = bean.getSpecial3();
            }
            if ( bean.getSpecial4() != null ) {
                special4 = bean.getSpecial4();
            }
            if ( bean.getSpecial5() != null ) {
                special5 = bean.getSpecial5();
            }
            if ( bean.getNumber() != null ) {
                number = bean.getNumber();
            }
            if ( bean.getApelljatsija() != null ) {
                apelljatsija = bean.getApelljatsija();
            }
            if ( bean.getDataJekzamena() != null ) {
                dataJekzamena = bean.getDataJekzamena();
            }

            kodSpetsialnosti = bean.getKodSpetsialnosti();

    
	    if (bean.getAbbreviatura()!=null) {
                abbreviatura = bean.getAbbreviatura();
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
            if (bean.getShifrKursov()!=null) {
                shifrKursov = bean.getShifrKursov();
            }
            if (bean.getPredmet()!=null) {
                predmet = bean.getPredmet();
            }                 
            otsenka=bean.getOtsenka();
            kodPredmeta=bean.getKodPredmeta();

        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      
     

        if ( special1!=null && !special1.equals("") ) {
            bean.setSpecial1(StringUtil.toDB(special1));
        }
        if ( special2!=null && !special2.equals("") ) {
            bean.setSpecial2(StringUtil.toDB(special2));
        }
        if ( special3!=null && !special3.equals("") ) {
            bean.setSpecial3(StringUtil.toDB(special3));
        }
        if ( special4!=null && !special4.equals("") ) {
            bean.setSpecial4(StringUtil.toDB(special4));
        }
        if ( special5!=null && !special5.equals("") ) {
            bean.setSpecial5(StringUtil.toDB(special5));
        }
        if ( number!=null && !number.equals("") ) {
            bean.setNumber(StringUtil.toDB(number));
        }
        if ( apelljatsija!=null && !apelljatsija.equals("") ) {
            bean.setApelljatsija(StringUtil.toDB(apelljatsija));
        }
        if ( dataJekzamena!=null && !dataJekzamena.equals("") ) {
            bean.setDataJekzamena(StringUtil.toDB(dataJekzamena));
        }

        bean.setKodSpetsialnosti(kodSpetsialnosti);

	if (abbreviatura!=null && !abbreviatura.equals("")) {
            bean.setAbbreviatura(StringUtil.toDB(abbreviatura));
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
        if (shifrKursov!=null && !shifrKursov.equals("")) {
            bean.setShifrKursov(StringUtil.toDB(shifrKursov));
        }
        
        if (predmet!=null && !predmet.equals("")) {
            bean.setPredmet(StringUtil.toDB(predmet));
        }
       
        bean.setOtsenka(otsenka);
        bean.setKodPredmeta(kodPredmeta);
	
      return bean;
    }      
}