package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import abit.bean.AbiturientBean;
import abit.util.StringUtil;

public class SpetsialnostiForm extends ActionForm {

    private String  abbreviatura                      = null;
    private String  action                            = null;
    private String  jekzamenZachet                    = null;
    private String  nazvanieSpetsialnosti             = null;
    private String  priznakSortirovki                 = null;
    private String  nazvanie                          = null;
    private String  predmet                           = null;
    private String  shifrSpetsialnosti                = null;
    private String  shifrSpetsialnostiOKSO            = null;
    private String  sobesedovanie                     = null;
    private String  special1                          = null;
    private String  tip_Spec                          = null;
    private Integer stolbetsSortirovki                = new Integer(1);
    private Integer kodFakulteta                      = null;
    private Integer kodSpetsialnosti                  = null;
    private Integer kodPredmeta                       = null;
    private Integer kodKonGrp                         = null;
    private Integer planPriema                        = null;
    private Integer planPriemaLg                      = null;
    private Integer planPriemaDog                     = null;
    private Integer planPriemaT1                      = null;
    private Integer planPriemaT2                      = null;
    private Integer planPriemaT3                      = null;
    private Integer planPriemaT4                      = null;
    private Integer planPriemaT5                      = null;
    private Integer poluProhodnoiBall                 = null;
    private Integer prohodnoiBallNaSpetsialnosti      = null;
    private Integer krimobshee				          = null;
    private Integer krimok							  = null;
    private Integer krimcp							  = null;
    private Integer planPriemaIG					  = null;
    private Integer ppDogLgot						  = null;
    private Integer ppKrimDog						  = null;  
    private Integer ppKrimDogLgot					  = null;  
    private Integer fisId                         = null;
    private String  formaOb                          = null;
    private Integer idNews = null;
    
    

    public String  getAbbreviatura()                     { return abbreviatura;                     }
    public String  getAction()                           { return action;                           }
    public String  getJekzamenZachet()                   { return jekzamenZachet;                   }
    public String  getNazvanieSpetsialnosti()            { return nazvanieSpetsialnosti;            }
    public String  getNazvanie()                         { return nazvanie;                         }
    public String  getPredmet()                          { return predmet;                          }
    public String  getPriznakSortirovki()                { return priznakSortirovki;                }
    public String  getShifrSpetsialnosti()               { return shifrSpetsialnosti;               }
    public String  getShifrSpetsialnostiOKSO()           { return shifrSpetsialnostiOKSO;           }
    public String  getSobesedovanie()                    { return sobesedovanie;                    }
    public String  getSpecial1()                         { return special1;                         }
    public String  getTip_Spec()                         { return tip_Spec;                         }
    public Integer getStolbetsSortirovki()               { return stolbetsSortirovki;               }
    public Integer getKodFakulteta()                     { return kodFakulteta;                     }
    public Integer getKodSpetsialnosti()                 { return kodSpetsialnosti;                 }
    public Integer getKodPredmeta()                      { return kodPredmeta;                      }
    public Integer getKodKonGrp()                        { return kodKonGrp;                        }
    public Integer getPlanPriemaLg()                     { return planPriemaLg;                     }
    public Integer getPlanPriema()                       { return planPriema;                       }
    public Integer getPlanPriemaDog()                    { return planPriemaDog;                    }
    public Integer getPlanPriemaT1()                     { return planPriemaT1;                     }
    public Integer getPlanPriemaT2()                     { return planPriemaT2;                     }
    public Integer getPlanPriemaT3()                     { return planPriemaT3;                     }
    public Integer getPlanPriemaT4()                     { return planPriemaT4;                     }
    public Integer getPlanPriemaT5()                     { return planPriemaT5;                     }
    public Integer getPoluProhodnoiBall()                { return poluProhodnoiBall;                }
    public Integer getProhodnoiBallNaSpetsialnosti()     { return prohodnoiBallNaSpetsialnosti;     }
    public Integer getKrimobshee()						 { return krimobshee;						}
    public Integer getKrimok()							 { return krimok;							}
    public Integer getKrimcp()							 { return krimcp;							}
    public Integer getPlanPriemaIG()						 { return planPriemaIG;						}
    public Integer getPpDogLgot()						 { return ppDogLgot;						}
    public Integer getPpKrimDog()						 { return ppKrimDog;						}    
    public Integer getPpKrimDogLgot()						 { return ppKrimDogLgot;						}   
    public Integer getFisId()     { return fisId;     }
    public String getFormaOb()     { return formaOb;     }
    public Integer getIdNews()     { return idNews;     }


    public void setAbbreviatura(String value)                      { abbreviatura                     = value.trim(); }
    public void setAction(String value)                            { action                           = value.trim(); }
    public void setJekzamenZachet(String value)                    { jekzamenZachet                   = value.trim(); }
    public void setNazvanieSpetsialnosti(String value)             { nazvanieSpetsialnosti            = value.trim(); }
    public void setNazvanie(String value)                          { nazvanie                         = value.trim(); }
    public void setPredmet(String value)                           { predmet                          = value.trim(); }
    public void setPriznakSortirovki(String value)                 { priznakSortirovki                = value.trim(); }
    public void setShifrSpetsialnosti(String value)                { shifrSpetsialnosti               = value.trim(); }
    public void setShifrSpetsialnostiOKSO(String value)            { shifrSpetsialnostiOKSO           = value.trim(); }
    public void setSobesedovanie(String value)                     { sobesedovanie                    = value.trim(); }
    public void setSpecial1(String value)                          { special1                         = value.trim(); }
    public void setTip_Spec(String value)                          { tip_Spec                         = value;        }
    public void setStolbetsSortirovki(Integer value)               { stolbetsSortirovki               = value;        }
    public void setKodFakulteta(Integer value)                     { kodFakulteta                     = value;        }
    public void setKodSpetsialnosti(Integer value)                 { kodSpetsialnosti                 = value;        }
    public void setKodPredmeta(Integer value)                      { kodPredmeta                      = value;        }
    public void setKodKonGrp(Integer value)                        { kodKonGrp                        = value;        }
    public void setPlanPriema(Integer value)                       { planPriema                       = value;        }
    public void setPlanPriemaLg(Integer value)                     { planPriemaLg                     = value;        }
    public void setPlanPriemaDog(Integer value)                    { planPriemaDog                    = value;        }
    public void setPlanPriemaT1(Integer value)                     { planPriemaT1                     = value;        }
    public void setPlanPriemaT2(Integer value)                     { planPriemaT2                     = value;        }
    public void setPlanPriemaT3(Integer value)                     { planPriemaT3                     = value;        }
    public void setPlanPriemaT4(Integer value)                     { planPriemaT4                     = value;        }
    public void setPlanPriemaT5(Integer value)                     { planPriemaT5                     = value;        }
    public void setPoluProhodnoiBall(Integer value)                { poluProhodnoiBall                = value;        }
    public void setProhodnoiBallNaSpetsialnosti(Integer value)     { prohodnoiBallNaSpetsialnosti     = value;        }
    public void setFisId(Integer value)             			   { fisId          				  = value;        }
    public void setFormaOb(String value)             			   { formaOb          				  = value;        }
    public void setIdNews(Integer value)             			   { idNews          				  = value;        }
    public void setKrimobshee(Integer value)					   { krimobshee						  = value;		  }
    public void setKrimok(Integer value)						   { krimok							  = value;		  }
    public void setKrimcp(Integer value)						   { krimcp							  = value;		  }
    public void setPlanPriemaIG(Integer value)					   { planPriemaIG					  = value;		  }
    public void setPpDogLgot(Integer value)					   { ppDogLgot					  = value;		  }    
    public void setPpKrimDog(Integer value)					   { ppKrimDog					  = value;		  }    
    public void setPpKrimDogLgot(Integer value)					   { ppKrimDogLgot					  = value;		  }   
    
 public void reset(ActionMapping mapping, HttpServletRequest request )
    {
        abbreviatura                     =  null;
        action                           =  null;
        jekzamenZachet                   =  null;
        kodFakulteta                     =  null;
        kodSpetsialnosti                 =  null;
        kodPredmeta                      =  null;
        kodKonGrp                        =  null;
        nazvanieSpetsialnosti            =  null;
        planPriema                       =  null;
        planPriemaLg                     =  null;
        planPriemaDog                    =  null;
        planPriemaT1                     =  null;
        planPriemaT2                     =  null;
        planPriemaT3                     =  null;
        planPriemaT4                     =  null;
        planPriemaT5                     =  null;
        nazvanie                         =  null;
        predmet                          =  null;
        poluProhodnoiBall                =  null;
        prohodnoiBallNaSpetsialnosti     =  null;
        shifrSpetsialnosti               =  null;
        shifrSpetsialnostiOKSO           =  null;
        sobesedovanie                    =  null;
        special1                         =  null;
        tip_Spec                         =  null;
        priznakSortirovki                =  null;
        stolbetsSortirovki               =  new Integer(1);
        fisId							 =  null;
        formaOb							 =  null;
        krimobshee						 =  null;
        krimok							 =  null;
        krimcp							 =  null;
        planPriemaIG					 =  null;
        ppDogLgot						 =  null;      
        ppKrimDog						 =  null;
        ppKrimDogLgot					 =  null;
    }

 public void setBean(AbiturientBean bean,HttpServletRequest request,ActionErrors errors) throws ServletException {
        if (bean!=null) {
        	idNews = bean.getIdNews();

            kodFakulteta = bean.getKodFakulteta();

            kodSpetsialnosti = bean.getKodSpetsialnosti();

            kodPredmeta = bean.getKodPredmeta();

            kodKonGrp = bean.getKodKonGrp();

            planPriema = bean.getPlanPriema();
            
            planPriemaLg = bean.getPlanPriemaLg();
            
            planPriemaDog = bean.getPlanPriemaDog();

            planPriemaT1 = bean.getPlanPriemaT1();

            planPriemaT2 = bean.getPlanPriemaT2();

            planPriemaT3 = bean.getPlanPriemaT3();

            planPriemaT4 = bean.getPlanPriemaT4();

            planPriemaT5 = bean.getPlanPriemaT5();
            
            krimobshee = bean.getKrimobshee();
            
            krimok = bean.getKrimok();
            
            krimcp = bean.getKrimcp();
            
            planPriemaIG = bean.getPlanPriemaIG();
            
            ppDogLgot = bean.getPpDogLgot();
            
            ppKrimDog = bean.getPpKrimDog();
            
            ppKrimDogLgot = bean.getPpKrimDogLgot();
            
            fisId = bean.getFisId();
            formaOb = bean.getFormaOb();

            poluProhodnoiBall = bean.getPoluProhodnoiBall();

            prohodnoiBallNaSpetsialnosti = bean.getProhodnoiBallNaSpetsialnosti();

            stolbetsSortirovki = bean.getStolbetsSortirovki();

            if (bean.getPriznakSortirovki()!=null) {
                priznakSortirovki = bean.getPriznakSortirovki();
            }
            if(bean.getAbbreviatura()!=null) {
               abbreviatura = bean.getAbbreviatura();
            }
            if(bean.getJekzamenZachet()!=null){
               jekzamenZachet = bean.getJekzamenZachet();
            }
            if(bean.getNazvanieSpetsialnosti()!=null) {
               nazvanieSpetsialnosti = bean.getNazvanieSpetsialnosti();
            }
            if(bean.getNazvanie()!=null) {
               nazvanie = bean.getNazvanie();
            }
            if(bean.getPredmet()!=null) {
               predmet = bean.getPredmet();
            }
            if(bean.getShifrSpetsialnosti()!=null) {
               shifrSpetsialnosti = bean.getShifrSpetsialnosti();
            }
            if(bean.getShifrSpetsialnostiOKSO()!=null) {
               shifrSpetsialnostiOKSO = bean.getShifrSpetsialnostiOKSO();
            }
            if(bean.getSobesedovanie()!=null) {
               sobesedovanie = bean.getSobesedovanie();
            }
            if(bean.getSpecial1()!=null) {
               special1 = bean.getSpecial1();
            }
            if(bean.getTip_Spec()!=null) {
               tip_Spec = bean.getTip_Spec();
            }
        }
    }
    public AbiturientBean getBean(HttpServletRequest request, ActionErrors errors)
    throws ServletException {

        AbiturientBean bean = new AbiturientBean();
        
        bean.setIdNews(idNews);

        bean.setKodFakulteta(kodFakulteta);

        bean.setKodSpetsialnosti(kodSpetsialnosti);

        bean.setKodPredmeta(kodPredmeta);

        bean.setKodKonGrp(kodKonGrp);

        bean.setPlanPriema(planPriema);
        
        bean.setPlanPriemaLg(planPriemaLg);
        
        bean.setPlanPriemaDog(planPriemaDog);

        bean.setPlanPriemaT1(planPriemaT1);

        bean.setPlanPriemaT2(planPriemaT2);

        bean.setPlanPriemaT3(planPriemaT3);

        bean.setPlanPriemaT4(planPriemaT4);

        bean.setPlanPriemaT5(planPriemaT5);
        
        bean.setFisId(fisId);

        bean.setFormaOb(formaOb);
        
        bean.setKrimobshee(krimobshee);
        
        bean.setKrimok(krimok);
        
        bean.setKrimcp(krimcp);
        
        bean.setPlanPriemaIG(planPriemaIG);
        
        bean.setPpDogLgot(ppDogLgot);
        
        bean.setPpKrimDog(ppKrimDog);
        
        bean.setPpKrimDogLgot(ppKrimDogLgot);
        
        bean.setPoluProhodnoiBall(poluProhodnoiBall);

        bean.setProhodnoiBallNaSpetsialnosti(prohodnoiBallNaSpetsialnosti);

        bean.setStolbetsSortirovki(stolbetsSortirovki);

        if(abbreviatura!=null && !abbreviatura.equals("")) {
           bean.setAbbreviatura(StringUtil.toDB(abbreviatura));
        }
        if(jekzamenZachet!=null && !jekzamenZachet.equals("")) {
           bean.setJekzamenZachet(StringUtil.toDB(jekzamenZachet));
        }
        if(nazvanieSpetsialnosti!=null && !nazvanieSpetsialnosti.equals("")) {
           bean.setNazvanieSpetsialnosti(StringUtil.toDB(nazvanieSpetsialnosti));
        }
        if(nazvanie!=null && !nazvanie.equals("")) {
           bean.setNazvanie(StringUtil.toDB(nazvanie));
        }
        if(predmet!=null && !predmet.equals("")) {
           bean.setPredmet(StringUtil.toDB(predmet));
        }
        if (priznakSortirovki!=null && !priznakSortirovki.equals("")) {
            bean.setPriznakSortirovki(StringUtil.toDB(priznakSortirovki));
        }
        if(shifrSpetsialnosti!=null && !shifrSpetsialnosti.equals("")) {
           bean.setShifrSpetsialnosti(StringUtil.toDB(shifrSpetsialnosti));
        }
        if(shifrSpetsialnostiOKSO!=null && !shifrSpetsialnostiOKSO.equals("")) {
           bean.setShifrSpetsialnostiOKSO(StringUtil.toDB(shifrSpetsialnostiOKSO));
        }
        if(sobesedovanie!=null && !sobesedovanie.equals("")) {
           bean.setSobesedovanie(StringUtil.toDB(sobesedovanie));
        }
        if(special1!=null && !special1.equals("")) {
           bean.setSpecial1(StringUtil.toDB(special1));
        }
        if(tip_Spec!=null && !tip_Spec.equals("")) {
           bean.setTip_Spec(StringUtil.toDB(tip_Spec));
        }
        return bean;
    }      
}