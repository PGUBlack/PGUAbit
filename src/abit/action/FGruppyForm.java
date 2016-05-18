package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class FGruppyForm extends ActionForm {

    private String  nomerLichnogoDela                 = null;
    private String  familija                          = null;
    private String  imja                              = null;
    private String  otchestvo                         = null;
    private String  gruppa                            = null;

    private Integer kodAbiturienta                    = null;
    private Integer kodKursov                         = null;

    private String  abbreviatura                      = null;
    private String  action                            = null;
    private String  shifrKursov                       = null;
    private String  nomerPlatnogoDogovora             = null;
    private String  shifrLgot                 = null;
    private String  srokObuchenija                          = null;
    private String  special2                          = null;
    private String  special3                          = null;
    private String  special1                          = null;
    private Integer nomerPotoka                = null;
    private int     amount                            = 0;
    private int     maxCountAbiturients               = 0;
    private Integer kodFakulteta                      = null;
    private Integer kodSpetsialnosti                  = null;
    private Integer special11                        = null;
    private Integer special22                 = null;
    private Integer prohodnoiBallNaSpetsialnosti      = null;

    public String  getNomerLichnogoDela()                { return nomerLichnogoDela;                }
    public String  getFamilija()                         { return familija;                         }
    public String  getImja()                             { return imja;                             }
    public String  getOtchestvo()                        { return otchestvo;                        }
    public String  getGruppa()                           { return gruppa;                           }

    public Integer getKodAbiturienta()                   { return kodAbiturienta;                   }
    public Integer getKodKursov()                        { return kodKursov;                        }

    public String  getAbbreviatura()                     { return abbreviatura;                     }
    public String  getAction()                           { return action;                           }
    public String  getShifrKursov()                   { return shifrKursov;                   }
    public String  getNomerPlatnogoDogovora()            { return nomerPlatnogoDogovora;            }
    public String  getSrokObuchenija()                          { return srokObuchenija;                          }
    public String  getShifrLgot()                { return shifrLgot;                }
    public String  getSpecial2()               { return special2;               }
    public String  getSpecial3()                    { return special3;                    }
    public String  getSpecial1()                         { return special1;                         }

    public Integer getNomerPotoka()               { return nomerPotoka;               }
    public int getAmount()                                { return amount;                            }
    public int getMaxCountAbiturients()                  { return maxCountAbiturients;              }

    public Integer getKodFakulteta()                     { return kodFakulteta;                     }
    public Integer getKodSpetsialnosti()                 { return kodSpetsialnosti;                 }
    public Integer getSpecial11()                       { return special11;                       }
    public Integer getSpecial22()                { return special22;                }
    public Integer getProhodnoiBallNaSpetsialnosti()     { return prohodnoiBallNaSpetsialnosti;     }

    public void setNomerLichnogoDela(String value)                 { nomerLichnogoDela                = value.trim(); }
    public void setFamilija(String value)                          { familija                         = value.trim(); }
    public void setImja(String value)                              { imja                             = value.trim(); }
    public void setOtchestvo(String value)                         { otchestvo                        = value.trim(); }
    public void setGruppa(String value)                            { gruppa                           = value.trim(); }

    public void setKodAbiturienta(Integer value)                   { kodAbiturienta                   = value;        }
    public void setKodKursov(Integer value)                   { kodKursov                   = value;        }

    public void setAbbreviatura(String value)                      { abbreviatura                     = value.trim(); }
    public void setAction(String value)                            { action                           = value.trim(); }
    public void setShifrKursov(String value)                    { shifrKursov                   = value.trim(); }
    public void setNomerPlatnogoDogovora(String value)             { nomerPlatnogoDogovora            = value.trim(); }
    public void setSrokObuchenija(String value)                           { srokObuchenija                          = value.trim(); }
    public void setShifrLgot(String value)                 { shifrLgot                = value.trim(); }
    public void setSpecial2(String value)                { special2               = value.trim(); }
    public void setSpecial3(String value)                     { special3                    = value.trim(); }
    public void setSpecial1(String value)                          { special1                         = value.trim(); }
    public void setNomerPotoka(Integer value)               { nomerPotoka               = value;        }
    public void setAmount(int value)                                { amount                            = value;        }
    public void setMaxCountAbiturients(int value)                  { maxCountAbiturients              = value;        }

    public void setKodFakulteta(Integer value)                     { kodFakulteta                     = value;        }
    public void setKodSpetsialnosti(Integer value)                 { kodSpetsialnosti                 = value;        }
    public void setSpecial11(Integer value)                       { special11                       = value;        }
    public void setSpecial22(Integer value)                { special22                = value;        }
    public void setProhodnoiBallNaSpetsialnosti(Integer value)     { prohodnoiBallNaSpetsialnosti     = value;        }


 public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        nomerLichnogoDela                =  null;
        familija                         =  null;
        imja                             =  null;
        otchestvo                        =  null;
        gruppa                           =  null;

        kodAbiturienta                   =  null;
        kodKursov                        =  null;

        abbreviatura                     =  null;
        action                           =  null;
        shifrKursov                   =  null;
        kodFakulteta                     =  null;
        kodSpetsialnosti                 =  null;
        nomerPlatnogoDogovora            =  null;
        special11                       =  null;
        srokObuchenija                          =  null;
        special22                =  null;
        prohodnoiBallNaSpetsialnosti     =  null;
        special2               =  null;
        special3                    =  null;
        special1                         =  "auto";
        shifrLgot                =  null;
        nomerPotoka               =  null;
        amount                            =  0;
        maxCountAbiturients              =  0;
    }

 public void setBean(AbiturientBean bean,HttpServletRequest request,ActionErrors errors) throws ServletException {
        if (bean!=null) {

            kodFakulteta = bean.getKodFakulteta();

            kodSpetsialnosti = bean.getKodSpetsialnosti();

            special11 = bean.getSpecial11();

            special22 = bean.getSpecial22();

            prohodnoiBallNaSpetsialnosti = bean.getProhodnoiBallNaSpetsialnosti();

            nomerPotoka = bean.getNomerPotoka();

            amount = bean.getAmount();

            maxCountAbiturients = bean.getMaxCountAbiturients();

            kodAbiturienta = bean.getKodAbiturienta();

            kodKursov = bean.getKodKursov();


            if(bean.getNomerLichnogoDela()!=null) {
               nomerLichnogoDela = bean.getNomerLichnogoDela();
            }
            if(bean.getFamilija()!=null) {
               familija = bean.getFamilija();
            }
            if(bean.getImja()!=null) {
               imja = bean.getImja();
            }
            if(bean.getOtchestvo()!=null) {
               otchestvo = bean.getOtchestvo();
            }
            if(bean.getGruppa()!=null) {
               gruppa = bean.getGruppa();
            }


            if (bean.getShifrLgot()!=null) {
                shifrLgot = bean.getShifrLgot();
            }
            if(bean.getAbbreviatura()!=null) {
               abbreviatura = bean.getAbbreviatura();
            }
            if(bean.getShifrKursov()!=null){
               shifrKursov = bean.getShifrKursov();
            }
            if(bean.getNomerPlatnogoDogovora()!=null) {
               nomerPlatnogoDogovora = bean.getNomerPlatnogoDogovora();
            }
            if(bean.getSrokObuchenija()!=null) {
               srokObuchenija = bean.getSrokObuchenija();
            }
            if(bean.getSpecial2()!=null) {
               special2 = bean.getSpecial2();
            }
            if(bean.getSpecial3()!=null) {
               special3 = bean.getSpecial3();
            }
            if(bean.getSpecial1()!=null) {
               special1 = bean.getSpecial1();
            }
        }
    }
    public AbiturientBean getBean(HttpServletRequest request, ActionErrors errors)
    throws ServletException {

        AbiturientBean bean = new AbiturientBean();

        bean.setKodFakulteta(kodFakulteta);

        bean.setKodSpetsialnosti(kodSpetsialnosti);

        bean.setSpecial11(special11);

        bean.setSpecial22(special22);

        bean.setProhodnoiBallNaSpetsialnosti(prohodnoiBallNaSpetsialnosti);

        bean.setNomerPotoka(nomerPotoka);

        bean.setAmount(amount);

        bean.setMaxCountAbiturients(maxCountAbiturients);

        bean.setKodAbiturienta(kodAbiturienta);

        bean.setKodKursov(kodKursov);

        if(nomerLichnogoDela!=null && !nomerLichnogoDela.equals("")) {
           bean.setNomerLichnogoDela(StringUtil.toDB(nomerLichnogoDela));
        }
        if(familija!=null && !familija.equals("")) {
           bean.setFamilija(StringUtil.toDB(familija));
        }
        if(imja!=null && !imja.equals("")) {
           bean.setImja(StringUtil.toDB(imja));
        }
        if(otchestvo!=null && !otchestvo.equals("")) {
           bean.setOtchestvo(StringUtil.toDB(otchestvo));
        }
        if(gruppa!=null && !gruppa.equals("")) {
           bean.setGruppa(StringUtil.toDB(gruppa));
        }

        if(abbreviatura!=null && !abbreviatura.equals("")) {
           bean.setAbbreviatura(StringUtil.toDB(abbreviatura));
        }
        if(shifrKursov!=null && !shifrKursov.equals("")) {
           bean.setShifrKursov(StringUtil.toDB(shifrKursov));
        }
        if(nomerPlatnogoDogovora!=null && !nomerPlatnogoDogovora.equals("")) {
           bean.setNomerPlatnogoDogovora(StringUtil.toDB(nomerPlatnogoDogovora));
        }
        if(srokObuchenija!=null && !srokObuchenija.equals("")) {
           bean.setSrokObuchenija(StringUtil.toDB(srokObuchenija));
        }
        if (shifrLgot!=null && !shifrLgot.equals("")) {
            bean.setShifrLgot(StringUtil.toDB(shifrLgot));
        }
        if(special2!=null && !special2.equals("")) {
           bean.setSpecial2(StringUtil.toDB(special2));
        }
        if(special3!=null && !special3.equals("")) {
           bean.setSpecial3(StringUtil.toDB(special3));
        }
        if(special1!=null && !special1.equals("")) {
           bean.setSpecial1(StringUtil.toDB(special1));
        }
        return bean;
    }      
}