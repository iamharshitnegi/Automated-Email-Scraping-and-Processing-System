package com.resavenue.mars.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.resavenue.mars.form.MailScrapingRategainForm;

public interface MailScrapingRategainService {

	

	Map<String, String> parseHtmlFile(File file);

	void insertParseData(Map<String, String> dataMap);

	

	StringBuilder createScrapInvoiceMailContent(MailScrapingRategainForm vInvoiceForm, HttpServletRequest req,
			HttpServletResponse res, String propId, MailScrapingRategainForm vPaymentDetails);

	MailScrapingRategainForm simpleInvoiceDetails(String mInvoiceNo);

	StringBuilder createScrapInvoiceMailContent(MailScrapingRategainForm vInvoiceForm);

	

}
