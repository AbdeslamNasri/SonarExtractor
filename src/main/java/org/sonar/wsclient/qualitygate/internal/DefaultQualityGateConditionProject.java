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
package org.sonar.wsclient.qualitygate.internal;

import org.sonar.wsclient.qualitygate.QualityGateCondition;
import org.sonar.wsclient.qualitygate.QualityGateConditionProject;
import org.sonar.wsclient.unmarshallers.JsonUtils;

import java.util.Map;

public class DefaultQualityGateConditionProject implements QualityGateConditionProject {

  private Map<String, String> json;

  public DefaultQualityGateConditionProject(Map<String, String> json) {
    this.json = json;
  }

  @Override
  public String status() {
    return JsonUtils.getString(json, "status");
  }

  @Override
  public String metricKey() {
    return JsonUtils.getString(json, "metricKey");
  }

  @Override
  public String comparator() {
    return JsonUtils.getString(json, "comparator");
  }

  @Override
  public Integer  periodIndex() {
    return JsonUtils.getInteger(json, "periodIndex");
  }

  @Override
  public String errorThreshold() {
    return JsonUtils.getString(json, "errorThreshold");
  }

  @Override
  public String actualValue() {
    return JsonUtils.getString(json, "actualValue");
  }

}
