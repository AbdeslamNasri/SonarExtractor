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
import org.sonar.wsclient.services.Projects;
import org.sonar.wsclient.services.WSUtils;

public class ProjectsUnmarshaller extends AbstractUnmarshaller<Projects> {

	@Override
	protected Projects parse(Object json) {
		Projects projs = new Projects();
		WSUtils utils = WSUtils.getINSTANCE();
		Object paging = utils.getField(json, "paging");
		Paging p = new Paging(paging);
		projs.setPaging(p);
		Object array = utils.getField(json, "components");
		List<Project> pp = new ArrayList<Project>();
		projs.setProjects(pp);
		if (array instanceof List) {
			if (utils.getArraySize(array) >= 1) {
				for (int i = 0; i < utils.getArraySize(array); i++) {
					Object elt = utils.getArrayElement(array, i);
					if (elt != null) {
						Project proj = new Project(elt);
						pp.add(proj);
					}
				}
			}
		}

		return projs;
	}
}
