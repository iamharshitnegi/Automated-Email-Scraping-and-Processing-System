### **Project Title:**
### **Automated Email Scraping and Processing System**
### **Description:**
Developed an automated system to scrape, filter, and process emails from an inbox using the JavaMail API. The system connects to
IMAP mail servers, retrieves messages, and processes both plain text and multi-part emails. It supports the extraction of attachments
and uses keyword-based filtering to categorize and store email data efficiently. Additionally, the project handles various content types
(plain text, HTML, and attachments), ensuring secure and scalable operations through OAuth2 authentication and scheduled execution
for periodic scraping.
### **Key Concepts and Technologies:**
- **JavaMail API:** For connecting to mail servers (IMAP) and fetching emails.
- **IMAP Protocol:** To retrieve emails and manage folders.
- **Multithreading and Scheduling:** To schedule email scraping tasks at regular intervals.
- **Message Parsing:** Extracted email subjects, bodies (plain text and HTML), and attachments.
- **OAuth2 Authentication:** For secure login and access to email services.
- **MIME Handling:** Processed different email content types (plain text, HTML, and multi-part messages).
- **Filtering and Categorization:** Implemented logic to filter emails based on subject, sender, and content for automated processing.
- **Attachment Processing:** Extracted and saved email attachments from multi-part emails.
- **Scheduling:** Automated the periodic execution of email scraping using `ScheduledExecutorService`.