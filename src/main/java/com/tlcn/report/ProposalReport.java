package com.tlcn.report;

import java.util.HashMap;
import java.util.List;

import com.tlcn.dto.ModelFormProposal;
import com.tlcn.model.Car;
import com.tlcn.model.Proposal;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ProposalReport {
	 public void generatePdfReport(List<ModelFormProposal> proposal) throws JRException {
		 String report = "src/main/resources/report/template/Form_Proposal.jrxml";
		 JasperReport jreport = JasperCompileManager.compileReport(report);
		 JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(proposal);
		 HashMap params = new HashMap();
		 JasperPrint jprint = JasperFillManager.fillReport(jreport, params, ds);
		 
		 JasperExportManager.exportReportToPdfFile(jprint,
	                "src/main/resources/report3.pdf");
	 }
}
