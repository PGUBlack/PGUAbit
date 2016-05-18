package abit.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpSession;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;

public class abitCronTrigger extends GenericServlet {
	
 private void run() throws Exception {
// First we must get a reference to a scheduler
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

// Scheduling jobs


// Job "abitFixStat" will run every 360 seconds (6 minutes)

    JobDetail job = new JobDetail("abitFixStat", "group2", abit.servlet.FixStatistics.class);
    CronTrigger trigger = new CronTrigger("abitTrig", "group2", "abitFixStat", "group2", "0/360 * * * * ?");
    sched.addJob(job, true);
    sched.scheduleJob(trigger); 

// All of the jobs have been added to the scheduler and it starting now

    sched.start();
    

    SchedulerMetaData metaData = sched.getMetaData();
    System.out.println("Executed " + metaData.numJobsExecuted() + " jobs.");
 }

 public void init()
 {
	try{
		if(true)
		{
			this.run();	
		}
	} catch(Exception e){e.printStackTrace();}
 }
@Override
public void service(ServletRequest arg0, ServletResponse arg1)
		throws ServletException, IOException {
	// TODO 
	try{
    abitCronTrigger scheduler = new abitCronTrigger();
    scheduler.run();	
	} catch(Exception e){}
}
}