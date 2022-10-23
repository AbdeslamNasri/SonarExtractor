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
package org.sonar.wsclient.unmarshallers;

import java.util.ArrayList;
import java.util.List;

import org.sonar.wsclient.services.Paging;
import org.sonar.wsclient.services.Project;
import org.sonar.wsclient.services.ProjectAnalyses;
import org.sonar.wsclient.services.ProjectAnalysis;
import org.sonar.wsclient.services.Projects;
import org.sonar.wsclient.services.WSUtils;

public class ProjectAnalysesUnmarshaller extends AbstractUnmarshaller<ProjectAnalyses> {

	@Override
	protected ProjectAnalyses parse(Object json) {
		WSUtils utils = WSUtils.getINSTANCE();
		Object paging = utils.getField(json, "paging");
		ProjectAnalyses proj = new ProjectAnalyses(paging);
		Object array = utils.getField(json, "analyses");
		List<ProjectAnalysis> analyses = new ArrayList<ProjectAnalysis>();
		proj.setAnalyses(analyses);
		if (array instanceof List) {
			if (utils.getArraySize(array) >= 1) {
				for (int i = 0; i < utils.getArraySize(array); i++) {
					Object elt = utils.getArrayElement(array, i);
					if (elt != null) {
						ProjectAnalysis projA = new ProjectAnalysis(elt);
						analyses.add(projA);
					}
				}
			}
		}

		return proj;
	}
}
