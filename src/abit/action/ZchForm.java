package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class ZchForm extends ActionForm {

    private Integer ball                         = null;
    private Integer godOkonchanijaSrObrazovanija = null;
    private String  godRojdenija                 = null;
    private Integer kodAbiturienta               = null;
    private Integer kodGruppy                    = null;
    private Integer kodLgot                      = null;
    private Integer kodKonGrp                    = null;
    private Integer kodMedali                    = null;
    private Integer kodPredmeta                  = null;
    private Integer kodPunkta                    = null;
    private Integer kodVuza                      = null;
    private Integer kodSpetsialnZach             = null;
    private Integer kodFakulteta                 = null;
    private Integer kodKonkursa                  = null;
    private Integer kodSpetsialnosti             = null;
    private String  dokumentyHranjatsja          = null;
    private String  shifrKursov                  = null;
    private String  gdePoluchilSrObrazovanie     = null;
    private String  inostrannyjJazyk             = null;
    private String  napravlenieOtPredprijatija   = null;
    private String  srokObuchenija               = null;
    private String  nujdaetsjaVObschejitii       = null;
    private String  pol                          = null;
    private String  prinjat                      = null;
    private String  sobesedovanie                = null;
    private String  special1                     = null;
    private String  special2                     = null;
    private String  special3                     = null;
    private String  special4                     = null;
    private String  special5                     = null;
    private String  special6                     = null;
    private String  tselevojPriem                = null;
    private String  familija                     = null;
    private String  grajdanstvo                  = null;
    private String  gruppaOplativshego           = null;
    private String  imja                         = null;
    private String  nazvanie                     = null;
    private String  nazvanieRajona               = null;
    private String  nazvanieOblasti              = null;
    private String  nomerDokumenta               = null;
    private String  nomerLichnogoDela            = null;
    private String  nomerPlatnogoDogovora        = null;
    private String  otchestvo                    = null;
    private String  polnoeNaimenovanieZavedenija = null;
    private String  action                       = null;
    private String  priznakSortirovki            = "1";
    private Integer planPriema                   = null;

    public Integer getBall()                         { return ball;                         }
    public Integer getGodOkonchanijaSrObrazovanija() { return godOkonchanijaSrObrazovanija; }
    public String  getGodRojdenija()                 { return godRojdenija;                 }
    public Integer getKodAbiturienta()               { return kodAbiturienta;               }
    public Integer getKodGruppy()                    { return kodGruppy;                    }
    public Integer getKodLgot()                      { return kodLgot;                      }
    public Integer getKodKonGrp()                    { return kodKonGrp;                    }
    public Integer getKodMedali()                    { return kodMedali;                    }
    public Integer getKodPredmeta()                  { return kodPredmeta;                  }
    public Integer getKodPunkta()                    { return kodPunkta;                    }
    public Integer getKodFakulteta()                 { return kodFakulteta;                 }
    public Integer getKodKonkursa()                  { return kodKonkursa;                  }
    public Integer getKodSpetsialnZach()             { return kodSpetsialnZach;             }
    public Integer getKodVuza()                      { return kodVuza;                      }
    public Integer getKodSpetsialnosti()             { return kodSpetsialnosti;             }
    public String  getShifrKursov()                  { return shifrKursov;                  }
    public String  getDokumentyHranjatsja()          { return dokumentyHranjatsja;          }
    public String  getGdePoluchilSrObrazovanie()     { return gdePoluchilSrObrazovanie;     }
    public String  getInostrannyjJazyk()             { return inostrannyjJazyk;             }
    public String  getNapravlenieOtPredprijatija()   { return napravlenieOtPredprijatija;   }
    public String  getSrokObuchenija()               { return srokObuchenija;               }
    public String  getNujdaetsjaVObschejitii()       { return nujdaetsjaVObschejitii;       }
    public String  getPol()                          { return pol;                          }
    public String  getPrinjat()                      { return prinjat;                      }
    public String  getSobesedovanie()                { return sobesedovanie;                }
    public String  getSpecial1()                     { return special1;                     }
    public String  getSpecial2()                     { return special2;                     }
    public String  getSpecial3()                     { return special3;                     }
    public String  getSpecial4()                     { return special4;                     }
    public String  getSpecial5()                     { return special5;                     }
    public String  getSpecial6()                     { return special6;                     }
    public String  getTselevojPriem()                { return tselevojPriem;                }
    public String  getFamilija()                     { return familija;                     }
    public String  getGrajdanstvo()                  { return grajdanstvo;                  }
    public String  getGruppaOplativshego()           { return gruppaOplativshego;           }
    public String  getImja()                         { return imja;                         }
    public String  getNomerDokumenta()               { return nomerDokumenta;               }
    public String  getNomerLichnogoDela()            { return nomerLichnogoDela;            }
    public String  getNomerPlatnogoDogovora()        { return nomerPlatnogoDogovora;        }
    public String  getNazvanie()                     { return nazvanie;                     }
    public String  getNazvanieOblasti()              { return nazvanieOblasti;              }
    public String  getNazvanieRajona()               { return nazvanieRajona;               }
    public String  getOtchestvo()                    { return otchestvo;                    }
    public String  getPolnoeNaimenovanieZavedenija() { return polnoeNaimenovanieZavedenija; }
    public String  getPriznakSortirovki()            { return priznakSortirovki;            }
    public Integer getPlanPriema()                   { return planPriema;                   }
    public String  getAction()                       { return action;                       }

    public void setBall(Integer value)                         { ball                         = value;        }
    public void setGodOkonchanijaSrObrazovanija(Integer value) { godOkonchanijaSrObrazovanija = value;        }
    public void setGodRojdenija(String value)                  { godRojdenija                 = value.trim(); }
    public void setKodAbiturienta(Integer value)               { kodAbiturienta               = value;        }
    public void setKodGruppy(Integer value)                    { kodGruppy                    = value;        }
    public void setKodLgot(Integer value)                      { kodLgot                      = value;        }
    public void setKodKonGrp(Integer value)                    { kodKonGrp                    = value;        }
    public void setKodMedali(Integer value)                    { kodMedali                    = value;        }
    public void setKodPredmeta(Integer value)                  { kodPredmeta                  = value;        }
    public void setKodPunkta(Integer value)                    { kodPunkta                    = value;        }
    public void setKodVuza(Integer value)                      { kodVuza                      = value;        }
    public void setKodSpetsialnZach(Integer value)             { kodSpetsialnZach             = value;        }
    public void setKodFakulteta(Integer value)                 { kodFakulteta                 = value;        }
    public void setKodKonkursa(Integer value)                  { kodKonkursa                  = value;        }
    public void setKodSpetsialnosti(Integer value)             { kodSpetsialnosti             = value;        }
    public void setShifrKursov(String value)                   { shifrKursov                  = value.trim(); }
    public void setGdePoluchilSrObrazovanie(String value)      { gdePoluchilSrObrazovanie     = value.trim(); }
    public void setInostrannyjJazyk(String value)              { inostrannyjJazyk             = value.trim(); }
    public void setNapravlenieOtPredprijatija(String value)    { napravlenieOtPredprijatija   = value.trim(); }
    public void setSrokObuchenija(String value)                { srokObuchenija               = value.trim(); }
    public void setNujdaetsjaVObschejitii(String value)        { nujdaetsjaVObschejitii       = value.trim(); }
    public void setPol(String value)                           { pol                          = value.trim(); }
    public void setPrinjat(String value)                       { prinjat                      = value.trim(); }
    public void setSobesedovanie(String value)                 { sobesedovanie                = value.trim(); }
    public void setSpecial1(String value)                      { special1                     = value.trim(); }
    public void setSpecial2(String value)                      { special2                     = value.trim(); }
    public void setDokumentyHranjatsja(String value)           { dokumentyHranjatsja          = value.trim(); }
    public void setSpecial3(String value)                      { special3                     = value.trim(); }
    public void setSpecial4(String value)                      { special4                     = value.trim(); }
    public void setSpecial5(String value)                      { special5                     = value.trim(); }
    public void setSpecial6(String value)                      { special6                     = value.trim(); }
    public void setTselevojPriem(String value)                 { tselevojPriem                = value.trim(); }
    public void setFamilija(String value)                      { familija                     = value.trim(); }
    public void setGrajdanstvo(String value)                   { grajdanstvo                  = value.trim(); }
    public void setGruppaOplativshego(String value)            { gruppaOplativshego           = value.trim(); }
    public void setImja(String value)                          { imja                         = value.trim(); }
    public void setNomerDokumenta(String value)                { nomerDokumenta               = value.trim(); }
    public void setNomerLichnogoDela(String value)             { nomerLichnogoDela            = value.trim(); }
    public void setNomerPlatnogoDogovora(String value)         { nomerPlatnogoDogovora        = value.trim(); }
    public void setNazvanie(String value)                      { nazvanie                     = value.trim(); }
    public void setNazvanieOblasti(String value)               { nazvanieOblasti              = value.trim(); }
    public void setNazvanieRajona(String value)                { nazvanieRajona               = value.trim(); }
    public void setOtchestvo(String value)                     { otchestvo                    = value.trim(); }
    public void setPolnoeNaimenovanieZavedenija(String value)  { polnoeNaimenovanieZavedenija = value.trim(); }
    public void setPlanPriema(Integer value)                   { planPriema                   = value;        }
    public void setPriznakSortirovki(String value)             { priznakSortirovki            = value.trim(); }
    public void setAction(String value)                        { action                       = value.trim(); }

    public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        ball                         = null;
        godOkonchanijaSrObrazovanija = null;
        godRojdenija                 = null;
        kodAbiturienta               = null;
        kodGruppy                    = null;
        kodLgot                      = null;
        kodKonGrp                    = null;
        kodMedali                    = null;
        kodPredmeta                  = null;
        kodPunkta                    = null;
        kodVuza                      = null;
        kodSpetsialnZach             = null;
        kodFakulteta                 = null;
        kodKonkursa                  = null;
        kodSpetsialnosti             = null;
        shifrKursov                  = null;
        gdePoluchilSrObrazovanie     = null;
        inostrannyjJazyk             = null;
        napravlenieOtPredprijatija   = null;
        srokObuchenija               = null;
        nujdaetsjaVObschejitii       = null;
        pol                          = null;
        prinjat                      = null;
        sobesedovanie                = null;
        special1                     = null;
        special2                     = null;
        special3                     = null;
        special4                     = null;
        special5                     = null;
        special6                     = null;
        tselevojPriem                = null;
        familija                     = null;
        grajdanstvo                  = null;
        gruppaOplativshego           = null;
        imja                         = null;
        nomerDokumenta               = null;
        nomerLichnogoDela            = null;
        nomerPlatnogoDogovora        = null;
        nazvanie                     = null;
        nazvanieOblasti              = null;
        nazvanieRajona               = null;
        dokumentyHranjatsja          = null;
        otchestvo                    = null;
        polnoeNaimenovanieZavedenija = null;
        planPriema                   = null;
        priznakSortirovki            = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors) throws ServletException {

        if ( bean != null ) {

                ball                         = bean.getBall();
                godOkonchanijaSrObrazovanija = bean.getGodOkonchanijaSrObrazovanija();
                kodAbiturienta               = bean.getKodAbiturienta();
                kodGruppy                    = bean.getKodGruppy();
                kodLgot                      = bean.getKodLgot();
                kodKonGrp                    = bean.getKodKonGrp();
                kodMedali                    = bean.getKodMedali();
                kodPunkta                    = bean.getKodPunkta();
                kodVuza                      = bean.getKodVuza();
                kodSpetsialnZach             = bean.getKodSpetsialnZach();
                kodFakulteta                 = bean.getKodFakulteta();
                kodKonkursa                  = bean.getKodKonkursa();
                kodSpetsialnosti             = bean.getKodSpetsialnosti();
                kodPredmeta                  = bean.getKodPredmeta();

            if ( bean.getGodRojdenija() != null ) {
                godRojdenija = bean.getGodRojdenija();
            }
            if ( bean.getShifrKursov() != null ) {
                shifrKursov = bean.getShifrKursov();
            }
            if ( bean.getDokumentyHranjatsja() != null ) {
                dokumentyHranjatsja = bean.getDokumentyHranjatsja();
            }
            if ( bean.getGdePoluchilSrObrazovanie() != null ) {
                gdePoluchilSrObrazovanie = bean.getGdePoluchilSrObrazovanie();
            }
            if ( bean.getInostrannyjJazyk() != null ) {
                inostrannyjJazyk = bean.getInostrannyjJazyk();
            }
            if ( bean.getNapravlenieOtPredprijatija() != null ) {
                napravlenieOtPredprijatija = bean.getNapravlenieOtPredprijatija();
            }
            if ( bean.getSrokObuchenija() != null ) {
                srokObuchenija = bean.getSrokObuchenija();
            }
            if ( bean.getNujdaetsjaVObschejitii() != null ) {
                nujdaetsjaVObschejitii = bean.getNujdaetsjaVObschejitii();
            }
            if ( bean.getPol() != null ) {
                pol = bean.getPol();
            }
            if ( bean.getPrinjat() != null ) {
                prinjat = bean.getPrinjat();
            }
            if ( bean.getNazvanie() != null ) {
                nazvanie = bean.getNazvanie();
            }
            if ( bean.getNazvanieOblasti() != null ) {
                nazvanieOblasti = bean.getNazvanieOblasti();
            }
            if ( bean.getNazvanieRajona() != null ) {
                nazvanieRajona = bean.getNazvanieRajona();
            }
            if ( bean.getSobesedovanie() != null ) {
                sobesedovanie = bean.getSobesedovanie();
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
            if ( bean.getTselevojPriem() != null ) {
                tselevojPriem = bean.getTselevojPriem();
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
            if ( bean.getFamilija() != null ) {
                familija = bean.getFamilija();
            }
            if ( bean.getGrajdanstvo() != null ) {
                grajdanstvo = bean.getGrajdanstvo();
            }
            if ( bean.getGruppaOplativshego() != null ) {
                gruppaOplativshego = bean.getGruppaOplativshego();
            }
            if ( bean.getImja() != null ) {
                imja = bean.getImja();
            }
            if ( bean.getNomerDokumenta() != null ) {
                nomerDokumenta = bean.getNomerDokumenta();
            }
            if ( bean.getNomerLichnogoDela() != null ) {
                nomerLichnogoDela = bean.getNomerLichnogoDela();
            }
            if ( bean.getNomerPlatnogoDogovora() != null ) {
                nomerPlatnogoDogovora = bean.getNomerPlatnogoDogovora();
            }
            if ( bean.getOtchestvo() != null ) {
                otchestvo = bean.getOtchestvo();
            }
            if ( bean.getPolnoeNaimenovanieZavedenija() != null ) {
                polnoeNaimenovanieZavedenija = bean.getPolnoeNaimenovanieZavedenija();
            }
            if ( bean.getPriznakSortirovki() != null ) {
                priznakSortirovki = bean.getPriznakSortirovki();
            }

            planPriema = bean.getPlanPriema();

        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();      

            bean.setBall(ball);
            bean.setGodOkonchanijaSrObrazovanija(godOkonchanijaSrObrazovanija);
            bean.setKodAbiturienta(kodAbiturienta);
            bean.setKodGruppy(kodGruppy);
            bean.setKodLgot(kodLgot);
            bean.setKodKonGrp(kodKonGrp);
            bean.setKodMedali(kodMedali);
            bean.setKodPredmeta(kodPredmeta);
            bean.setKodPunkta(kodPunkta);
            bean.setKodVuza(kodVuza);
            bean.setKodSpetsialnZach(kodSpetsialnZach);
            bean.setKodFakulteta(kodFakulteta);
            bean.setKodKonkursa(kodKonkursa);
            bean.setKodSpetsialnosti(kodSpetsialnosti);

        if ( godRojdenija!=null && !godRojdenija.equals("") ) {
            bean.setGodRojdenija(StringUtil.toDB(godRojdenija));
        }
        if ( shifrKursov!=null && !shifrKursov.equals("") ) {
            bean.setShifrKursov(StringUtil.toDB(shifrKursov));
        }
        if ( dokumentyHranjatsja!=null && !dokumentyHranjatsja.equals("") ) {
            bean.setDokumentyHranjatsja(StringUtil.toDB(dokumentyHranjatsja));
        }
        if ( gdePoluchilSrObrazovanie!=null && !gdePoluchilSrObrazovanie.equals("") ) {
            bean.setGdePoluchilSrObrazovanie(StringUtil.toDB(gdePoluchilSrObrazovanie));
        }
        if ( inostrannyjJazyk!=null && !inostrannyjJazyk.equals("") ) {
            bean.setInostrannyjJazyk(StringUtil.toDB(inostrannyjJazyk));
        }
        if ( napravlenieOtPredprijatija!=null && !napravlenieOtPredprijatija.equals("") ) {
            bean.setNapravlenieOtPredprijatija(StringUtil.toDB(napravlenieOtPredprijatija));
        }
        if ( srokObuchenija!=null && !srokObuchenija.equals("") ) {
            bean.setSrokObuchenija(StringUtil.toDB(srokObuchenija));
        }
        if ( nujdaetsjaVObschejitii!=null && !nujdaetsjaVObschejitii.equals("") ) {
            bean.setNujdaetsjaVObschejitii(StringUtil.toDB(nujdaetsjaVObschejitii));
        }
        if ( pol!=null && !pol.equals("") ) {
            bean.setPol(StringUtil.toDB(pol));
        }
        if ( prinjat!=null && !prinjat.equals("") ) {
            bean.setPrinjat(StringUtil.toDB(prinjat));
        }
        if ( sobesedovanie!=null && !sobesedovanie.equals("") ) {
            bean.setSobesedovanie(StringUtil.toDB(sobesedovanie));
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
        if ( special4!=null && !special4.equals("") ) {
            bean.setSpecial4(StringUtil.toDB(special4));
        }
        if ( special5!=null && !special5.equals("") ) {
            bean.setSpecial5(StringUtil.toDB(special5));
        }
        if ( special6!=null && !special6.equals("") ) {
            bean.setSpecial6(StringUtil.toDB(special6));
        }
        if ( tselevojPriem!=null && !tselevojPriem.equals("") ) {
            bean.setTselevojPriem(StringUtil.toDB(tselevojPriem));
        }
        if ( familija!=null && !familija.equals("") ) {
            bean.setFamilija(StringUtil.toDB(familija));
        }
        if ( grajdanstvo!=null && !grajdanstvo.equals("") ) {
            bean.setGrajdanstvo(StringUtil.toDB(grajdanstvo));
        }
        if ( gruppaOplativshego!=null && !gruppaOplativshego.equals("") ) {
            bean.setGruppaOplativshego(StringUtil.toDB(gruppaOplativshego));
        }
        if ( imja!=null && !imja.equals("") ) {
            bean.setImja(StringUtil.toDB(imja));
        }
        if ( nomerDokumenta!=null && !nomerDokumenta.equals("") ) {
            bean.setNomerDokumenta(StringUtil.toDB(nomerDokumenta));
        }
        if ( nomerLichnogoDela!=null && !nomerLichnogoDela.equals("") ) {
            bean.setNomerLichnogoDela(StringUtil.toDB(nomerLichnogoDela));
        }
        if ( nomerPlatnogoDogovora!=null && !nomerPlatnogoDogovora.equals("") ) {
            bean.setNomerPlatnogoDogovora(StringUtil.toDB(nomerPlatnogoDogovora));
        }
        if ( nazvanie!=null && !nazvanie.equals("") ) {
            bean.setNazvanie(StringUtil.toDB(nazvanie));
        }
        if ( nazvanieOblasti!=null && !nazvanieOblasti.equals("") ) {
            bean.setNazvanieOblasti(StringUtil.toDB(nazvanieOblasti));
        }
        if ( nazvanieRajona!=null && !nazvanieRajona.equals("") ) {
            bean.setNazvanieRajona(StringUtil.toDB(nazvanieRajona));
        }
        if ( otchestvo!=null && !otchestvo.equals("") ) {
            bean.setOtchestvo(StringUtil.toDB(otchestvo));
        }
        if ( polnoeNaimenovanieZavedenija!=null && !polnoeNaimenovanieZavedenija.equals("") ) {
            bean.setPolnoeNaimenovanieZavedenija(StringUtil.toDB(polnoeNaimenovanieZavedenija));
        }

        bean.setPlanPriema(planPriema);

        if ( priznakSortirovki!=null && !priznakSortirovki.equals("") ) {
            bean.setPriznakSortirovki(StringUtil.toDB(priznakSortirovki));
        }
        return bean;
    }      
}