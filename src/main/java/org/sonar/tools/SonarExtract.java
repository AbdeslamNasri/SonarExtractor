/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.SonarClient;
import org.sonar.wsclient.issue.Issue;
import org.sonar.wsclient.issue.IssueChange;
import org.sonar.wsclient.issue.IssueChangeDiff;
import org.sonar.wsclient.issue.IssueClient;
import org.sonar.wsclient.issue.IssueQuery;
import org.sonar.wsclient.issue.Issues;
import org.sonar.wsclient.qualitygate.QualityGate;
import org.sonar.wsclient.qualitygate.QualityGateClient;
import org.sonar.wsclient.qualitygate.QualityGateConditionProject;
import org.sonar.wsclient.qualitygate.QualityGateDetailsProject;
import org.sonar.wsclient.services.Component;
import org.sonar.wsclient.services.ComponentQuery;
import org.sonar.wsclient.services.Project;
import org.sonar.wsclient.services.ProjectAnalyses;
import org.sonar.wsclient.services.ProjectAnalysis;
import org.sonar.wsclient.services.Projects;
import org.sonar.wsclient.services.ProjectsAnalysesQuery;
import org.sonar.wsclient.services.ProjectsQuery;

public class SonarExtract {

	public static void main(String args[]) throws IOException {

		if(args.length<3) {
			usage();
			System.exit(1);
		}
		final String url = args[0];
		final String login = args[1];
		final String password = args[2];

		SonarClient client = SonarClient.builder().url(url).login(login).password(password)

				.build();

		System.out.println("client: " + client.toString());

		IssueQuery query = IssueQuery.create();
		query.severities("CRITICAL", "MAJOR", "MINOR");

		IssueClient issueClient = client.issueClient();
		Issues issues = issueClient.find(query);
		List<Issue> issueList = issues.list();

		String filename = "SonarIssues.xls";
		FileOutputStream fileOut = new FileOutputStream(filename);
		HSSFWorkbook workbook = new HSSFWorkbook();

		createExcel(workbook, "Issue List", issueList);

		QualityGateClient qualityGateClient = client.qualityGateClient();
//		List<QualityGate> gatesList = new ArrayList<>(qualityGateClient.list().qualityGates());

		/*
		 * for(QualityGate qg: gatesList) { qg. }
		 */

		QualityGate qg = qualityGateClient.list().defaultGate();
		createExcelGate(workbook, "Default Gate", qg);

		createExcelChanges(workbook, "Issue Change Log", issueList, issueClient);

		/*
		 * ProjectClient pc = client.projectClient();
		 * 
		 * MetricQuery mq = MetricQuery.all(); Sonar sonar = Sonar.create(url, login,
		 * password); List<Metric> metrics = sonar.findAll(mq);
		 */

//		client.qualityGateClient().selectProject(0, 0);
		ProjectsQuery pq = ProjectsQuery.all();
		Sonar sonar = Sonar.create(url, login, password);
		Projects projs = sonar.find(pq);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -100);

		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String from = simpleDateFormat.format(cal.getTime());

		projs.getProjects().forEach(prj -> {
			ProjectsAnalysesQuery paq = new ProjectsAnalysesQuery(prj.getKey(), from, null, null);
			Sonar sonar2 = Sonar.create(url, login, password);
			prj.setLastAnalysis(null);
			try {
				ProjectAnalyses proja = sonar2.find(paq);
				ProjectAnalysis paa = proja.getLastAnalysis();
				prj.setLastAnalysis(paa);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Set<Project> projectKeys = new HashSet<>();
		projectKeys.addAll(projs.getProjects());

		createExcelMetrics(workbook, "Project Metrics", client, projectKeys);

//*******************************
//		sonar = Sonar.create(url, login, password);
		List<Component> measures = new ArrayList<>();

		projs.getProjects().forEach(prj -> {
			ComponentQuery cq = new ComponentQuery(prj.getKey());
			Sonar sonar2 = Sonar.create(url, login, password);
			Component comp = sonar2.find(cq);
			measures.add(comp);
		});

		createExcelMeasures(workbook, "Project Stats", measures);

		workbook.write(fileOut);
		fileOut.close();
		System.out.println("Your excel file has been generated!");
	}

	private static void createExcelMeasures(HSSFWorkbook workbook, String sheetName, List<Component> comps) {

		try {

			HSSFSheet sheet = workbook.createSheet(sheetName);

			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Project ID");
			rowhead.createCell(1).setCellValue("Project Key");
			rowhead.createCell(2).setCellValue("Project Name");
			rowhead.createCell(3).setCellValue("Qualifier");
			rowhead.createCell(4).setCellValue("Lines of code"); // ncloc
			rowhead.createCell(5).setCellValue("Cyclomatic Complexity "); // complexity
			rowhead.createCell(6).setCellValue("Issues "); // violations
			rowhead.createCell(7).setCellValue("Technical Debt (md) "); // violations
			short num_row = 1;
			for (Component comp : comps) {

				HSSFRow row = sheet.createRow((short) num_row);
				System.out.println("Excel Measures ID: " + comp.getId() + ", Key: " + comp.getKey());
				row.createCell(0).setCellValue(comp.getId());
				row.createCell(1).setCellValue(comp.getKey());
				row.createCell(2).setCellValue(comp.getName());
				row.createCell(3).setCellValue(comp.getQualifier());
				if (comp.getMeasures().size() > 0) {
					if(null!=comp.getMeasures().get("ncloc"))
						row.createCell(4).setCellValue(comp.getMeasures().get("ncloc").getValue());
					else
						row.createCell(4).setCellValue("");
					if(null!=comp.getMeasures().get("complexity"))
						row.createCell(5).setCellValue(comp.getMeasures().get("complexity").getValue());
					else
						row.createCell(5).setCellValue("");
					if(null!=comp.getMeasures().get("violations"))
						row.createCell(6).setCellValue(comp.getMeasures().get("violations").getValue());
					else
						row.createCell(6).setCellValue("");
					if(null!=comp.getMeasures().get("sqale_index")) {
						double tech_debt_md = Integer.parseInt(comp.getMeasures().get("sqale_index").getValue())/(8*60);
						row.createCell(7).setCellValue(tech_debt_md+"");
						
					}
					else
						row.createCell(7).setCellValue("");
					
				} else {
					row.createCell(4).setCellValue("");
					row.createCell(5).setCellValue("");
					row.createCell(6).setCellValue("");
				}

				num_row++;
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	private static void createExcelMetrics(HSSFWorkbook workbook, String sheetName, SonarClient client,
			Set<Project> projectKeys) {

		try {

			HSSFSheet sheet = workbook.createSheet(sheetName);

			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Project Key");
			rowhead.createCell(1).setCellValue("Project Status");
			rowhead.createCell(2).setCellValue("Project Name");
			rowhead.createCell(3).setCellValue("ignoredConditions");
			rowhead.createCell(4).setCellValue("Last Update");
			rowhead.createCell(5).setCellValue("Last Version");

			rowhead.createCell(6).setCellValue("Metric status");
			rowhead.createCell(7).setCellValue("metricKey");
			rowhead.createCell(8).setCellValue("Metric comparator");
			rowhead.createCell(9).setCellValue("Metric periodIndex");
			rowhead.createCell(10).setCellValue("Metric errorThreshold");
			rowhead.createCell(11).setCellValue("Metric actualValue");

			List<Project> lprojectKeys = (new ArrayList<Project>(projectKeys)).stream()
					.sorted(new Comparator<Project>() {
						@Override
						public int compare(Project o1, Project o2) {
							return o1.getName().compareTo(o2.getName());
						}
					}).collect(Collectors.toList());

			int k = 0;
			for (int i = 0; i < lprojectKeys.size(); i++) {
				HSSFRow row = sheet.createRow((short) i + 1 + k);
				QualityGateDetailsProject qd = client.qualityGateClient().projectStatus(lprojectKeys.get(i).getKey());
				row.createCell(0).setCellValue(lprojectKeys.get(i).getKey());
				row.createCell(1).setCellValue(qd.status());
				row.createCell(2).setCellValue(lprojectKeys.get(i).getName());
				if (null != qd.ignoredConditions())
					row.createCell(3).setCellValue(qd.ignoredConditions());
				else
					row.createCell(3).setCellValue("");
				ProjectAnalysis pa = lprojectKeys.get(i).getLastAnalysis();
				row.createCell(4).setCellValue(null != pa ? (null != pa.getDate() ? pa.getDate() : "") : "");
				row.createCell(5)
						.setCellValue(null != pa ? (null != pa.getProjectVersion() ? pa.getProjectVersion() : "") : "");
				int j = 0;
				for (QualityGateConditionProject c : qd.conditions()) {
					if (j > 0) {
						k++;
						row = sheet.createRow((short) i + 1 + k);
						row.createCell(0).setCellValue(lprojectKeys.get(i).getKey());
						row.createCell(1).setCellValue(qd.status());
						row.createCell(2).setCellValue(lprojectKeys.get(i).getName());
						if (null != qd.ignoredConditions())
							row.createCell(3).setCellValue(qd.ignoredConditions());
						else
							row.createCell(3).setCellValue("");
						row.createCell(4).setCellValue(null != pa ? (null != pa.getDate() ? pa.getDate() : "") : "");
						row.createCell(5).setCellValue(
								null != pa ? (null != pa.getProjectVersion() ? pa.getProjectVersion() : "") : "");
					}
					row.createCell(6).setCellValue(c.status());
					row.createCell(7).setCellValue(c.metricKey());
					row.createCell(8).setCellValue(c.comparator());
					if (null != c.periodIndex())
						row.createCell(9).setCellValue(c.periodIndex());
					else
						row.createCell(9).setCellValue("");

					row.createCell(10).setCellValue(c.errorThreshold());
					row.createCell(11).setCellValue(c.actualValue());
					j++;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void createExcelChanges(HSSFWorkbook workbook, String sheetName, List<Issue> issueList,
			IssueClient issueClient) {

		try {

			HSSFSheet sheet = workbook.createSheet(sheetName);

			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Issue Key");
			rowhead.createCell(1).setCellValue("Project Key");
			rowhead.createCell(2).setCellValue("creationDate");
			rowhead.createCell(3).setCellValue("diffs-0-key");
			rowhead.createCell(4).setCellValue("diffs-0-newValue");
			rowhead.createCell(5).setCellValue("diffs-0-oldValue");
			rowhead.createCell(6).setCellValue("diffs-1-key");
			rowhead.createCell(7).setCellValue("diffs-1-newValue");
			rowhead.createCell(8).setCellValue("diffs-1-oldValue");

			for (int i = 0; i < issueList.size(); i++) {
				HSSFRow row = sheet.createRow((short) i + 1);
				List<IssueChange> issueChanges = issueClient.changes(issueList.get(i).key());
				row.createCell(0).setCellValue(issueList.get(i).key());
				row.createCell(1).setCellValue(issueList.get(i).projectKey());
				row.createCell(2).setCellValue("");
				row.createCell(3).setCellValue("");
				row.createCell(4).setCellValue("");
				row.createCell(5).setCellValue("");
				row.createCell(6).setCellValue("");
				row.createCell(7).setCellValue("");
				row.createCell(8).setCellValue("");
				if (null == issueChanges || issueChanges.size() == 0)
					continue;
				IssueChange ic = issueChanges.get(0);
				if (null != ic) {
					row.createCell(2).setCellValue(ic.creationDate());
					if (null != ic.diffs()) {
						int j = 0;
						for (IssueChangeDiff icd : ic.diffs()) {
							if (j == 0) {
								row.createCell(3).setCellValue(icd.key());
								if (null != icd.newValue() && icd.newValue() instanceof String)
									row.createCell(4).setCellValue((String) icd.newValue());
								if (null != icd.oldValue() && icd.oldValue() instanceof String)
									row.createCell(5).setCellValue((String) icd.oldValue());
							} else if (j == 1) {
								row.createCell(6).setCellValue(icd.key());
								if (null != icd.newValue() && icd.newValue() instanceof String)
									row.createCell(7).setCellValue((String) icd.newValue());
								if (null != icd.oldValue() && icd.oldValue() instanceof String)
									row.createCell(8).setCellValue((String) icd.oldValue());
							} else
								break;
							j++;
						}
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	private static void createExcelGate(HSSFWorkbook workbook, String sheetName, QualityGate qg) {

		try {

			HSSFSheet sheet = workbook.createSheet(sheetName);

			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("ID");
			rowhead.createCell(1).setCellValue("Name");

			HSSFRow row = sheet.createRow((short) 1);
			row.createCell(0).setCellValue(qg.id());
			row.createCell(1).setCellValue(qg.name());

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	private static void createExcel(HSSFWorkbook workbook, String sheetName, List<Issue> issueList) {

		try {

			HSSFSheet sheet = workbook.createSheet(sheetName);

			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Key");
			rowhead.createCell(1).setCellValue("Project Key");
			rowhead.createCell(2).setCellValue("Component");
			rowhead.createCell(3).setCellValue("Line");
			rowhead.createCell(4).setCellValue("Rule Key");
			rowhead.createCell(5).setCellValue("Severity");

			rowhead.createCell(6).setCellValue("Status");
			rowhead.createCell(7).setCellValue("Author");
			rowhead.createCell(8).setCellValue("CreationDate");
			rowhead.createCell(9).setCellValue("UpdateDate");
			rowhead.createCell(10).setCellValue("CloseDate");

			rowhead.createCell(11).setCellValue("Message");

			for (int i = 0; i < issueList.size(); i++) {
				HSSFRow row = sheet.createRow((short) i + 1);
				row.createCell(0).setCellValue(issueList.get(i).key());
				row.createCell(1).setCellValue(issueList.get(i).projectKey());
				row.createCell(2).setCellValue(issueList.get(i).componentKey());
				row.createCell(3).setCellValue(String.valueOf(issueList.get(i).line()));
				row.createCell(4).setCellValue(issueList.get(i).ruleKey());
				row.createCell(5).setCellValue(issueList.get(i).severity());

				row.createCell(6).setCellValue(issueList.get(i).status());
				row.createCell(7).setCellValue(issueList.get(i).author());

				if (null != issueList.get(i).creationDate())
					row.createCell(8).setCellValue(issueList.get(i).creationDate());
				else
					row.createCell(8).setCellValue("");

				if (null != issueList.get(i).updateDate())
					row.createCell(9).setCellValue(issueList.get(i).updateDate());
				else
					row.createCell(9).setCellValue("");

				if (null != issueList.get(i).closeDate())
					row.createCell(10).setCellValue(issueList.get(i).closeDate());
				else
					row.createCell(10).setCellValue("");

				row.createCell(11).setCellValue(issueList.get(i).message());
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}
	static void usage() {
		System.out.println("Usage : java -cp <your-class-path> <sonar-url> <login> <password>");
	}
	
}