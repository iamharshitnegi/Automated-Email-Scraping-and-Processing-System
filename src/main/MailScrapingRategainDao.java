package com.resavenue.mars.dao;

import com.resavenue.mars.form.MailScrapingRategainForm;

public interface MailScrapingRategainDao {

	void insertParseData(MailScrapingRategainForm vRategainForm);

	MailScrapingRategainForm getSimpleInvoiceDetails(String mInvoiceNo);

	

}
