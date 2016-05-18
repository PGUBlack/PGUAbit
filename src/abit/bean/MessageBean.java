package abit.bean;

public class MessageBean{

 protected int     id;
 protected String  date;
 protected String  descr;
 protected String  initiator;
 protected String  message;
 protected String  status;
 protected String  time;


//---------------------------------------
 public int getId(){
   return id;
 }
 public void setId(int val){
   id = val;
 }

//---------------------------------------
 public String getMessage(){  
   return message; 
 }
 public void setMessage(String str){
   message = str;
   id++;
 }
//---------------------------------------
 public String getStatus(){ 
   return status; 
 }
 public void setStatus(String str){
   status = str; 
 }
//---------------------------------------
 public String getDate(){ 
   return date; 
 }
 public void setDate(String str){
   date = str; 
 }
//---------------------------------------
 public String getTime(){ 
   return time; 
 }
 public void setTime(String str){
   time = str; 
 }
//---------------------------------------
 public String getInitiator(){ 
   return initiator; 
 }
 public void setInitiator(String str){
   initiator = str; 
 }
//---------------------------------------
 public String getDescr(){ 
   return descr; 
 }
 public void setDescr(String str){
   descr = str; 
 }
}