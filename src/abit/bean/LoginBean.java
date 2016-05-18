package abit.bean;

 public class LoginBean {
  protected Integer id;
  protected Integer kodVuza;
  protected String  userName;   
  protected String  userGroup;
  protected String  password;

 public Integer getId(){
        return id;
 }

 public Integer getKodVuza(){
        return kodVuza;
 }

 public String getUserName(){
        return userName;
 }
 
 public String getUserGroup(){
        return userGroup;
 }

 public String getPassword(){
        return password;
 }

 public void setId(Integer value){
        id = value;
 }

 public void setKodVuza(Integer value){
        kodVuza = value;
 }

 public void setUserName(String value){
        userName = value;
 }

 public void setUserGroup(String value){
        userGroup = value;
 }

 public void setPassword(String value){
        password = value;
 }

}