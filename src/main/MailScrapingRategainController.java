package com.resavenue.mars.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.OrTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.resavenue.mars.Auth.ResavenueMailAuthBean;
import com.resavenue.mars.dao.MailScrapingRategainDao;
import com.resavenue.mars.service.MailScrapingRategainService;
import com.resavenue.mars.util.ResUtil;



@Controller
public class MailScrapingRategainController {

	@Autowired
	MailScrapingRategainService vMailScrapingRategainService;

	@Autowired
	MailScrapingRategainDao vMailScrapingRategainDao;

	final static Logger LOGGER = Logger.getLogger(MailScrapingRategainController.class.getName());

	//@Scheduled(cron = "0 0/30 * * * ?")
	@RequestMapping(value = { "/rategainmailStoreConnector" }, method = { RequestMethod.GET, RequestMethod.POST })
	public synchronized void rategainmailStoreConnector() {

		try {
			String host = "180.179.175.2";
			String username = "otainvoicingstella@resavenue.com";
			String password = "AleOta@!#%";
			int port = 995;
			boolean useSSL = true;

			Flags seen = new Flags(Flags.Flag.SEEN);

			// Create properties for the mail session

			Properties properties = new Properties();
			properties.setProperty("mail.store.protocol", "pop3");
			properties.setProperty("mail.pop3.host", host);
			properties.setProperty("mail.pop3.port", String.valueOf(port));

			properties.setProperty("mail.pop3.ssl.enable", String.valueOf(useSSL));
			properties.setProperty("mail.mime.base64.ignoreerrors", String.valueOf(useSSL));
			properties.setProperty("mail.pop3s.connectiontimeout", "900000");
			properties.setProperty("mail.pop3s.timeout", "900000");

			properties.setProperty("mail.pop3.ssl.protocols", "TLSv1.2");

			LOGGER.info("==========================inside rategainmailStoreConnector=================================");

			// Create the JavaMail session
			Session session = Session.getDefaultInstance(properties);

			// Create the POP3 store
			Store store = session.getStore("pop3");
			// Check if the connection is already open
			if (store.isConnected()) {
				LOGGER.info(
						"=================Connection is already open. Closing the existing connection. ==============");
				closeConnection(store);
			}
			// Connect to the POP3 store
			store.connect(host, username, password);

			LOGGER.info("=================================Connection  sucessful.=====================================");
			Folder fldr = store.getFolder("INBOX");
			fldr.open(Folder.READ_WRITE);
			int count = fldr.getMessageCount();
			FetchProfile fetchProfile = new FetchProfile();

			// Define the search terms for multiple subjects
			String[] subjects = { "Stella Di Mare Dubai Marina" };
			SearchTerm searchTerm = new OrTerm(
					Arrays.stream(subjects).map(SubjectTerm::new).toArray(SearchTerm[]::new));

			fetchProfile.add(FetchProfile.Item.CONTENT_INFO);
			int batchSize = 50;
			int startIndex = 1;
			int endIndex = count;
			LOGGER.info(
					"=======================inside rategainmailStoreConnector ==================total message count:"
							+ count);
			// Perform additional checks
			if (endIndex < startIndex) {

				LOGGER.info(
						"================inside rategainmailStoreConnector=====================No messages to retrieve.");
				fldr.close(false);
				store.close();
				return;
			}

			if (endIndex > fldr.getMessageCount()) {
				endIndex = fldr.getMessageCount();
			}
			// Process messages in batches
			while (startIndex <= endIndex) {

				int currentBatchEndIndex = Math.min(startIndex + batchSize - 1, endIndex);
				System.out.println("currentBatchEndIndex" + currentBatchEndIndex);
				LOGGER.info("================inside rategainmailStoreConnector==============startIndex:======="
						+ startIndex);
				LOGGER.info(
						"================inside rategainmailStoreConnector==============endIndex:=========" + endIndex);
				LOGGER.info("================inside rategainmailStoreConnector==============batchsize:========"
						+ batchSize);
				LOGGER.info("================inside rategainmailStoreConnector=========currentBatchEndIndex.=="
						+ currentBatchEndIndex);

				Message[] messages = fldr.search(searchTerm, fldr.getMessages(startIndex, currentBatchEndIndex));
				fldr.fetch(messages, fetchProfile);
				for (Message message : messages) {
					if (message.getSubject().contains("Stella Di Mare Dubai Marina")) {
						writeToPart(message);
						File input = new File(
								"/opt/servers/jboss-eap-6.4/standalone/deployments/res_mars_new.war/tp/mail_upload/mail.html");
						try {
							// scrapping booking info from html
							Map<String, String> dataMap = null;
							dataMap = vMailScrapingRategainService.parseHtmlFile(input);

							// 1st condtion ---> check wheather the scrapped data is present or not
							if (dataMap != null && !dataMap.isEmpty()) {

								if (dataMap.containsKey("Origin")) {
									String origin = ResUtil.checkNull(dataMap.get("Origin"));
									LOGGER.info("Origin:: for persiting data : " + origin);

									// 2nd conditon ----> check wheather the origin contains Booking.com
									if (origin.contains("Booking.com")) {

										String payoutType = ResUtil.checkNull(dataMap.get("Payout  Type"));
										LOGGER.info("Payout Type:: for persiting data : " + payoutType);

										// 3rd condition ----> allow other than Virtual credit card
										if (!payoutType.contains("Virtual credit card") || payoutType.isEmpty()) {

											String bookingDateStr = ResUtil.checkNull(dataMap.get("Booking Date"));

											DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

											try {
												LocalDate bookingDate = LocalDate.parse(bookingDateStr, formatter);
												LocalDate currentDate = LocalDate.now();

												// 4th condition ---> allowing only when booking date is current or
												// after the current date
												if (!bookingDate.isBefore(currentDate)) {
													LOGGER.info("Booking date is ::  " + currentDate);

													String bookingStatus = dataMap.get("Booking Status");

													// 5th condition --->allowing only when the booking status is 'New'
													if (bookingStatus.contains("New")) {
														LOGGER.info("===========inside rategainmailStoreConnector ==========calling service (insertParseData ()) to process mail data:======.");
														vMailScrapingRategainService.insertParseData(dataMap);			
													}

												}
											} catch (DateTimeParseException e) {
												LOGGER.info("Invalid booking date format: " + bookingDateStr);
											}
										}
									}
								}
							}

							LOGGER.info(
									"===================inside rategainmailStoreConnector Done reading and writing mail data================ ");

						} catch (Exception e1) {
							LOGGER.info(
									"===================inside rategainmailStoreConnector ==================Exception :=================."
											+ e1);
						}
					}

				}
				// Increment the start index for the next batch

				startIndex += batchSize;

				LOGGER.info("============inside rategainmailStoreConnector=========incrementing start index======="
						+ startIndex);
			}

			// Close the folder
			fldr.close(false);
			LOGGER.info("=================inside rategainmailStoreConnector================Folder closed");

			// Close the store
			store.close();
			LOGGER.info("=================inside rategainmailStoreConnector=================Store closed");

		} catch (Exception e) {
			LOGGER.info("=================Exception :: rategainmailStoreConnector=================== " + e);

		}
	}

	public static void closeConnection(Store store) {
		try {
			if (store != null && store.isConnected()) {
				Folder fldr = store.getFolder("INBOX");
				fldr.close(false);
				store.close();
				// System.out.println("Closed connection.");
				LOGGER.info("Closed connection.");
			}
		} catch (MessagingException e) {
			// System.out.println("Error closing the connection: " + e.getMessage());
			LOGGER.info("Error closing the connection: " + e.getMessage());
		}
	}

	public static void writeToPart(Message message) throws Exception {
		LOGGER.info(
				"======================inside MailScrapingController::================================writeToPart()");
		FileOutputStream fos = new FileOutputStream(
				"/opt/servers/jboss-eap-6.4/standalone/deployments/res_mars_new.war/tp/mail_upload/mail.html");
		Object content = message.getContent();

		if (message instanceof Message)
			writeEnvelope(message);

		if (content instanceof Multipart) {
			Multipart multipart = (Multipart) content;

			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodypart = multipart.getBodyPart(i);
				// System.out.println("mime type"+bodypart.getContentType());
				LOGGER.info("===========================inside writeToPart():: ===================mime type"
						+ bodypart.getContentType());
				if (bodypart.isMimeType("text/html")) {
					Object o = bodypart.getContent();
					if (o instanceof String) {
						byte[] bytes = ((String) o).getBytes();
						fos.write(bytes);
						LOGGER.info("Done writing scraping html into file");
						fos.close();
						// System.out.println("This is a string");

						LOGGER.info("===========================================================================");

					}
				}
			}
		}

	}

	public static void writeEnvelope(Message m) throws Exception {
		// System.out.println("This is the message envelope");
		LOGGER.info("====================This is the message envelope=========================");
		LOGGER.info("=========================================================================");
		Address[] a;

		// FROM
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				LOGGER.info("FROM: " + a[j].toString());
		}

		// TO
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++)
				LOGGER.info("TO: " + a[j].toString());
		}

		// SUBJECT
		if (m.getSubject() != null)
			LOGGER.info("SUBJECT: " + m.getSubject());

	}

}
