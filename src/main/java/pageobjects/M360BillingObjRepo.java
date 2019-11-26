package pageobjects;

public class M360BillingObjRepo {

	public class M360ManagePayment {
		public static final String m360Menu_xpath = "//*[@id=\'td1\']/a";
		public static final String menuBilling_xpath = "/html/body/form/table/tbody/tr[1]/td/table/tbody/tr[2]/td/table[2]/tbody/tr/td[15]/a";
		public static final String memPaymentsTab_xpath = "/html/body/form/table[2]/tbody/tr/td/table/tbody/tr/td[5]/a";
		public static final String medId_xpath = "//*[@id=\'eemBillPaymentsSearchDiv\']/table/tbody/tr[1]/td[4]/input";
		public static final String goBtn_id = "btnGoGif";
		public static final String resetBtn_xpath = "/html/body/form/table[4]/tbody/tr/td[2]/table/tbody/tr/td[1]/input";
	}

	public class M360BillingInvoice {
		public static final String M360_xpath = "//*[@id='td1']";
		public static final String invoicesTab_xpath = "//table[@id='eemMenu']//ancestor::table[1]//following-sibling::table[1]/tbody//td[text()='Invoices']";
		public static final String BillResetSearch_name = "newBillingSearch";
		public static final String medicareid_xpath = "//*[@id='eemBillInvoiceSearchDiv']/table/tbody/tr[2]/td[2]/input";
		public static final String goButton_id = "btnGoGif";
		public static final String invoiceTypeSearch_xpath = "(//span[contains(@class,'select2 select2-container')])[3]";
		public static final String dropDownInvoiceTypeSearch_xpath = "//*[@id=\"eemBillInvoiceSearchDiv\"]/table/tbody/tr[2]/td[8]/select";
		public static final String dropDownInvoiceTypeSelect_xpath = "(//span[contains(@class,'select2 select2-container')])[3]//li[1]";

		public static final String invoicelist_xpath = "//*[@id='tblSearchResultsList']/tbody/tr";
		public static final String memberId_xpath = "//*[text()='Member Summary']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Member Id')]//following-sibling::td[1]";
		public static final String memberName_xpath = "//*[text()='Member Summary']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Name')]//following-sibling::td[1]";
		public static final String memberInvoice_xpath = "//*[text()='Member Summary']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Invoice')]//following-sibling::td[1]";
		public static final String memberTotalDue_xpath = "//*[text()='Member Summary']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Total Due')]//following-sibling::td[1]";
		public static final String medicareidValue_xpath = "//*[text()='Member Summary']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Medicare ID')]//following-sibling::td[1]";
		public static final String memberPayments_xpath = "//*[text()='Member Summary']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Payments')]//following-sibling::td[1]";
		public static final String memberAdjustments_xpath = "//*[text()='Member Summary']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Adjustments')]//following-sibling::td[1]";

		public static final String invoiceNbr_xpath = "//*[text()='Invoice Header Details']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Invoice Nbr')]//following-sibling::td[1]";
		public static final String invoiceType_xpath = "//*[text()='Invoice Header Details']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Invoice Type')]//following-sibling::td[1]";
		public static final String invoiceDueDate_xpath = "//*[text()='Invoice Header Details']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Due Date')]//following-sibling::td[1]";
		public static final String invoiceBillThruDate_xpath = "//*[text()='Invoice Header Details']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Bill Thru Date')]//following-sibling::td[1]";
		public static final String invoiceStatus_xpath = "//*[text()='Invoice Header Details']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Invoice Status')]//following-sibling::td[1]";
		public static final String invoiceAmt_xpath = "//*[text()='Invoice Header Details']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Invoice Amt')]//following-sibling::td[1]";
		public static final String invoiceAdjustmentAmt_xpath = "//*[text()='Invoice Header Details']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Adjustment Amt')]//following-sibling::td[1]";
		public static final String invoicePaymentAmt_xpath = "//*[text()='Invoice Header Details']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Payment Amt')]//following-sibling::td[1]";
		public static final String invoiceFinalAmt_xpath = "//*[text()='Invoice Header Details']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Final Amt')]//following-sibling::td[1]";
		public static final String invoiceFrequency_xpath = "//*[text()='Invoice Header Details']//ancestor::table[1]//following-sibling::table[1]//*[starts-with(text(),'Frequency')]//following-sibling::td[1]";

		public static final String invoiceDetailsDesc1_xpath = "//*[@id='dtl0']/td[3]";
		public static final String invoiceDetailsDesc2_xpath = "//*[@id='dtl1']/td[3]";
		public static final String invoiceDetailsDesc3_xpath = "//*[@id='dtl2']/td[3]";

		public static final String invoiceDetailsItem_xpath = "//*[@id='invoiceDetailView.itemId']";
		public static final String invoiceDetailsStatus_xpath = "//*[@id='invoiceDetailView.lineStatusDesc']";
		public static final String invoiceDetailsGroup_xpath = "//*[@id='invoiceDetailView.groupName']";
		public static final String invoiceDetailsProduct_xpath = "//*[@id='invoiceDetailView.productName']";
		public static final String invoiceDetailsSubProduct_xpath = "//*[@id='invoiceDetailView.subProductDesc']";
		public static final String invoiceDetailsDescription_xpath = "//*[@id='invoiceDetailView.itemDesc']";
		public static final String invoiceDetailsFunction_xpath = "//*[@id='invoiceDetailView.functionCd']";
		public static final String invoiceDetailsCheck_xpath = "//*[@id='invoiceDetailView.checkNbr']";
		public static final String invoiceDetailsDetailAmt_xpath = "//*[@id='invoiceDetailView.detailAmt']";
		public static final String invoiceDetailsBankCode_xpath = "//*[@id='invoiceDetailView.bankAcctCd']";
		public static final String invoiceDetailsXrefInvoiceNbr_xpath = "//*[@id='invoiceDetailView.xrefInvoiceNbr']";
		public static final String invoiceDetailsPaySource_xpath = "//*[@id='invoiceAdjustmentView']/table/tbody/tr[3]/td[6]";
		public static final String invoiceDetailsPayBatch_xpath = "//*[@id='invoiceAdjustmentView']/table/tbody/tr[3]/td[8]";
		public static final String invoiceDetailsBatchSeq_xpath = "//*[@id='invoiceAdjustmentView']/table/tbody/tr[3]/td[10]";
		public static final String invoiceDetailslist_xpath = "//*[@id='tblDetailList']/tbody/tr";
		public static final String invoiceAdjustment_xpath = "//input[@name='adjustment']";
		public static final String invoiceAdjustmentFunction_xpath = "//select[@name='displayInvoiceDetail.functionCd']";
		public static final String invoiceAdjustmentAmount_xpath = "//input[@name='displayInvoiceDetail.detailAmt']";
		public static final String invoiceAdjustmentReason_xpath = "//select[@name='vvv']";
		public static final String invoiceDetailsAdjustmentBanckCode_xpath = "//input[@name='displayInvoiceDetail.bankAcctCd']";
		public static final String invoiceDetailsAdjustmentInvoiceNbr_xpath = "//input[@name='displayInvoiceDetail.xrefInvoiceNbr']";
		public static final String invoiceAdjustmentRefundReason_xpath = "//select[@name='displayInvoiceDetail.reasonCd']";
		public static final String invoiceAdjustmentDetailAmt_xpath = "//input[@name='displayInvoiceDetail.detailAmt']";
		public static final String invoiceAdjustmentUpdateDtlsBtn_xpath = "//input[@name='updateDetails']";

	}

	public class MemberPaymentDetails {
		public static final String memId_xpath = "//*[@id=\'paymentsDetailView.invoiceId\']";
		public static final String name_xpath = "//*[@id=\'paymentsDetailView.name\']";
		public static final String medicareId_xpath = "//*[@id=\'paymentsDetailView.hicNbr\']";
		public static final String paySource_xpath = "//*[@id=\'paymentsDetailView.paySource\']";
		public static final String payPostedIndicator_xpath = "//*[@id=\'paymentsDetailView.paymentPostedInd\']";
		public static final String payAmount_xpath = "//*[@id=\'paymentsDetailView.paymentAmt\']";

		public static final String memberlist_xpath = "//*[@id='tblDetailList']/tbody/tr[@class='bordered-table']";
		public static final String invoicNum_xpath = "//*[@id='paymentDtlInvoiceData']/table/tbody/tr[2]/td[1]";
		public static final String checkNum_xpath = "//*[@id=\'paymentsDetailView.checkNbr\']";
		public static final String checkDate_xpath = "//*[@id=\'paymentsDetailView.frmtCheckDate\']";
		public static final String invoiceType_xpath = "//*[@id='paymentDtlInvoiceData']/table/tbody/tr[2]/td[2]";
		public static final String dueDate_xpath = "//*[@id='paymentsDetailView.frmtInvoiceDueDate']";
		public static final String invoiceList_xpath = "//*[@id='paymentDtlInvoiceData']/table/tbody/tr";

	}

}
