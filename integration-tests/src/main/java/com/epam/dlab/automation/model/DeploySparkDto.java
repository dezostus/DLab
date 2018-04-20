/***************************************************************************

Copyright (c) 2016, EPAM SYSTEMS INC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

****************************************************************************/

package com.epam.dlab.automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class DeploySparkDto extends DeployClusterDto{

	@JsonProperty("dataengine_instance_count")
	private String dataengineInstanceCount;

	@JsonProperty("dataengine_instance_shape")
	private String dataengineInstanceShape;


	public String getDataengineInstanceCount() {
		return dataengineInstanceCount;
	}

	public void setDataengineInstanceCount(String dataengineInstanceCount) {
		this.dataengineInstanceCount = dataengineInstanceCount;
	}

	public String getDataengineInstanceShape() {
		return dataengineInstanceShape;
	}

	public void setDataengineInstanceShape(String dataengineInstanceShape) {
		this.dataengineInstanceShape = dataengineInstanceShape;
	}

	@Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
        		.add("image", getImage())
				.add("template_name", getTemplateName())
        		.add("name", getName())
				.add("notebook_name", getNotebookName())
				.add("dataengine_instance_shape", dataengineInstanceShape)
				.add("dataengine_instance_count", dataengineInstanceCount)
        		.toString();
    }
	
	

}
