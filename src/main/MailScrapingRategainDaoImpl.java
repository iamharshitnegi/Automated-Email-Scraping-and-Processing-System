package com.resavenue.mars.daoimp;

import static org.hamcrest.CoreMatchers.nullValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.resavenue.mars.common.Procedure;
import com.resavenue.mars.common.ProcedureFactory;
import com.resavenue.mars.common.ProcedureMetaData;
import com.resavenue.mars.dao.MailScrapingRategainDao;
import com.resavenue.mars.dto.PropertyConfigDetailsDto;
import com.resavenue.mars.dto.PropertyListDto;
import com.resavenue.mars.form.InvoiceForm;
import com.resavenue.mars.form.MailScrapingRategainForm;
import com.resavenue.mars.serviceimp.MailScrapingRategainServiceImpl;
import com.resavenue.mars.util.ResUtil;


import java.util.HashMap;
import java.util.Map;


@Repository
public class MailScrapingRategainDaoImpl implements MailScrapingRategainDao {

	final static Logger LOGGER = Logger.getLogger(MailScrapingRategainDaoImpl.class.getName());

	@Override
	public void insertParseData(MailScrapingRategainForm vRategainForm) {
		LOGGER.info("==========Inside MailScrapingRategainDaoImpl : in insertParseData()===================");

		String vIsError = null;
		String vErrorDesc = null;
		String modID=null;

		RowSet dbResultSet = null;
		Procedure vProcedure = ProcedureFactory.getProcedure();
		ProcedureMetaData vProcedureMeta;

		String phone = vRategainForm.getPhone();
		// Remove leading '+'
		if (phone.startsWith("+")) {
		    phone = phone.substring(1);
		}
		
		try {
			vProcedureMeta = new ProcedureMetaData(" PR$set_invoice_Scrap_new_mars_rategain");
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getFirstname()), java.sql.Types.VARCHAR, false); // 1
			// FirstName
			vProcedureMeta.addParameter("", java.sql.Types.VARCHAR, false); // 2 MiddleName

			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getLastname()), java.sql.Types.VARCHAR, false); // 3
			// MiddleName
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getBookingRefNo()), java.sql.Types.VARCHAR,
					false); // 4 ReferenceNo
			vProcedureMeta.addParameter(ResUtil.checkNull(phone).replaceAll("\\s+", ""),
					java.sql.Types.VARCHAR, false); // 5 Mobile No
			vProcedureMeta.addParameter(ResUtil.checkNull(phone).replaceAll("\\s+", ""),
					java.sql.Types.VARCHAR, false); // 6 Mobile No
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getEmail()), java.sql.Types.VARCHAR, false); // 7
			// email
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getBookingDate()), java.sql.Types.VARCHAR,
					false); // 8 booking date
			vProcedureMeta.addParameter("", java.sql.Types.VARCHAR, false); // 9 description for description call
			// service();
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getMod_amt()), java.sql.Types.VARCHAR, false); // 10
			// mod_amt
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getCurrency()), java.sql.Types.VARCHAR, false); // 11
			// Mobile
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getCancellationpolicy()),
					java.sql.Types.VARCHAR, false); // 12 term & condition
			vProcedureMeta.addParameter(ResUtil.checkNull("ResAvenue"), java.sql.Types.VARCHAR, false); // 13 invoice
			// expr
			vProcedureMeta.addParameter("I", java.sql.Types.VARCHAR, false); // 14 Ref
			vProcedureMeta.addParameter(ResUtil.checkNull("Scrap"), java.sql.Types.VARCHAR, false); // 15 Ref
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getPropName()), java.sql.Types.VARCHAR, false); // 16
			// propName
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getRatePlan()), java.sql.Types.VARCHAR, false); // 17
			// rate_pan
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getOrigin()), java.sql.Types.VARCHAR, false); // 18
			// origin
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getCheckin()), java.sql.Types.VARCHAR, false); // 19
			vProcedureMeta.addParameter(ResUtil.checkNull(vRategainForm.getCheckout()), java.sql.Types.VARCHAR, false); // 20
			vProcedureMeta.addParameter(null, java.sql.Types.VARCHAR, true); // 21 error
			vProcedureMeta.addParameter(null, java.sql.Types.VARCHAR, true); // 22 errordesc
			vProcedureMeta.addParameter(null, java.sql.Types.VARCHAR, true); // 23 checksum
			vProcedureMeta.addParameter(null, java.sql.Types.VARCHAR, true); // 24 modid
			if (vProcedureMeta != null) {
				ProcedureMetaData procedureMetaOut = vProcedure.execute(vProcedureMeta);
				vIsError = (String) (procedureMetaOut.getParameterByindex(21)).getOutputValue();
				vErrorDesc = (String) (procedureMetaOut.getParameterByindex(22)).getOutputValue();
				modID=(String)(procedureMetaOut.getParameterByindex(24)).getOutputValue();
				LOGGER.info("MOD_ID ::"+modID);
				
				LOGGER.info("vIsError:" + vIsError + "\t" + "vIsError Desc" + vErrorDesc);
				if (vErrorDesc == null) {
					LOGGER.info("~~~Invoice Created successfully~~~  ");

				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			LOGGER.info("~~~Exception e~~~  " + e);
		} finally {
			try {
				if (dbResultSet != null)
					dbResultSet.close();
				vProcedure.closeResources();
			} catch (Exception exception) {
				// exception.printStackTrace();
				LOGGER.info("~~~finally catch Exception e~~~  " + exception);
			}
		}

	}

	@Override
	public MailScrapingRategainForm getSimpleInvoiceDetails(String mInvoiceNo) {
		String vIsError=null;
		String vErrorDesc=null;
		

		MailScrapingRategainForm pInvoiceForm = new MailScrapingRategainForm();
	
		RowSet dbResultSet = null;
		RowSet dbResultSet1 = null;
		
		Procedure vProcedure = ProcedureFactory.getProcedure();
		ProcedureMetaData vProcedureMeta;
		
		
		try {
			vProcedureMeta = new ProcedureMetaData("PR$fetch_order_tax_detail");
			
			vProcedureMeta.addParameter(ResUtil.checkNull(mInvoiceNo),java.sql.Types.VARCHAR,false);								//1 propId
			vProcedureMeta.addParameter(null,java.sql.Types.VARCHAR,true);		//2 error
			vProcedureMeta.addParameter(null,java.sql.Types.VARCHAR,true);		// 3 error_desc	
			
			vProcedureMeta.addParameter(null,ProcedureMetaData.TYPE_CURSOR, true);// 4 cursor
			vProcedureMeta.addParameter(null, ProcedureMetaData.TYPE_CURSOR, true);// 5 cursor
			if (vProcedureMeta != null) {
				ProcedureMetaData procedureMetaOut = vProcedure.execute(vProcedureMeta);

				vIsError = (String) (procedureMetaOut.getParameterByindex(2)).getOutputValue();
				vErrorDesc = (String) (procedureMetaOut.getParameterByindex(3).getOutputValue());
				dbResultSet = (RowSet) (procedureMetaOut.getParameterByindex(4).getOutputValue());
				dbResultSet1 = (RowSet) (procedureMetaOut.getParameterByindex(5).getOutputValue());

				if (dbResultSet != null) {
					while (dbResultSet.next()) {
						pInvoiceForm.setMerRefenceNo(dbResultSet.getString("mod_refno"));
						pInvoiceForm.setMerAmount(dbResultSet.getString("mod_amt"));
						pInvoiceForm.setMerCurrId(dbResultSet.getString("curr_id"));

						pInvoiceForm.setMerCustFirstName(dbResultSet.getString("mod_cust_first_name"));
						pInvoiceForm.setMerCustMiddleName(dbResultSet.getString("mod_cust_middle_name"));
						pInvoiceForm.setMerCustLastName(dbResultSet.getString("mod_cust_last_name"));
						pInvoiceForm.setInvTaxAmt(dbResultSet.getString("mod_tax_amt"));
						pInvoiceForm.setTotalTaxAmt(
								ResUtil.checkNull(dbResultSet.getString("mod_total_tax_amt")).equalsIgnoreCase("") ? "0"
										: dbResultSet.getString("mod_total_tax_amt"));

						pInvoiceForm.setInvType(dbResultSet.getString("invoice_type"));
						pInvoiceForm.setMod_checksum(dbResultSet.getString("mod_checksum"));
						pInvoiceForm.setCheckin(ResUtil.checkNull(dbResultSet.getString("mod_arrival")));
						pInvoiceForm.setCheckout(ResUtil.checkNull(dbResultSet.getString("mod_departure")));
						
//						pInvoiceForm.setRoomPrice(dbResultSet.getString("mod_amt"));
//						pInvoiceForm.setTotalRoomPrice(dbResultSet.getString("excluding_total_tax_amt"));
						
					}

				}
				
				 // Process dbResultSet1
		        Map<String, Map<String, String>> taxDetails = new HashMap<>();
		        if (dbResultSet1 != null) {
		            while (dbResultSet1.next()) {
		                String taxName = dbResultSet1.getString("tax_name");
		                String taxPer = dbResultSet1.getString("tax_per");
		                String taxAmount = dbResultSet1.getString("tax_amount");

		                Map<String, String> taxInfo = new HashMap<>();
		                taxInfo.put("taxPer", taxPer);
		                taxInfo.put("taxAmount", taxAmount);

		                taxDetails.put(taxName, taxInfo);
		            }
		        }

		        //Accessing the tax details
		        if (taxDetails.containsKey("Service Charge")) {
		            Map<String, String> serviceChargeDetails = taxDetails.get("Service Charge");
		            String serviceChargePer = serviceChargeDetails.get("taxPer");
		            String serviceChargeAmount = serviceChargeDetails.get("taxAmount");
		            
		            pInvoiceForm.setServiceCharge(serviceChargeAmount);
		            pInvoiceForm.setServiceChargePer(serviceChargePer);
		            System.out.println("Service Charge Percentage: " + pInvoiceForm.getServiceChargePer());
		            System.out.println("Service Charge Amount: " + pInvoiceForm.getServiceCharge());
		        }

		        if (taxDetails.containsKey("Municipality")) {
		            Map<String, String> municipalityDetails = taxDetails.get("Municipality");
		            String municipalityPer = municipalityDetails.get("taxPer");
		            String municipalityAmount = municipalityDetails.get("taxAmount");
		            pInvoiceForm.setMunicipalityFee(municipalityAmount);
		            pInvoiceForm.setMunicipalityFeePer(municipalityPer);
		            
		            System.out.println("Municipality Tax Percentage: " + pInvoiceForm.getMunicipalityFeePer());
		            System.out.println("Municipality Tax Amount: " + pInvoiceForm.getMunicipalityFee());
		        }

		        if (taxDetails.containsKey("vat")) {
		            Map<String, String> vatDetails = taxDetails.get("vat");
		            String vatPer = vatDetails.get("taxPer");
		            String vatAmount = vatDetails.get("taxAmount");
		            pInvoiceForm.setVat(vatAmount);
		            pInvoiceForm.setVatPer(vatPer);
		            
		            System.out.println("VAT Percentage: " + pInvoiceForm.getVatPer());
		            System.out.println("VAT Amount: " + pInvoiceForm.getVat());
		        }
				
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dbResultSet != null) {
					dbResultSet.close();
				}
				if (dbResultSet1 != null)
					dbResultSet1.close();

				vProcedure.closeResources();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		return pInvoiceForm;
	}

}
