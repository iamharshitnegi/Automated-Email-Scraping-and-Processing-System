
package com.resavenue.mars.schedulars;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.*;
import com.resavenue.mars.util.ResUtil;
import java.net.*;
import java.util.Date;
import java.io.*;
import java.lang.String;
public class CronJob implements Job  {
	
	private static org.apache.log4j.Logger log = Logger
			.getLogger(CronJob.class);
	public CronJob()
	{
		log.info("CronJob Job Invocked ");
		  ResUtil.printLog("scrap_mail","CronJob Job Invocked ");
		
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		log.info("CronJob Job Invocked in execute method ");
		  ResUtil.printLog("scrap_mail","CronJob Job Invocked in execute method ");
		String vUrl = ResUtil.getProperty("resavenue", "url");
		
		if(context.getTrigger().getName().equalsIgnoreCase("CCAvenueAuth")) {
			  log.info("In CCAvenueAuthQRyServlet Job : JobTrigger Name"+context.getTrigger().getName());
			  ResUtil.printLog("scrap_mail","In CCAvenueAuthQRyServlet Job : JobTrigger Name"+context.getTrigger().getName());
			try{
				
				
				
				log.info("In try block of CCAvenueAuth "+vUrl);
				ResUtil.printLog("scrap_mail","In try block of CCAvenueAuth "+vUrl);
				URL url =new URL(vUrl+"/res_mars_new/CCAvenueAuthQry4");  
				//URL url =new URL("http://192.168.3.57:8080/res_mars_new/CCAvenueAuthQry4");
				//URL url =new URL("https://172.17.2.87/res_mars_new/CCAvenueAuthQry4");
			
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
			
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				    String line = in.readLine();
				    log.info("Successfull Hit to CCAvenueAuthQRyServlet Scheduler :  Returned code "+line);
				    ResUtil.printLog("scrap_mail","Successfull Hit to CCAvenueAuthQRyServlet Scheduler :  Returned code "+line);
				    
				    in.close();
				} else {
					log.info("Failed to Hit CCAvenueAuthQRyServlet Scheduler :  HTTP Response "+conn.getResponseCode());
					  ResUtil.printLog("scrap_mail","Failed to Hit CCAvenueAuthQRyServlet Scheduler :  HTTP Response "+conn.getResponseCode());
					
				}
				
			}catch(Exception e){
				log.info("Exception : "+e);
				  ResUtil.printLog("scrap_mail","Exception : "+e);
			}
	 }	else if(context.getTrigger().getName().equalsIgnoreCase("expiryReminder")) { 
			
	  	
	  		log.info("In InvoiceReportMail Scheduler Job :-"+context.getTrigger().getName());
	  		 ResUtil.printLog("scrap_mail","In InvoiceReportMail Scheduler Job :-"+context.getTrigger().getName());
			URL url = null;
			HttpURLConnection conn1 = null;
			Date jobTime=context.getFireTime();
			String fireTime=jobTime.getHours()+":"+jobTime.getMinutes();
			log.info("In InvoiceReportMail Scheduler Job :-"+fireTime);
			 ResUtil.printLog("scrap_mail","In InvoiceReportMail Scheduler Job :-"+fireTime);
			try{
				
				
				log.info("In try block of expiryReminder "+vUrl);
				 ResUtil.printLog("scrap_mail","In try block of expiryReminder "+vUrl);
				url = new URL(vUrl+"/res_mars_new/CCAvenueAuthQry4?mailType=expiry&fireTime="+fireTime);
				// url =new URL("https://172.17.2.87/res_mars_new/CCAvenueAuthQry4?mailType=expiry&fireTime="+fireTime);
				conn1 = (HttpURLConnection)url.openConnection();
				conn1.connect();
			
				if (conn1.getResponseCode() == HttpURLConnection.HTTP_OK) {
				    BufferedReader in = new BufferedReader(new InputStreamReader(conn1.getInputStream()));
				    String line = in.readLine();
				    log.info("Successfull Hit to expiryReminder scheduler :  Returned code "+line);
				    ResUtil.printLog("scrap_mail","Successfull Hit to expiryReminder scheduler :  Returned code "+line);
				    in.close();
				} else {
					log.info("Failed to Hit expiryReminder scheduler :  HTTP Response "+conn1.getResponseCode());
					ResUtil.printLog("scrap_mail","Failed to Hit expiryReminder scheduler :  HTTP Response "+conn1.getResponseCode());
				}
			}catch(Exception e){
				log.info("Exception : "+e);
				ResUtil.printLog("scrap_mail","Exception : "+e);
			}finally{
				try {
					conn1.disconnect();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
	  		//
	  		
	  		
	 }
	 else if(context.getTrigger().getName().equalsIgnoreCase("maxiMojoResNotifRQ")) { 
			
		  	
	  		log.info("In maximojo ResNotifRQ push Scheduler Job :-"+context.getTrigger().getName());
	  		 ResUtil.printLog("maximojo","In maximojo ResNotifRQ push Scheduler Job :-"+context.getTrigger().getName());
			URL url = null;
			HttpURLConnection conn1 = null;
			Date jobTime=context.getFireTime();
			String fireTime=jobTime.getHours()+":"+jobTime.getMinutes();
			log.info("In maximojo ResNotifRQ push Scheduler Job :-"+fireTime);
			 ResUtil.printLog("maximojo","In maximojo ResNotifRQ push Scheduler Job :-"+fireTime);
			try{
				
				url = new URL(vUrl+"/res_mars_new/sendDetailsMaxiMojo");
				
				conn1 = (HttpURLConnection)url.openConnection();
				conn1.connect();
			
				if (conn1.getResponseCode() == HttpURLConnection.HTTP_OK) {
				    BufferedReader in = new BufferedReader(new InputStreamReader(conn1.getInputStream()));
				    String line = in.readLine();
				    log.info("Successfull Hit to maximojo ResNotifRQ push scheduler :  Returned code "+line);
				    ResUtil.printLog("maximojo","Successfull Hit to maximojo ResNotifRQ push scheduler :  Returned code "+line);
				    in.close();
				} else {
					log.info("Failed to Hit maximojo ResNotifRQ push scheduler :  HTTP Response "+conn1.getResponseCode());
					ResUtil.printLog("maximojo","Failed to Hit maximojo ResNotifRQ push scheduler :  HTTP Response "+conn1.getResponseCode());
				}
			}catch(Exception e){
				log.info("Exception : "+e);
				ResUtil.printLog("maximojo","Exception : "+e);
			}finally{
				try {
					conn1.disconnect();
				} catch (Exception e) {
					
				}
			}
	  		
	  	
	 }
		
	} 
 }
