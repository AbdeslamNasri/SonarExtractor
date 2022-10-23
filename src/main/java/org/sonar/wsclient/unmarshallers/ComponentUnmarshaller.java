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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sonar.wsclient.services.Component;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.WSUtils;

public class ComponentUnmarshaller extends AbstractUnmarshaller<Component> {

	@Override
	protected Component parse(Object json) {
//		System.out.println("Component JSON Object: "+json);
		Component comp = new Component();
		WSUtils utils = WSUtils.getINSTANCE();
		Object component = utils.getField(json, "component");
		String  id = utils.getString(component, "id");
		String  key = utils.getString(component, "key");
		String  name = utils.getString(component, "name");
		String  qualifier = utils.getString(component, "qualifier");
		comp.setId(id);
		comp.setKey(key);
		comp.setName(name);
		comp.setQualifier(qualifier);
		
		Object measures = utils.getField(component, "measures");
		Map<String, Measure> pp = new HashMap<>();
		comp.setMeasures(pp);
		if (measures instanceof List) {
			if (utils.getArraySize(measures) >= 1) {
				for (int i = 0; i < utils.getArraySize(measures); i++) {
					Object elt = utils.getArrayElement(measures, i);
					if (elt != null) {
						Measure meas = new Measure(elt);
						pp.put(meas.getMetric(), meas);
					}
				}
			}
		}

		return comp;
	}
}
