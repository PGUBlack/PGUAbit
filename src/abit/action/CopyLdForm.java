package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class CopyLdForm extends ActionForm {

    private String  special1               = null;
    private String  special2               = null;
    private String  special3               = null;
    private String  special4               = null;
    private String  special5               = null;
    private String  special6               = null;
    private String  special7               = null;
    private String  special8               = null;
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
    private Integer kodKursov              = null;
    private String  action                 = null;
    
    
    
    
    public String  getSpecial1()              { return special1;              }
    public String  getSpecial2()              { return special2;              }
    public String  getSpecial3()              { return special3;              }
    public String  getSpecial4()              { return special4;              }
    public String  getSpecial5()              { return special5;              }
    public String  getSpecial6()              { return special6;              }
    public String  getSpecial7()              { return special7;              }
    public String  getSpecial8()              { return special8;              }
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
    public Integer getKodKursov()             { return kodKursov;             }


    public void setSpecial1(String value)              { special1               = value.trim(); }
    public void setSpecial2(String value)              { special2               = value.trim(); }
    public void setSpecial3(String value)              { special3               = value.trim(); }
    public void setSpecial4(String value)              { special4               = value.trim(); }
    public void setSpecial5(String value)              { special5               = value.trim(); }
    public void setSpecial6(String value)              { special6               = value.trim(); }
    public void setSpecial7(String value)              { special7               = value.trim(); }
    public void setSpecial8(String value)              { special8               = value.trim(); }
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
    public void setKodKursov(Integer value)            {kodKursov          	= value;        }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
	special1              = null;
        special2              = null;
        special3              = null;
        special4              = null;
        special5              = null;
        special6              = null;
        special7              = null;
        special8              = null;
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
        kodKursov             = null;
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
            if ( bean.getSpecial6() != null ) {
                special6 = bean.getSpecial6();
            }
            if ( bean.getSpecial7() != null ) {
                special7 = bean.getSpecial7();
            }
            if ( bean.getSpecial8() != null ) {
                special8 = bean.getSpecial8();
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
            kodKursov=bean.getKodKursov();

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
        if ( special6!=null && !special6.equals("") ) {
            bean.setSpecial6(StringUtil.toDB(special6));
        }
        if ( special7!=null && !special7.equals("") ) {
            bean.setSpecial7(StringUtil.toDB(special7));
        }
        if ( special8!=null && !special8.equals("") ) {
            bean.setSpecial8(StringUtil.toDB(special8));
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
        bean.setKodKursov(kodKursov);
	
      return bean;
    }      
}