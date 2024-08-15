package com.resavenue.mars.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.resavenue.mars.Auth.ResavenueMailAuthBean;
import com.resavenue.mars.form.InvoiceForm;
import com.resavenue.mars.form.MailScrapingRategainForm;
import com.resavenue.mars.invoiceresmail.SendCCAvenueAuthMail;
import com.resavenue.mars.service.MailScrapingRategainService;
import com.resavenue.mars.util.ResMail;
import com.resavenue.mars.util.ResUtil;


@Controller
public class RateGainMAil {

	@Autowired
	MailScrapingRategainService vMailScrapingRategainService;

	private static org.apache.log4j.Logger log = Logger.getLogger(RateGainMAil.class);

	//@Scheduled(cron = "0 0/45 * * * ?")
	@RequestMapping(value = { "/rateGainSendMail" }, method = { RequestMethod.GET, RequestMethod.POST })
	public void rateGainSendMail() {

		ResUtil.printLog("scrap_mail",
				"====================Inside RateGainMail in rateGainSendMail()==========================");
		log.info("====================Inside RateGainMail in rateGainSendMail()==========================");

		
		ResavenueMailAuthBean vResavenueMailAuthBean = null;
		boolean creationMailStatus = false;

		try {

			ArrayList<ResavenueMailAuthBean> mailRecords = ResUtil.fetchMailLogsDetails();
			log.info(
					"=====================mail records in creation mail ========================" + mailRecords.size());

			if (mailRecords != null) {
				if (mailRecords.size() > 0) {
					for (int i = 0; i < mailRecords.size(); i++) {
						log.info("In for loop mailRecords=" + mailRecords.size());
						ResUtil.printLog("scrap_mail", "In for loop mailRecords=" + mailRecords.size());
						vResavenueMailAuthBean = mailRecords.get(i);

						try {

							String mInvoiceNo = ResUtil.checkNull(vResavenueMailAuthBean.getmInvoiceNo());
							
							// String mailFrom = ResUtil.checkNull(vResavenueMailAuthBean.getMailFrom());
							// String mailTo = ResUtil.checkNull(vResavenueMailAuthBean.getMailTo());
							
							String mailFrom = "service@resavenue.com";
							String mailTo = "avinash.rai@avenues.info";

							log.info("=====InvoiceNo :" + mInvoiceNo);
							log.info("=====mailFrom :" + mailFrom);
							log.info("=====mailTo" + mailTo);
							log.info("=====Bcclist :" + ResUtil.checkNull(ResUtil.getBccList()));

							MailScrapingRategainForm vInvoiceForm = vMailScrapingRategainService
									.simpleInvoiceDetails(mInvoiceNo);
							log.info("====================Mail sending execution Started =============================");		
							log.info("================================================================================");
							log.info("merRefenceNo:  " + vInvoiceForm.getMerRefenceNo());
							log.info("merCustFirstName:  " + vInvoiceForm.getMerCustFirstName());
							log.info("merCustMiddleName:  " + vInvoiceForm.getMerCustMiddleName());
							log.info("merCustLastName:  " + vInvoiceForm.getMerCustLastName());
							log.info("checksum:  " + vInvoiceForm.getMod_checksum());
							

							StringBuilder mailContent1 = vMailScrapingRategainService
									.createScrapInvoiceMailContent(vInvoiceForm);

							String mailContent2 = mailContent1.toString();
							System.out.println("mail body = " + mailContent2.toString().replaceAll("\\s+", " "));
							creationMailStatus = ResMail.sendMail(ResUtil.checkNull(mailFrom),ResUtil.checkNull(mailTo), "narendra.payasi@avenues.info","narendra.payasi@avenues.info","Hotel Booking Payment Required:" + vInvoiceForm.getMerRefenceNo() + "",ResUtil.checkNull(mailContent2), "", "1");

							log.info("=====mailFrom :" + mailFrom);
							ResUtil.printLog("scrap_mail", "mailFrom :" + mailFrom);
							log.info("=====mailTo" + mailTo);
							ResUtil.printLog("scrap_mail", "mailTo :" + mailTo);
							log.info("=====Bcclist :" + ResUtil.checkNull(ResUtil.getBccList()));
							ResUtil.printLog("scrap_mail", "Bcclist :" + ResUtil.checkNull(ResUtil.getBccList()));

							log.info("=====creationMailStatus :" + creationMailStatus);
							ResUtil.printLog("scrap_mail", "creationMailStatus :" + creationMailStatus);

							if (creationMailStatus == true) {
								log.info(
										"==============The resend process of failed mails, status is Successful=================");
								ResUtil.printLog("scrap_mail",
										"The resend process of failed mails, status is Successful");

							} else {
								log.info(
										"======================Resend mail proces status is Failed==============================");
								ResUtil.printLog("scrap_mail", "Resend mail proces status is Failed");
							}

						} catch (Exception e) {
							e.printStackTrace();
							log.info("=======================Exception during sending the failed mails ="
									+ e.toString());
							ResUtil.printLog("scrap_mail",
									"Exception during sending the failed mails =" + e.toString());
						}
					}
				}
				//SendCCAvenueAuthMail mail = new SendCCAvenueAuthMail();
				//mail.SendMail(mailRecords);
			}

		} catch (Exception e) {
			log.info("=======================Exception  In CCAvenueAuthQry Servlet =" + e.toString()
					+ "========================");
			ResUtil.printLog("scrap_mail", "Exception  In CCAvenueAuthQry Servlet =" + e.toString());
			e.printStackTrace();
		}

	}
}
