package com.resavenue.mars.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.Adler32;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.resavenue.mars.Auth.ResavenueMailAuthBean;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ResUtil {

	static PrintWriter pw;
	static File file;
	private static HashMap<String, String> vPropertyMap = null;
	// static String mWorkingKey = "bdcfe2222abc9983"; commnetd by narendra
	// payasi[2011-9-5]

	static String mWorkingKey = "kdgfj823hmfso7sksdfql63zhipzay"; // added by
																	// narenndra
																	// payasi[2011-9-5]

	static String ccAveWorkingKey = "JpUS5INB63435PCCrkSLMg";

	static String tcAesKey = "FABE114254BDBC7823534894FFFCCC1";
	// to genrate the password
	private static final String charset = "!#$&@0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	final static Logger logger = Logger.getLogger(ResUtil.class.getName());
	private static String resavenueMailQue = ""; // For JMS Mail
	private static String resavenueSMSQue = "";// For JMS SMS

	private ResUtil() {
		dbProps = new Properties();
		getProps();
		count++;
	}

	public void getProps() {
		InputStream is = getClass().getResourceAsStream("/com/resavenue/mars/properties/resavenue.properties");
		try {

			dbProps.load(is);
			strURL = dbProps.getProperty("url");
			strURL1 = dbProps.getProperty("url1");
			strRootPath = dbProps.getProperty("rootPath");
			strCssPath = dbProps.getProperty("cssPath");
			strImagesPath = dbProps.getProperty("imagesPath");
			strImageUploadPath = dbProps.getProperty("image_upload_path");
			strTemplateImageUploadPath = dbProps.getProperty("template_images_path");
			strPropImagesPath = dbProps.getProperty("prop_images_path");
			strPropImagesPathBooking = dbProps.getProperty("prop_images_path_book");
			getPropImagesPath = dbProps.getProperty("propImagesPath");
			strPropCssPath = dbProps.getProperty("prop_css_path");
			strTemplateCssPath = dbProps.getProperty("template_css_path");
			PropCssPath = dbProps.getProperty("propcsspath");
			strJavascriptPath = dbProps.getProperty("scriptPath");
			strJdbcPath = dbProps.getProperty("jdbc_lookup");
			strCCJdbcPath = dbProps.getProperty("ccav_jdbc_lookup");
			strLogFilePath = dbProps.getProperty("log_file_path");
			strInitialCtxFactory = dbProps.getProperty("initial_context_factory");
			strServerIp = dbProps.getProperty("server_ip");
			strBccList = dbProps.getProperty("bccList");
			strJndiPort = dbProps.getProperty("jndi_port");
			strJndiUrlPkgs = dbProps.getProperty("jboss_jndi_pkgs");
			strSmtpHost = dbProps.getProperty("smtpHost");
			logger.info(" strSmtpHost " + strSmtpHost);
			strSitePath = dbProps.getProperty("siteman");
			strGlobalLogFile = dbProps.getProperty("logfile");
			strLogFolder = dbProps.getProperty("logfolder");
			strKeyPath = dbProps.getProperty("keyPath");
			strKekPath = dbProps.getProperty("kekPath");
			strXmlFolder = dbProps.getProperty("xmlfolder");
			strPropPath = dbProps.getProperty("propertyPath");
			strDevList = dbProps.getProperty("devList");
			strServiceList = dbProps.getProperty("serviceList");
			resServiceTax = dbProps.getProperty("resServiceTax");
			schemeTypes = dbProps.getProperty("scheme_type_qry");
			strXlsFilePath = dbProps.getProperty("xlsFilePath");
			webPort = dbProps.getProperty("web_port");
			setResavenueMailQue(dbProps.getProperty("resavenueMailQue"));
			setResavenueSMSQue(dbProps.getProperty("resavenueSMSQue"));
			System.out.println("is ******  " + is + "strJdbcPath " + strJdbcPath);
			count1++;
			logger.info("---->file:" + strGlobalLogFile + "\t" + "floder:" + strGlobalLogFile);
		} catch (Exception e) {
			logger.info("Can't read the properties file. Make sure resavenue.properties is in the CLASSPATH");
			return;
		}
	}

	public static String get_CurrDate(String format) throws Exception {
		String curdate = null;
		CallableStatement cstmt = null;
		Connection con = null;
		try {
			con = ResUtil.getConnection();
			cstmt = con.prepareCall("{call lib$getDate(?,?)}");
			cstmt.setString(1, format);
			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
			cstmt.execute();
			curdate = "" + cstmt.getString(2);
		} catch (SQLException se) {
			logger.info("SQL Exception in get_CurrDate(String format) in the ResUtil.java file : " + se);
			throw se;
		} catch (Exception e) {
			logger.info("General Exception in get_CurrDate(String format) in the ResUtil.java file : " + e);
			throw e;
		} finally {
			try {
				ResUtil.closeConnection(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return curdate;
	}

	public static String get_CurrTime(int type) {
		SimpleDateFormat sd = null;
		switch (type) {
		case 1: // '\001'
			sd = new SimpleDateFormat("H:m:s");
			break;

		case 2: // '\002'
			sd = new SimpleDateFormat("yyyyy MMMMM dd   hh:mm aaa");
			break;
		}
		return sd.format(new java.util.Date());//
	}

	/**
	 * ] Method get_NextDate : to get next date from current day after specified
	 * days.
	 * 
	 * @param addDays
	 *            int: No of days by which to increment current date.
	 * @return String[]: Contains next date as mm,dd,yyyy
	 * @throws Exception
	 *             :
	 */
	public static String getCCAvnChecksum(String Merchant_Id, String Amount, String Order_Id, String Redirect_Url,
			String working_key) throws Exception {
		String ccav = Merchant_Id + "|" + Order_Id + "|" + Amount + "|" + Redirect_Url + "|" + working_key;

		Adler32 adl = new Adler32();
		adl.update(ccav.getBytes());
		return String.valueOf(adl.getValue());
	}

	public static String[] get_NextDate(int addDays) throws Exception {
		String[] nextDate = new String[3];
		try {
			String tmpDtStr = ResUtil.get_CurrDate("101");
			Calendar calendar = new GregorianCalendar(Integer.parseInt(tmpDtStr.substring(6, tmpDtStr.length())),
					Integer.parseInt(tmpDtStr.substring(0, 2)), Integer.parseInt(tmpDtStr.substring(3, 5)));
			calendar.add(Calendar.DATE, addDays);
			int nextDay = calendar.get(Calendar.DATE);
			int nextMonth = calendar.get(Calendar.MONTH);
			int nextYear = calendar.get(Calendar.YEAR);
			nextDate[0] = "" + nextMonth;
			nextDate[1] = "" + nextDay;
			nextDate[2] = "" + nextYear;
		} catch (Exception e) {
			throw e;
		}
		return nextDate;
	}

	/**
	 * Method get_NextDate : to get next date from given day after specified
	 * days.
	 * 
	 * @param yyyy
	 *            int: start year in yyyy format.
	 * @param mm
	 *            int: Start month in mm format.
	 * @param dd
	 *            int: Start day in dd format.
	 * @param addDays
	 *            int: No of days by which to increment given date.
	 * @return String[]: Contains next date as mm,dd,yyyy
	 * @throws Exception
	 *             :
	 */

	public static String[] get_NextDate(int yyyy, int mm, int dd, int addDays) throws Exception {
		String[] nextDate = new String[3];
		try {
			Calendar calendar = new GregorianCalendar(yyyy, mm, dd);
			calendar.add(Calendar.DATE, addDays);
			int nextDay = calendar.get(Calendar.DATE);
			int nextMonth = calendar.get(Calendar.MONTH);
			int nextYear = calendar.get(Calendar.YEAR);
			nextDate[0] = "" + nextMonth;
			nextDate[1] = "" + nextDay;
			nextDate[2] = "" + nextYear;
		} catch (Exception e) {
			throw e;
		}
		return nextDate;
	}

	public static String putDecimal(String no, int places) {
		String str_Tmp = "";
		try {
			str_Tmp = no.substring(no.indexOf(".") + 1, no.length());
		} catch (Exception e) {
			for (int x = 1; x <= places; x++) {
				str_Tmp = str_Tmp + "0";
			}

			String s = no + "." + str_Tmp;
			return s;
		}

		if (places == str_Tmp.length()) {
			return no;
		}

		str_Tmp = "";
		for (int x = places - no.substring(no.indexOf(".") + 1, no.length()).length(); x < places; x++) {
			str_Tmp = str_Tmp + "0";
		}

		return no + str_Tmp;
	}

	public static String convertTel(String tel_no, int mode) {
		StringBuffer org_Telno = new StringBuffer();
		org_Telno.append(" ");
		if (mode == 2) {
			for (int x = 0; x < tel_no.length(); x++) {
				if (!tel_no.substring(x, x + 1).equals("-")) {
					org_Telno.append(tel_no.substring(x, x + 1));
				}
			}
		} else if (mode == 1) {
			for (int x = 0; x < tel_no.length(); x++) {
				if (x == 3 || x == 6) {
					org_Telno.append("-");
				}
				org_Telno.append(tel_no.substring(x, x + 1));
			}
		}
		return org_Telno.toString().trim();
	}

	public static float roundNumber(float fl_Number, int decimal) {
		return roundNumber("" + fl_Number, decimal);
	}

	public static float roundNumber(double db_Number, int decimal) {
		return roundNumber("" + db_Number, decimal);
	}

	public static float roundNumber(String str_Number, int decimal) {
		String str_Dec = "";
		int number = 0;
		try {
			str_Dec = str_Number.substring(str_Number.indexOf(".") + 1, str_Number.length());
		} catch (NullPointerException e) {
			str_Dec = "";
		}

		if (str_Dec.length() == decimal) {
			return Float.parseFloat(str_Number);
		}
		if (str_Dec.length() > 0) {
			for (int x = str_Dec.length() - 1; x >= 0 && str_Dec.length() != decimal; x--) {
				number = Integer.parseInt(str_Dec.substring(x));
				if (number < 5) {
					str_Dec = str_Dec.substring(0, str_Dec.length() - 1);
					continue;
				}
				if (number >= 5 && number < 9) {
					if (Integer.parseInt(str_Dec.substring(x - 2, str_Dec.length() - 2)) == 9) {
						number = Integer.parseInt(str_Dec.substring(0, str_Dec.length() - 1));
						number++;
						str_Dec = "" + number;
					} else {
						number = Integer.parseInt(str_Dec.substring(x - 1, str_Dec.length() - 1));
						number++;
						str_Dec = str_Dec.substring(0, str_Dec.length() - 2);
						str_Dec = str_Dec + number;
					}
					continue;
				}
				if (number == 9) {
					number = Integer.parseInt(str_Dec.substring(0, str_Dec.length() - 1));
					number++;
					str_Dec = "" + number;
				}
			}

			return Float.parseFloat(str_Number.substring(0, str_Number.indexOf(".")) + "." + str_Dec);
		} else {
			return Float.parseFloat(str_Number);
		}
	}

	public static String replaceChar(String str, String char1, String char2) {
		StringBuffer sb_Tmp = new StringBuffer();
		for (int x = 0; x < str.length(); x++) {
			if (str.substring(x, x + 1).equals(char1)) {
				sb_Tmp.append(char2);
				continue;
			}

			if (str.substring(x, x + 1).equals(" ")) {
				sb_Tmp.append("+");
			} else {
				sb_Tmp.append(str.substring(x, x + 1));
			}
		}

		return sb_Tmp.toString();
	}

	public static String putTokFor(String a_StrDataIn, PrintWriter a_Out, String a_u_Const_Tok, String a_ToRepl) {
		StringTokenizer w_StrTok = new StringTokenizer(a_StrDataIn, a_ToRepl);
		String w_StrTemp = new String();
		int i_Ctr = 0;
		while (w_StrTok.hasMoreTokens()) {
			if (i_Ctr > 0) {
				w_StrTemp = w_StrTemp + a_u_Const_Tok;
			}
			i_Ctr++;
			w_StrTemp = w_StrTemp + w_StrTok.nextToken();
		}
		return w_StrTemp;
	}

	public static String getTokFor(String a_StrDataIn, PrintWriter a_Out, String a_u_Const_Tok, String a_ActChar) {
		StringTokenizer w_StrTok = new StringTokenizer(a_StrDataIn, a_u_Const_Tok);
		String w_StrTemp = new String();
		int i_Ctr = 0;
		while (w_StrTok.hasMoreTokens()) {
			if (i_Ctr > 0) {
				w_StrTemp = w_StrTemp + a_ActChar;
			}
			i_Ctr++;
			w_StrTemp = w_StrTemp + w_StrTok.nextToken();
		}
		return w_StrTemp;
	}

	public static int cmptwodt(String a_StrCandt, String a_StrRenDt) {
		if (a_StrCandt == null) {
			a_StrCandt = "00/00/0000";
		}

		if (a_StrRenDt == null) {
			a_StrRenDt = "00/00/0000";
		}
		Calendar w_CanDate = Calendar.getInstance();
		Calendar w_RenDate = Calendar.getInstance();
		w_CanDate.clear();
		w_RenDate.clear();
		w_CanDate.set((new Integer(a_StrCandt.substring(6, 10))).intValue(),
				(new Integer(a_StrCandt.substring(0, 2))).intValue(),
				(new Integer(a_StrCandt.substring(3, 5))).intValue());
		w_RenDate.set((new Integer(a_StrRenDt.substring(6, 10))).intValue(),
				(new Integer(a_StrRenDt.substring(0, 2))).intValue(),
				(new Integer(a_StrRenDt.substring(3, 5))).intValue());
		if (w_CanDate.before(w_RenDate)) {
			return 0;
		}
		if (w_CanDate.after(w_RenDate)) {
			return 1;
		}
		return !w_CanDate.equals(w_RenDate) ? 3 : 2;
	}

	public static void opnDbgFile(String a_StrFileName, String a_StrMode) {
		try {
			u_RndAccFile = new RandomAccessFile(a_StrFileName, a_StrMode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeDbgFile(String a_StrData) {
		try {
			u_RndAccFile.write(a_StrData.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeDbgFile() {
		try {
			u_RndAccFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getResavenueMailQue() {
		return resavenueMailQue;
	}

	public static void setResavenueMailQue(String resavenueMailQue) {
		ResUtil.resavenueMailQue = resavenueMailQue;
	}

	public static String getResavenueSMSQue() {
		return resavenueSMSQue;
	}

	public static void setResavenueSMSQue(String resavenueSMSQue) {
		ResUtil.resavenueSMSQue = resavenueSMSQue;
	}

	public static String convert(String inputString) {
		String outputString = "";
		String oldString[] = { "%", "&", "'", "+", "\\" };
		String oldHtml[] = { "&#37", "&#38", "&#39", "&#43", "&#92" };

		if (inputString != null) {
			if (inputString.indexOf("#") != -1) {
				for (int i = 0; i < inputString.length(); i++) {
					for (int j = 0; j < oldHtml.length; j++) {
						if (!inputString.regionMatches(true, i, oldHtml[j], 0, oldHtml[j].length())) {
							int max = oldHtml.length;
							if (j == max - 1) {
								outputString = outputString + inputString.charAt(i);
							}
							continue;
						}
						if (oldHtml[j].equals("&#38")) {
							outputString = outputString + "&";
						} else if (oldHtml[j].equals("&#39")) {
							outputString = outputString + "'";
						} else if (oldHtml[j].equals("&#37")) {
							outputString = outputString + "%";
						} else if (oldHtml[j].equals("&#43")) {
							outputString = outputString + "+";
						} else if (oldHtml[j].equals("&#92")) {
							outputString = outputString + "\\";
						}
						i += oldHtml[j].length() - 1;
						break;
					}
				}
			} else if (inputString.indexOf("'") != -1 || inputString.indexOf("+") != -1
					|| inputString.indexOf("%") != -1 || inputString.indexOf("&") != -1
					|| inputString.indexOf("\\") != -1) {
				for (int i = 0; i < inputString.length(); i++) {
					for (int j = 0; j < oldString.length; j++) {
						if (!inputString.regionMatches(true, i, oldString[j], 0, oldString[j].length())) {
							int max = oldString.length;
							if (j == max - 1) {
								outputString = outputString + inputString.charAt(i);
							}
							continue;
						}
						if (inputString.charAt(i) == '\'') {
							outputString = outputString + "&#39";
							break;
						}
						if (inputString.charAt(i) == '&') {
							outputString = outputString + "&#38";
							break;
						}
						if (inputString.charAt(i) == '%') {
							outputString = outputString + "&#37";
							break;
						}
						if (inputString.charAt(i) == '+') {
							outputString = outputString + "&#43";
							break;
						}
						if (inputString.charAt(i) == '\\') {
							outputString = outputString + "&#92";
						}
						break;
					}
				}
			} else {
				outputString = inputString;
			}
		}
		return outputString;
	}

	public static Context getContext() {
		Properties props = null;
		Context ctx = null;

		String providerUrl = "";
		providerUrl = strServerIp + ":" + strJndiPort;
		System.out.println("provider url::   " + providerUrl + "|:|" + strJndiUrlPkgs + "|:|" + strInitialCtxFactory);
		try {
			props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, strInitialCtxFactory);
			props.put(Context.PROVIDER_URL, providerUrl);
			props.put(InitialContext.URL_PKG_PREFIXES, strJndiUrlPkgs);
			ctx = new InitialContext(props);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ctx;
	}

	public static String checkNull(String s) {
		if ((s == null) || (s.equalsIgnoreCase("null"))) {
			return "";
		} else {
			return s.trim();
		}
	}

	public static Integer checkNull(Integer pValue) {
		if (pValue == null)
			return 0;
		else
			return pValue;
	}

	public static String checkNullNA(String s) {
		return s != null && s.length() != 0 ? s.trim() : "N/A";
	}

	public static void printLog(Object obj) {
		printLog("", obj);
	}

	public static void printLog(String fileName, Object obj) {
		printLog(fileName, obj, "");
	}

	public static void printLog(String fileName, Object obj, String format) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		String date = null;
		if (fileName.equals("")) {
			fileName = "res_";
		}
		if (format.equals("")) {
			format = "d";
		}
		if (format.equalsIgnoreCase("y")) {
			fileName = fileName + cal.get(Calendar.YEAR) + "_" + ".log";
		} else if (format.equalsIgnoreCase("d")) {
			fileName = fileName + cal.get(Calendar.YEAR) + "_" + (cal.get(Calendar.MONTH) + 1) + "_"
					+ cal.get(Calendar.DAY_OF_MONTH) + ".log";
		} else {
			fileName = fileName + cal.get(Calendar.YEAR) + "_" + (cal.get(Calendar.MONTH) + 1) + ".log";
		}

		try {
			pw = new PrintWriter(new FileWriter(strLogFolder + fileName, true), true);
			/*
			 * System.out.println("log folder is "+strLogFolder+" "+fileName);
			 */
			date = cal.getTime().toString();
			pw.println(date.substring(0, 19) + " > " + obj);
		} catch (Exception e) {
			System.out.println("Exception while writing to file " + e.getMessage());
		} finally {
			pw.close();
			cal = null;
		}
	}

	public static PrintWriter getPrintWriter() {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		String fileName = (cal.get(2) + 1) + "_" + cal.get(5) + ".log";
		try {
			pw = new PrintWriter(new FileWriter(strLogFolder + fileName, true), true);
		} catch (Exception e) {
			System.out.println("Exception while generating PrintWriter " + e.getMessage());
		}
		return pw;
	}

	public static String getKeyPath() {
		return strKeyPath;
	}

	public static String getResServiceTax() {
		return resServiceTax;
	}

	public static String getKekPath() {
		return strKekPath;
	}

	public static String getDSN() {
		return strJdbcPath;
	}

	public static String getCCAVDSN() {
		return strCCJdbcPath;
	}

	/*
	 * public static String getLogFilePath(){ return strLogFilePath; }
	 */

	public static String getLogFolder() {
		return strLogFolder;
	}

	public static String getXmlFolder() {
		return strXmlFolder;
	}

	public static String getCssPath() {
		return strCssPath;
	}

	public static String getBccList() {
		return strBccList;
	}

	public static String getdevList() {
		return strDevList;
	}

	public static String getResServiceList() { // Syed for report mail to
												// service members 9/6/2007
		return strServiceList;
	}

	public static String getImagesPath() {
		return strImagesPath;
	}

	public static String getImageUploadPath() {
		return strImageUploadPath;
	}

	public static String setPropImagesPath() {
		return strPropImagesPath;
	}

	public static String setPropCssPath() {
		return strPropCssPath;
	}

	public static String getStrPropCssPath() {
		return strPropCssPath;
	}
	

	public static String getStrTemplateImageUploadPath() {
		return strTemplateImageUploadPath;
	}

	public static void setStrTemplateImageUploadPath(String strTemplateImageUploadPath) {
		ResUtil.strTemplateImageUploadPath = strTemplateImageUploadPath;
	}

	public static String getStrTemplateCssPath() {
		return strTemplateCssPath;
	}

	public static void setStrTemplateCssPath(String strTemplateCssPath) {
		ResUtil.strTemplateCssPath = strTemplateCssPath;
	}

	public static String getStrXlsFilePath() {
		return strXlsFilePath;
	}

	public static String setStrXlsFilePath() {
		return strXlsFilePath;
	}

	public static String getRootPath() {
		return strRootPath;
	}

	public static String getPropCssPath() {
		return PropCssPath;
	}

	public static String getPropImagesPath() {
		return getPropImagesPath;
	}

	public static String getScriptPath() {
		return strJavascriptPath;
	}

	public static String getSmtpHost() {
		return strSmtpHost;
	}

	public static String getHelpFilePath() {
		return strJavascriptPath;
	}

	public static String getLogFile() {
		return strGlobalLogFile;
	}

	public static String getSitemanFile() {
		return strSitePath;
	}

	public static String getPropertyPath() {
		return strPropPath;
	}

	public static String getURL() {
		return strURL;
	}

	public static String getURL1() {
		return strURL1;
	}

	public static String getWebPort() {
		return webPort;
	}

	public static Connection getConnection() {
		Connection con = null;
		try {
			/*
			 * ctx = ResUtil.getContext(); System.out.println("--------"+ctx);
			 * System.out.println("--------"+ResUtil.getDSN()); ds =
			 * (DataSource)ctx.lookup("java:/MSSQLDS"); con =
			 * ds.getConnection();
			 */
			ctx = new InitialContext();
			ds = (javax.sql.DataSource) ctx.lookup("java:/MSSQLDS");
			con = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("From getConnection catch block: " + e.getMessage());
		}
		return con;
	}

	public static Connection getCCAVConnection() {
		Connection con = null;
		try {
			ctx = ResUtil.getContext();
			ds = (DataSource) ctx.lookup(ResUtil.getCCAVDSN());
			con = ds.getConnection();
		} catch (Exception e) {
			logger.info("From getCCAVConnection catch block: " + e.getMessage());
		}
		return con;
	}

	public static String getCCAVConnection(String rr) {
		return "RETURNED";
	}

	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			logger.info("From closeConnection catch block: " + e.getMessage());
		}
	}

	public static String getStrServerIp() {
		return strServerIp;
	}

	public static String setPropImagesPathBooking() {
		return strPropImagesPathBooking;
	}

	public static String getSchemeTypes() {
		return schemeTypes;
	}

	public static String getCurrDateTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String strCurDate = df.format(date);
		return strCurDate;
	}

	public static String getTcAesKey() {
		return tcAesKey;
	}

	public static String getAlphabeticalMonth(int month) {
		String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		return months[month];
	}

	// STATIC VARIABLES
	private static String strRootPath = "";
	private static String strCssPath = "";
	private static String strImagesPath = "";
	private static String strImageUploadPath = "";
	private static String strTemplateImageUploadPath = "";
	private static String strTemplateCssPath = "";
	private static String strPropImagesPath = "";
	private static String strPropImagesPathBooking = "";
	private static String strPropCssPath = "";
	private static String PropCssPath = "";
	private static String getPropImagesPath = "";
	private static String strKeyPath = "";
	private static String strKekPath = "";
	private static String strJdbcPath = "";
	private static String strCCJdbcPath = "";
	private static String strLogFilePath = "";
	private static String strGlobalLogFile = "";
	private static String strJavascriptPath = "";
	private static String strInitialCtxFactory = "";
	private static String strServerIp = "";
	private static String strPropPath = "";

	private static String strDevList = "";
	private static String strBccList = "";
	private static String strServiceList = ""; // Syed 9/6/2007

	private static String strJndiPort = "";
	private static String strJndiUrlPkgs = "";
	private static String strSmtpHost = "";
	private static String strSitePath = "";
	private static String pid = "";
	private static String propID = "";
	private static String propMonth = "";
	private static String propDate = "";
	private static StringBuffer regCode = null;
	private static char oneChar = ' ';
	private static Hashtable numAlpha = null;
	private static char[] dst = null;
	private static int charLen = 0;

	static RandomAccessFile u_RndAccFile = null;
	Properties dbProps;
	static int count = 1;
	static int count1 = 1;
	private static ResUtil instance;
	private static Context ctx = null;
	public static DataSource ds = null;
	private static String strLogFolder = "";
	private static String strXmlFolder = "";
	private static String strURL = "";
	private static String strURL1 = "";
	private static String resServiceTax = null;
	private static String schemeTypes = "";
	private static String strXlsFilePath = "";
	private static String webPort = null;
	// SMS API
	private static boolean msgStatus = false;
	private static final String REQUESTMETHOD = "POST";

	static {
		if (instance == null)
			instance = new ResUtil();
	}

	public static void main(String... strings) {
		try {

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String encryptRegular(String regularExpersion) throws Exception {
		String encStr = "";
		synchronized ("") {
			try {
				TripleDESsecurity tds = new TripleDESsecurity("new");
				encStr = tds.encrypt(regularExpersion);
				tds = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(" Encryption Failed. " + e);
			}
		}
		return encStr;
	}

	public static String getProperty(String pProperty) {

		if (vPropertyMap.containsKey(pProperty)) {
			return vPropertyMap.get(pProperty);
		}

		Properties vProps = new Properties();
		System.out.println("PROPERTY : " + pProperty);
		ClassLoader loader = ResUtil.class.getClassLoader();
		InputStream in = loader.getResourceAsStream("com/resavenue/mars/properties/resavenue.properties");
		try {
			vProps.load(in);

			if (vProps.containsKey(pProperty)) {
				vPropertyMap.put(pProperty, vProps.getProperty(pProperty));
				return vProps.getProperty(pProperty);
			}
		} catch (NumberFormatException e) {
			logger.error(
					"Invalid number found while reading Service TAX value from properties file ApplicationResources.properties");
		} catch (IOException e) {
			logger.error("Unable to read properties file ApplicationResources.properties");
		}
		return "";
	}

	public static String getProperty(String pPropertyFileName, String pProperty) {
		Properties vProps = new Properties();
		ClassLoader loader = ResUtil.class.getClassLoader();
		InputStream in = loader
				.getResourceAsStream("com/resavenue/mars/properties/" + pPropertyFileName + ".properties");
		try {
			vProps.load(in);

			if (vProps.containsKey(pProperty)) {
				return vProps.getProperty(pProperty);
			}
		} catch (NumberFormatException e) {
			logger.error("Invalid number found while reading Service TAX value from properties file  "
					+ pPropertyFileName + ".properties");
		} catch (IOException e) {
			logger.error("Unable to read properties file " + pPropertyFileName + ".properties");
		}
		return "";
	}

	public static boolean sendMobileSMS(String orderID, String prop_id, String contactNum, String URL, String pName,
			String currency, String amount, String expiry, String ipaddr, String uname, String flag)
			throws IOException {

		logger.info("Inside sendMobileSMS  =============================================   " + orderID + " prop_id: "
				+ prop_id + "Url " + URL);
		Integer vResponseCode = 0;
		boolean msgStatus = false;
		String userName = "avenues";
		String password = "zerb5rv";
		String smsMobNo = contactNum;
		String url1 = "";
		String message = null;
		// String message="GRT HOTEL Reservation Alert::R13#MD1802#1864 for INR
		// 500. Pls
		// check Resavenue M.A.R.s panel fordetails.";
		Connection con = null;
		try {
			/*
			 * CallableStatement cstmt = null; boolean result = false; ResultSet
			 * rs = null; String hotelInfo[][] = null; String err_flag = null;
			 * String errMsg = null; con = ResUtil.getConnection(); cstmt =
			 * con.prepareCall("{call pr$mobileMsgData(?,?,?,?)}");
			 * logger.info("call pr$mobileMsgData(" + orderID + "," + prop_id +
			 * ")}"); cstmt.setString(1, orderID); cstmt.setString(2, prop_id);
			 * cstmt.setString(3, ""); cstmt.setString(4, ""); result =
			 * cstmt.execute();
			 */
			// You have received an Invoice from %%HotelName%%. Click on
			// %%tinyurl%% to make
			// payment of %%Curr%% %%Invoice_Amount% before the %%Date%%.

			URL url = new URL("https://tinyurl.com/api-create.php?url=" + URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != 200) {
				logger.info("Failed : HTTP error code : " + conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
				logger.info(output + " this is the tinyurl");
				url1 = output;
			}
			conn.disconnect();

			if (flag.equalsIgnoreCase("invoice")) {

				logger.info("Inside sendMobileSMS  =============================================   " + orderID
						+ " prop_id: " + prop_id + "Url " + URL + "Tinney URRRRRRRL " + url1);

				message = "You have received an Invoice from " + pName + ". Click on " + url1 + " to make payment of "
						+ amount + " before the " + expiry + "." + "Rgds ResAvenue";
				// message="You have received an Invoice from "+pName+". Click
				// on "+URL+" to make payment of "+amount+" before the
				// "+expiry+"."+"Rgds ResAvenue"; old
			} else if (flag.equalsIgnoreCase("RoomInoiceExpiryAlertMsg")) {
				message = "You have received an Invoice from " + pName + ". Click on " + url1 + " to make payment of "
						+ amount + " before the " + expiry + "." + "Rgds ResAvenue";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResUtil.closeConnection(con);
		}
		try {
			if (!ResUtil.checkNull(smsMobNo).equalsIgnoreCase("")) {
				URL url;
				HttpURLConnection httpConnection = null;

				String temp = "";
				String content = "";
				content = "username=" + userName + "&password=" + password;
				// String requestedURL = readProperty("requesturl");
				String requestedURL = "http://sms6.routesms.com:Port:8080/bulksms?";

				String messageTest = URLEncoder.encode(message, "UTF-8");

				logger.info("messageTest ====== AFTER convesrion  " + messageTest);
				// System.out.println(messageTest);
				// requestedURL
				// ="http://sms6.routesms.com/bulksms/bulksms?username=avenues&password=zerb5rv&type=0&dlr=1&destination=919820096111&source=CCAVEN&message=GRTHOTEL%20Reservation::%20R13hashMD1802hash1864%20for%20INR%20500%20.%20Pls%20check%20Resavenue%20M.A.R.s%20panel%20for
				// details.";
				requestedURL = "http://sms6.rmlconnect.net/bulksms/bulksms?username=avenues&password=zerb5rv&type=0&entityid=1601100000000000444&tempid=1007240993938352647&dlr=1&destination="
						+ smsMobNo + "&source=RESAVE&message=" + messageTest;
				// System.out.println(requestedURL);
				url = new URL(requestedURL);
				URLConnection connection = url.openConnection();
				httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod(REQUESTMETHOD);
				httpConnection.connect();
				InputStream in = httpConnection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				while ((temp = reader.readLine()) != null) {
					String[] vResponse = temp.split("\\|");
					vResponseCode = Integer.parseInt(vResponse[0].toString().trim());
					logger.info("response code for Invoice No" + orderID + ": " + vResponseCode);

					if (vResponseCode == 1701) {
						msgStatus = true;
						mobSMSLOGS(orderID, prop_id, "true", ipaddr, uname);
					}
				}
			} else {
				logger.info("Mobile Number is not Avilable for property  " + prop_id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgStatus;

	}

	public static boolean sendInternationalSMS(String orderID, String prop_id, String contactNum, String URL,
			String pName, String currency, String amount, String expiry, String ipaddr, String uname) {

		logger.info("Inside Resutil.sendInternationalSMS() method... ");
		String vParam = "", vResponseCode = "", mStr = null;
		String userName = "";
		String password = "";
		String requestedURL = "";
		String content = "";
		boolean msgStatus = false;

		String msgText = "You have received an Invoice from " + pName + ". Click on " + URL + " to make payment of "
				+ currency + " " + amount + " before the " + expiry + ".";
		try {
			userName = "avenues2";
			password = "avn01";
			/* requestedURL = "http://94.56.94.242/api/api_http.php?"; */
			requestedURL = "http://213.132.34.107/api/api_http.php?";
			String message = URLEncoder.encode(msgText, "UTF-8");
			content = "username=" + userName + "&password=" + password;
			requestedURL = requestedURL + content + "&senderid=SMS%20Info&to=" + contactNum + "&text=" + message
					+ "&route=6564-Du&type=text";

			URL mUrl;
			URLConnection mUrlConn = null;
			DataOutputStream mDOS = null;
			mUrl = new URL(requestedURL);
			mUrlConn = (HttpURLConnection) mUrl.openConnection();
			mUrlConn.setDoInput(true);
			mUrlConn.setDoOutput(true);
			mUrlConn.setUseCaches(false);
			mDOS = new DataOutputStream(mUrlConn.getOutputStream());
			mDOS.writeBytes(vParam);
			BufferedReader vBuffReader = new BufferedReader(new InputStreamReader(mUrlConn.getInputStream()));
			while (null != ((mStr = vBuffReader.readLine()))) {
				vResponseCode += mStr;
			}
			vBuffReader.close();
			if (vResponseCode.contains("OK")) {
				logger.info("SMS sent successfuly ! Response received: " + vResponseCode);
				msgStatus = true;
				mobSMSLOGS(orderID, prop_id, "true", ipaddr, uname);
			} else {
				logger.info("SMS Failed for : " + contactNum + ". Response received : " + vResponseCode);
			}
			mDOS.close();

		} catch (Exception e) {
			logger.info("Exception while sending SMS: ", e);
		}
		return msgStatus;
	}

	public static boolean sendInvoiceConfirmationSMS(String resverationNum, String prop_id, String custMercFlag)
			throws IOException {
		// System.out.println("sms method called " + resverationNum + " prop_id
		// = " +prop_id);
		Integer vResponseCode = 0;
		String userName = "avenues";
		String password = "zerb5rv";
		String smsMobNo = "";
		boolean msgStatus = false;
		logger.info("sendMobileSMSInvoice called ");
		String message = null;
		// String message="GRT HOTEL Reservation Alert::R13#MD1802#1864 for INR
		// 500. Pls
		// check Resavenue M.A.R.s panel fordetails.";
		Connection con = null;
		try {
			CallableStatement cstmt = null;
			boolean result = false;
			ResultSet rs = null;
			con = ResUtil.getConnection();
			cstmt = con.prepareCall("{call pr$mobileMsgData_INVOICE(?,?,?,?)}");
			cstmt.setString(1, ResUtil.checkNull(resverationNum));
			cstmt.setString(2, ResUtil.checkNull(prop_id));
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			result = cstmt.execute();
			if (result) {
				rs = cstmt.getResultSet();
				while (rs.next()) {
					if (custMercFlag.equalsIgnoreCase("merc")) {
						message = "Payment of " + rs.getString(5) + " " + rs.getString(4) + " for " + rs.getString(3)
								+ " has been received.Pls check Resavenue CRS panel for more details.";
						smsMobNo = ResUtil.checkNull(rs.getString(2));
					} else if (custMercFlag.equalsIgnoreCase("cust")) {
						message = "Your payment to the " + ResUtil.checkNull(rs.getString(1)) + ","
								+ ResUtil.checkNull(rs.getString(6)) + " of " + ResUtil.checkNull(rs.getString(5)) + " "
								+ ResUtil.checkNull(rs.getString(4)) + " for invoice "
								+ ResUtil.checkNull(rs.getString(3)) + " is successful.";
						smsMobNo = ResUtil.checkNull(rs.getString(7));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResUtil.closeConnection(con);
		}
		try {
			if (!ResUtil.checkNull(smsMobNo).equalsIgnoreCase("")) {
				URL url;
				HttpURLConnection httpConnection = null;

				String temp = "";
				String content = "";
				content = "username=" + userName + "&password=" + password;
				// String requestedURL = readProperty("requesturl");
				String requestedURL = "http://sms6.routesms.com:Port:8080/bulksms?";

				String messageTest = URLEncoder.encode(message, "UTF-8");
				logger.info("message " + message);
				// requestedURL
				// ="http://sms6.routesms.com/bulksms/bulksms?username=avenues&password=zerb5rv&type=0&dlr=1&destination=919820096111&source=CCAVEN&message=GRTHOTEL%20Reservation::%20R13hashMD1802hash1864%20for%20INR%20500%20.%20Pls%20check%20Resavenue%20M.A.R.s%20panel%20for
				// details.";
				requestedURL = "http://sms6.rmlconnect.net/bulksms/bulksms?username=avenues&password=zerb5rv&type=0&entityid=1601100000000000444&tempid=1007178733216917651&dlr=1&destination="
						+ smsMobNo + "&source=RESAVE&message=" + messageTest;
				url = new URL(requestedURL);
				URLConnection connection = url.openConnection();
				httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod(REQUESTMETHOD);
				httpConnection.connect();
				InputStream in = httpConnection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				while ((temp = reader.readLine()) != null) {
					String[] vResponse = temp.split("\\|");
					vResponseCode = Integer.parseInt(vResponse[0].toString().trim());
					logger.info("Confirmation SMS Response code for Invoice no " + resverationNum
							+ " having contact no " + smsMobNo + ": " + vResponseCode);
					// System.out.println("response code >>>>>>>>>>>>>>>" +
					// vResponseCode);

					if (vResponseCode == 1701) {
						msgStatus = true;
						mobSMSLOGS(resverationNum, prop_id, "true", "", "");
					}
				}
			} else {
				logger.info("Mobile Number is not Avilable for property  " + prop_id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgStatus;
	}

	public static boolean sendInternationalConfirmationSMS(String resverationNum, String prop_id, String custMercFlag)
			throws IOException {

		logger.info("Inside ResUtil : sendInternationalConfirmationSMS ");
		String vResponseCode = null;
		String userName = "avenues";
		String password = "zerb5rv";
		String smsMobNo = "";
		String vParam = "", mStr = null;
		String requestedURL = "";
		String content = "";
		boolean msgStatus = false;

		String msgText = null;
		Connection con = null;
		try {
			CallableStatement cstmt = null;
			boolean result = false;
			ResultSet rs = null;
			con = ResUtil.getConnection();
			cstmt = con.prepareCall("{call pr$mobileMsgData_INVOICE(?,?,?,?)}");
			cstmt.setString(1, ResUtil.checkNull(resverationNum));
			cstmt.setString(2, ResUtil.checkNull(prop_id));
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			result = cstmt.execute();
			if (result) {
				rs = cstmt.getResultSet();
				while (rs.next()) {
					if (custMercFlag.equalsIgnoreCase("merc")) {
						msgText = "Payment of " + rs.getString(5) + " " + rs.getString(4) + " for " + rs.getString(3)
								+ " has been received.Pls check Resavenue CRS panel for more details.";
						smsMobNo = ResUtil.checkNull(rs.getString(2));
					} else if (custMercFlag.equalsIgnoreCase("cust")) {
						msgText = "Your payment to the " + ResUtil.checkNull(rs.getString(1)) + ","
								+ ResUtil.checkNull(rs.getString(6)) + " of " + ResUtil.checkNull(rs.getString(5)) + " "
								+ ResUtil.checkNull(rs.getString(4)) + " for invoice "
								+ ResUtil.checkNull(rs.getString(3)) + " is successful.";
						smsMobNo = ResUtil.checkNull(rs.getString(7));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResUtil.closeConnection(con);
		}
		try {
			userName = "avenues2";
			password = "avn01";
			/* requestedURL = "http://94.56.94.242/api/api_http.php?"; */
			requestedURL = "http://213.132.34.107/api/api_http.php?";
			String message = URLEncoder.encode(msgText, "UTF-8");
			content = "username=" + userName + "&password=" + password;
			requestedURL = requestedURL + content + "&senderid=ccavenue&to=" + smsMobNo + "&text=" + message
					+ "&route=6564-Du&type=text";

			URL mUrl;
			URLConnection mUrlConn = null;
			DataOutputStream mDOS = null;
			mUrl = new URL(requestedURL);
			mUrlConn = (HttpURLConnection) mUrl.openConnection();
			mUrlConn.setDoInput(true);
			mUrlConn.setDoOutput(true);
			mUrlConn.setUseCaches(false);
			mDOS = new DataOutputStream(mUrlConn.getOutputStream());
			mDOS.writeBytes(vParam);
			BufferedReader vBuffReader = new BufferedReader(new InputStreamReader(mUrlConn.getInputStream()));
			while (null != ((mStr = vBuffReader.readLine()))) {
				vResponseCode += mStr;
			}
			vBuffReader.close();
			if (vResponseCode.contains("OK")) {
				logger.info("Invoice Confirmation SMS sent successfuly ! Response received: " + vResponseCode);
				msgStatus = true;
				mobSMSLOGS(resverationNum, prop_id, "true", "", "");
			} else {
				logger.info(
						"Invoice Confirmation SMS Failed for : " + smsMobNo + ". Response received : " + vResponseCode);
			}
			mDOS.close();

		} catch (Exception e) {
			logger.info("Exception while sending SMS: ", e);
		}
		return msgStatus;
	}

	public static void mobSMSLOGS(String resv_no, String prop_id, String sent_status, String ipaddr, String uname) {
		int count = 0;
		Connection con = null;
		try {

			CallableStatement cstmt = null;
			int result = 0;
			ResultSet rs = null;
			String hotelInfo[][] = null;
			String err_flag = null;
			String errMsg = null;
			con = ResUtil.getConnection();
			logger.info("exec pr$SMSLOGS '" + resv_no + "','" + prop_id + "','" + sent_status + "','" + ipaddr + "','"
					+ uname + "'");
			cstmt = con.prepareCall("{call pr$SMSLOGS(?,?,?,?,?)}");
			cstmt.setString(1, resv_no);
			cstmt.setString(2, prop_id);
			cstmt.setString(3, sent_status);
			cstmt.setString(4, ipaddr);
			cstmt.setString(5, uname);
			result = cstmt.executeUpdate();
			if (result >= 1) {
				ResUtil.printLog("transaction", "MESSAGE STORE IN MESSAGE LOG TABLE  >>>>>>>>>>>>>>> FOR " + resv_no);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			ResUtil.closeConnection(con);
		}
	}

	public static String decryptCardNumber(String cardNumber) throws Exception {
		String cardNo = "";
		synchronized ("") {
			try {
				TripleDESsecurity tds = new TripleDESsecurity();
				cardNo = tds.cardNumberDecrypt(cardNumber);
				tds = null;
			} catch (Exception e) {
				throw new Exception("Card decryption Failed. " + e);
			}
		}
		return cardNo;
	}

	public static String decryptCardNumberWthNewKey(String cardNumber) throws Exception {
		String cardNo = "";
		synchronized ("") {
			try {
				TripleDESsecurity tds = new TripleDESsecurity("new");
				cardNo = tds.cardNumberDecrypt(cardNumber);
				tds = null;
			} catch (Exception e) {
				throw new Exception("Card decryption Failed. " + e);
			}
		}
		return cardNo;
	}

	/*
	 * public static String old_encryptCardNumber(String cardNumber) throws
	 * Exception{ String encCardNo = ""; synchronized(""){ try{ encCardNo =
	 * SecurityUtil.getInstance().encryptCardNo(cardNumber); }catch(Exception
	 * e){ throw new Exception("Card Encryption Failed. "+e); } } return
	 * encCardNo; }
	 */

	public static String decryptRegular(String str) throws Exception {
		String encStr = "";
		synchronized ("") {
			try {
				TripleDESsecurity tds = new TripleDESsecurity("new");
				encStr = tds.cardNumberDecrypt(str);
				tds = null;
			} catch (Exception e) {
				throw new Exception("Card decryption Failed. " + e);
			}
		}
		return encStr;
	}

	public static String encryptCardNumber(String cardNumber) throws Exception {
		String encCardNo = "";
		synchronized ("") {
			try {
				TripleDESsecurity tds = new TripleDESsecurity();
				encCardNo = tds.cardNumberEncrypt(cardNumber);
				tds = null;
			} catch (Exception e) {
				throw new Exception("Card Encryption Failed. " + e);
			}
		}
		return encCardNo;
	}

	public static String encryptCardNumberWthNewKey(String cardNumber) throws Exception {
		String encCardNo = "";
		synchronized ("") {
			try {
				TripleDESsecurity tds = new TripleDESsecurity("new");
				encCardNo = tds.cardNumberEncrypt(cardNumber);

				tds = null;
			} catch (Exception e) {
				throw new Exception("Card Encryption Failed. " + e);
			}
		}
		return encCardNo;
	}

	public static String getChecksum(String MerchantId, String OrderId, String Amount, String redirectUrl,
			String WorkingKey) {
		String str = null;
		Adler32 adl = null;
		try {
			str = MerchantId + "|" + OrderId + "|" + Amount + "|" + redirectUrl + "|" + WorkingKey;
			adl = new Adler32();
			adl.update(str.getBytes());
		} catch (Exception e) {
			// System.out.println("payment.log","----------Error while
			// generating Checksum : "+e);
		}
		return String.valueOf(adl.getValue());
	}

	public static String verifyChecksum(String MerchantId, String OrderId, String Amount, String AuthDesc,
			String WorkingKey, String CheckSum) throws Exception {
		String str = MerchantId + "|" + OrderId + "|" + Amount + "|" + AuthDesc + "|" + WorkingKey;

		Adler32 adl = new Adler32();
		adl.update(str.getBytes());
		long adler = adl.getValue();

		String newChecksum = String.valueOf(adl.getValue());
		return (newChecksum.equals(CheckSum)) ? "true" : "false";

	}

	public static String setAvenueChecksum(String trackingId, String trans_amt, String mCurrency) throws Exception {
		String str = trackingId + "|" + trans_amt + "|" + mCurrency + "|" + mWorkingKey;
		Adler32 adl = new Adler32();
		adl.update(str.getBytes());
		return String.valueOf(adl.getValue());
	}

	public static String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month.toUpperCase();
	}

	public static int monthsGap(Date startDate, Date endDate) {
		int diffMonth = 0;
		try {
			Calendar startCalendar = new GregorianCalendar();
			startCalendar.setTime(startDate);
			Calendar endCalendar = new GregorianCalendar();
			endCalendar.setTime(endDate);
			int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
			System.out.println("diffYear...." + diffYear);
			diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
			System.out.println("diffMonth...." + diffMonth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diffMonth;
	}

	public static String getIPAddress(String mRoot) {
		String hostAddress = "";
		try {
			int charCount = 0;
			for (int i = 0; i < mRoot.length(); i++) {
				if (mRoot.charAt(i) == ':') {
					charCount++;
				}
			}

			if (mRoot.contains("http") && charCount > 1)
				hostAddress = mRoot.substring(mRoot.lastIndexOf("/") + 1, mRoot.lastIndexOf(":"));
			else if (mRoot.contains("http") && charCount == 1)
				hostAddress = mRoot.substring(mRoot.lastIndexOf("/") + 1, mRoot.length());
			else if (charCount == 1)
				hostAddress = mRoot.substring(0, mRoot.lastIndexOf(":"));
			else if (charCount == 0)
				hostAddress = mRoot.substring(0, mRoot.length());
		} catch (Exception e) {
			logger.info("Exception in ResUtil.getDecryptedMCPGParam()" + e);
		}
		return hostAddress;
	}

	// For BulkInvoice
	public static String[][] isMsgService(String prop_id) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isSMS = false;
		String flag = "";
		String[][] serviceList = null;
		try {
			con = ResUtil.getConnection();
			stmt = con.prepareStatement("select pc_config_name, pc_config_value from res_prop_configs where prop_id='"
					+ prop_id + "' and pc_config_name in('RSMS','RIRD','RIAD','IVRS','PPINV')");
			rs = stmt.executeQuery();

			if (rs != null) {
				serviceList = getData(rs);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			ResUtil.closeConnection(con);
			rs = null;
		}
		return serviceList;
	}

	public static String[][] getData(ResultSet rs) {
		String data[][] = null;
		ResultSetMetaData rsm = null;
		int column_cnt = 0;
		int rowcounter = 0;
		int columncounter = 0;
		ArrayList<String> records;

		try {
			rsm = rs.getMetaData();
			column_cnt = rsm.getColumnCount();
			records = new ArrayList<String>();
			// entering data into the Vector
			while (rs.next()) {
				columncounter = 0;
				for (int k = 1; k <= column_cnt; k++) {
					records.add("" + rs.getString(k));
					columncounter++;
				}
				rowcounter++;
			}
			data = new String[rowcounter][column_cnt];// starting to insert the
														// data into the
														// staring.
			rowcounter = 0;
			for (int cnt = 0; cnt < records.size(); cnt += column_cnt) {
				for (int k = 0; k < column_cnt; k++) {
					data[rowcounter][k] = "" + records.get(cnt + k);
					columncounter++;
				}
				rowcounter++;
			}
		} catch (SQLException sqle) {
			logger.info("SQLException in catch block of DataAccess.getData()  :" + sqle);
			try {
				rs.close();
			} catch (SQLException e) {
				logger.info("SQLException: while closing rs in DataAccess.java: closing resultSet :" + e);
			}
		} catch (Exception sqle) {
			logger.info("Exception in catch block of DataAccess.getData()  :" + sqle);
			try {
				rs.close();
			} catch (SQLException e) {
				logger.info("SQLException: while closing rs in DataAccess.java: closing resultSet :" + e);
			}
		} finally {
			records = null;
		}
		return data;
	}

	public static String[] getPropCode(String regCode) {
		String[] props = new String[3];
		try {

			numAlpha = null;
			numAlpha = new Hashtable();
			numAlpha.put("A", "0");
			numAlpha.put("C", "1");
			numAlpha.put("E", "2");
			numAlpha.put("G", "3");
			numAlpha.put("I", "4");
			numAlpha.put("Z", "5");
			numAlpha.put("X", "6");
			numAlpha.put("V", "7");
			numAlpha.put("T", "8");
			numAlpha.put("R", "9");

			propID = regCode.substring(0, regCode.length() - 4);
			propMonth = regCode.substring(regCode.length() - 4);
			propDate = propMonth.substring(2);
			propMonth = propMonth.substring(0, 2);

			props[1] = propMonth;
			props[2] = propDate;

			StringBuffer prop = new StringBuffer();
			prop.append("PROP");
			for (int p = 0; p < propID.length(); p++) {
				oneChar = propID.charAt(p);
				prop.append(numAlpha.get("" + oneChar));
			}

			propID = prop.toString();
			props[0] = propID;
			prop = null;
			numAlpha = null;

		} catch (Exception e) {
			// System.out.println("Arrayindex out of bound exception");
		}
		return props;
	}

	// Added by mrinal singh
	public static int getNoOfNights(String startDate, String endDate) {
		int diffDays = (int) java.time.Duration.between(LocalDate.parse(startDate), LocalDate.parse(endDate)).toDays();
		return diffDays;
	}

	// Added for Schedular

	public static void insertFailedMails(String mInvoiceNo, String mailFrom, String mailTo, String mailDate,
			String reservationType, String isCustomer, String isHotelier, String isService, String propId,
			String mailType, String mApplUser, String mRegId, String checksum, String propMonth, String propDate,
			String trackingId) {
		// System.out.println("Inside ResUtil :: insertFailedMails()");
		logger.info("Inside ResUtil :: insertFailedMails()");
		ResUtil.printLog("scrap_mail", "Inside ResUtil :: insertFailedMails()");
		boolean result = false;
		ResultSet dbResultset = null;
		String vError = null;
		String vDesc = null;
		String vFlag = null;
		Connection con = null;
		CallableStatement cstmt = null;

		try {
			logger.info("Exec pr$failed_mail_ins '" + mInvoiceNo + "','" + mailFrom + "','" + mailTo + "','" + mailDate
					+ "','" + reservationType + "','" + isCustomer + "','" + isHotelier + "','" + isService + "','"
					+ propId + "','" + mailType + "','" + mApplUser + "','" + mRegId + "','" + checksum + "','"
					+ propMonth + "','" + propDate + "','" + trackingId + "'  ");
			ResUtil.printLog("scrap_mail",
					"Exec pr$failed_mail_ins '" + mInvoiceNo + "','" + mailFrom + "','" + mailTo + "','" + mailDate
							+ "','" + reservationType + "','" + isCustomer + "','" + isHotelier + "','" + isService
							+ "','" + propId + "','" + mailType + "','" + mApplUser + "','" + mRegId + "','" + checksum
							+ "','" + propMonth + "','" + propDate + "','" + trackingId + "'  ");
			con = ResUtil.getConnection();
			cstmt = con.prepareCall("{call pr$failed_mail_ins(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			cstmt.setString(1, mInvoiceNo);
			cstmt.setString(2, mailFrom);
			cstmt.setString(3, mailTo);
			cstmt.setString(4, mailDate);
			cstmt.setString(5, reservationType);
			cstmt.setString(6, isCustomer);
			cstmt.setString(7, isHotelier);
			cstmt.setString(8, isService);
			cstmt.setString(9, propId);
			cstmt.setString(10, mailType);
			cstmt.setString(11, mApplUser);
			cstmt.setString(12, mRegId);
			cstmt.setString(13, checksum);
			cstmt.setString(14, propMonth);
			cstmt.setString(15, propDate);
			cstmt.setString(16, trackingId);
			cstmt.registerOutParameter(17, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(18, java.sql.Types.VARCHAR);
			result = cstmt.execute();

			if (result = true) {
				dbResultset = cstmt.getResultSet();
				while (dbResultset.next()) {
					vFlag = dbResultset.getString("result");
				}
				vError = cstmt.getString(17);
				vDesc = cstmt.getString(18);

			}
			// System.out.println("vFlag="+ vFlag);
		} catch (SQLException sqle) {
			logger.info("SQL Exception from Reports insertFailedMails: " + sqle);
			ResUtil.printLog("scrap_mail", "SQL Exception from Reports insertFailedMails: " + sqle);

		} catch (Exception e) {
			logger.info("Exception from Reports insertFailedMails: " + e);
			ResUtil.printLog("scrap_mail", "Exception from Reports insertFailedMails: " + e);

		} finally {
			try {
				ResUtil.closeConnection(con);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static ArrayList<ResavenueMailAuthBean> fetchMailLogsDetails() {

		logger.info("Inside ResUtil :: fetchMailLogsDetails()");
		ResUtil.printLog("scrap_mail", "Inside ResUtil :: fetchMailLogsDetails()");
		boolean result = false;
		ResultSet dbResultset = null;
		String vError = null;
		String vDesc = null;
		ArrayList<ResavenueMailAuthBean> mailList = new ArrayList();

		Connection con = null;
		CallableStatement cstmt = null;

		try {
			// logger.info("Exec pr$fetch_mail_logs_Details'" + "','"+ "' ");
			logger.info("Exec pr$fetch_mail_logs_Details'" + "','" + "' ");
			ResUtil.printLog("scrap_mail", "Exec pr$fetch_mail_logs_Details'" + "','" + "' ");
			con = ResUtil.getConnection();
			cstmt = con.prepareCall("{call pr$fetch_mail_logs_Details(?, ?)}");
			cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
			result = cstmt.execute();

			if (result = true) {

				dbResultset = cstmt.getResultSet();

				while (dbResultset.next()) {

					mailList.add(new ResavenueMailAuthBean(dbResultset.getString("mInvoiceNo"),
							dbResultset.getString("mailFrom"), dbResultset.getString("mailTo"),
							dbResultset.getString("mailDate"), dbResultset.getString("reservationType"),
							dbResultset.getString("propId"), dbResultset.getString("mailType"),
							dbResultset.getString("mApplUser"), dbResultset.getString("mRegId"),
							dbResultset.getString("checksum"), dbResultset.getString("propMonth"),
							dbResultset.getString("propDate"), dbResultset.getString("trackingId"),
							dbResultset.getString("invoice_type")));

				}
				vError = cstmt.getString(1);
				vDesc = cstmt.getString(2);

			}

		} catch (SQLException sqle) {
			logger.info("SQL Exception from Reports fetchMailLogsDetails: " + sqle);
			ResUtil.printLog("scrap_mail", "SQL Exception from Reports fetchMailLogsDetails: " + sqle);

		} catch (Exception e) {
			logger.info("Exception from Reports fetchMailLogsDetails: " + e);
			ResUtil.printLog("scrap_mail", "Exception from Reports fetchMailLogsDetails: " + e);

		} finally {
			try {
				ResUtil.closeConnection(con);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return mailList;

	}

	public static ArrayList<ResavenueMailAuthBean> fetchMailLogsDetailsExpiry(String fireTime) {
		// System.out.println("Inside ResUtil :: fetchMailLogsDetails()");
		logger.info("Inside ResUtil :: fetchMailLogsDetailsExpiry()");
		ResUtil.printLog("scrap_mail", "Inside ResUtil :: fetchMailLogsDetailsExpiry()");
		boolean result = false;
		ResultSet dbResultset = null;
		String vError = null;
		String vDesc = null;
		ArrayList<ResavenueMailAuthBean> mailList = new ArrayList<>();
		Connection con = null;
		CallableStatement cstmt = null;

		try {
			logger.info("Exec  pr$fetch_mail_logs_Details_exp'" + "','" + "' ");
			ResUtil.printLog("scrap_mail", "Exec  pr$fetch_mail_logs_Details_exp'" + "','" + "' ");
			con = ResUtil.getConnection();
			cstmt = con.prepareCall("{call pr$fetch_mail_logs_Details_exp(?,?,?)}");

			cstmt.setString(1, fireTime.trim());
			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			result = cstmt.execute();
			logger.info("Inside ResUtil :: fetchMailLogsDetailsExpiry() =====  " + result);
			ResUtil.printLog("scrap_mail", "Inside ResUtil :: fetchMailLogsDetailsExpiry() =====  " + result);
			if (result = true) {
				dbResultset = cstmt.getResultSet();
				while (dbResultset.next()) {
					mailList.add(new ResavenueMailAuthBean(dbResultset.getString(1), dbResultset.getString(2),
							dbResultset.getString(3), dbResultset.getString(4), dbResultset.getString(5),
							dbResultset.getString(6), dbResultset.getString(7), dbResultset.getString(8),
							dbResultset.getString(9), dbResultset.getString(10), dbResultset.getString(11),
							dbResultset.getString(12), dbResultset.getString(13), dbResultset.getString(14)));

				}
				vError = cstmt.getString(2);
				vDesc = cstmt.getString(3);

			}

		} catch (SQLException sqle) {
			logger.info("SQL Exception from Reports fetchMailLogsDetails: " + sqle);
			ResUtil.printLog("scrap_mail", "SQL Exception from Reports fetchMailLogsDetails: " + sqle);

		} catch (Exception e) {
			logger.info("Exception from Reports fetchMailLogsDetails: " + e);
			ResUtil.printLog("scrap_mail", "Exception from Reports fetchMailLogsDetails: " + e);

		} finally {
			try {
				ResUtil.closeConnection(con);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		logger.info("Inside ResUtil :: fetchMailLogsDetailsExpiry() on db " + mailList.size());
		ResUtil.printLog("scrap_mail", "Inside ResUtil :: fetchMailLogsDetailsExpiry() on db " + mailList.size());
		return mailList;

	}

	public static String getTemplateType(String propId) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String TemplateType = "";
		// System.out.println(" propid is "+propId);
		try {
			con = ResUtil.getConnection();
			stmt = con.prepareStatement("SELECT  prop_template FROM Res_properties where prop_id='" + propId + "'");
			stmt.execute();
			rs = stmt.getResultSet();
			while (rs.next()) {

				TemplateType = ResUtil.checkNull(rs.getString(1));

			}

		} catch (Exception e) {
			// TODO: handle exception
			ResUtil.printLog(" Exception in ResUtil.getTemplateType(String propId) " + e);
		} finally {
			ResUtil.closeConnection(con);
		}

		return TemplateType;
	}

	/* added by shubham kulkarni to getting dynamic QR code */

	public static byte[] getQRCode(String inputstr, int height, int width) {

		logger.info("------------------ start ResUtil :getQRCode() -------------------------------");
		try {
			logger.info("------------------  ResUtil :getQRCode()  try block -------------------------------");
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(inputstr, BarcodeFormat.QR_CODE, width, height);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			logger.info("------------------  ResUtil :getQRCode()  catch block -------------------------------" + e);

			return null;
		}

	}
	/* added by shubham kulkarni for getting regcode to generate QR code */

	public static String getRegCode(String propID, String monthAndDay) {
		numAlpha = new Hashtable();
		numAlpha.put("0", "A");
		numAlpha.put("1", "C");
		numAlpha.put("2", "E");
		numAlpha.put("3", "G");
		numAlpha.put("4", "I");
		numAlpha.put("5", "Z");
		numAlpha.put("6", "X");
		numAlpha.put("7", "V");
		numAlpha.put("8", "T");
		numAlpha.put("9", "R");

		int charLen = propID.substring(4).length();
		// out.println("charLen : "+charLen);
		char[] dst = new char[charLen];

		// regCode = null;
		StringBuffer regCode = new StringBuffer();
		if (regCode.length() > 0) {
			regCode.delete(0, regCode.length());
		}
		String pid = propID.substring(4);
		pid.getChars(0, pid.length(), dst, 0);
		for (int c = 0; c < dst.length; c++) {
			regCode.append(numAlpha.get("" + dst[c]));
		}

		regCode.append((Integer.parseInt(monthAndDay) < 10) ? ("0" + Integer.parseInt(monthAndDay)) : monthAndDay);

		return regCode.toString();
	}

	/* added by shubham kulkarni to getting tiny url */
	public static String getShortenURL(String vLink) {
		String output = null;
		String tinyURL = null;
		try {

			URL url = new URL("https://tinyurl.com/api-create.php?url=" + ResUtil.checkNull(vLink));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			while ((output = br.readLine()) != null) {
				logger.info(output + " this is the tinyurl");
				tinyURL = ResUtil.checkNull(output);
			}
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResUtil.checkNull(tinyURL);
	}

	public static String convertTo12HourFormat(String timeString) {
		int h1 = (int)timeString.charAt(0) - '0';
	    int h2 = (int)timeString.charAt(1)- '0';	 
	    int hh = h1 * 10 + h2;
	    StringBuilder updatedTimeString = new StringBuilder();
	    // Finding out the Meridien of time
	    // ie. AM or PM
	    String meridien;
	    if (hh < 12) {
	        meridien = " AM";
	    }
	    else
	        meridien = " PM";
	    hh %= 12;
	    if(hh == 0) {
	    	hh = 12;
	    }
	    updatedTimeString.append(hh)
	    .append(timeString.substring(2, 5))
	    				.append(meridien);
	    
	    return updatedTimeString.toString();
	}
}