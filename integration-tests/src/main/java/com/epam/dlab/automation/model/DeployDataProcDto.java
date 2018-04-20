package com.epam.dlab.automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class DeployDataProcDto extends DeployClusterDto {

	@JsonProperty("dataproc_master_count")
	private String dataprocMasterCount;

	@JsonProperty("dataproc_slave_count")
	private String dataprocSlaveCount;

	@JsonProperty("dataproc_preemptible_count")
	private String dataprocPreemptibleCount;

	@JsonProperty("dataproc_master_instance_type")
	private String dataprocMasterInstanceType;

	@JsonProperty("dataproc_slave_instance_type")
	private String dataprocSlaveInstanceType;

	@JsonProperty("dataproc_version")
	private String dataprocVersion;


	public String getDataprocMasterCount() {
		return dataprocMasterCount;
	}

	public void setDataprocMasterCount(String dataprocMasterCount) {
		this.dataprocMasterCount = dataprocMasterCount;
	}

	public String getDataprocSlaveCount() {
		return dataprocSlaveCount;
	}

	public void setDataprocSlaveCount(String dataprocSlaveCount) {
		this.dataprocSlaveCount = dataprocSlaveCount;
	}

	public String getDataprocPreemptibleCount() {
		return dataprocPreemptibleCount;
	}

	public void setDataprocPreemptibleCount(String dataprocPreemptibleCount) {
		this.dataprocPreemptibleCount = dataprocPreemptibleCount;
	}

	public String getDataprocMasterInstanceType() {
		return dataprocMasterInstanceType;
	}

	public void setDataprocMasterInstanceType(String dataprocMasterInstanceType) {
		this.dataprocMasterInstanceType = dataprocMasterInstanceType;
	}

	public String getDataprocSlaveInstanceType() {
		return dataprocSlaveInstanceType;
	}

	public void setDataprocSlaveInstanceType(String dataprocSlaveInstanceType) {
		this.dataprocSlaveInstanceType = dataprocSlaveInstanceType;
	}

	public String getDataprocVersion() {
		return dataprocVersion;
	}

	public void setDataprocVersion(String dataprocVersion) {
		this.dataprocVersion = dataprocVersion;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("image", getImage())
				.add("template_name", getTemplateName())
				.add("name", getName())
				.add("notebook_name", getNotebookName())
				.add("dataproc_master_count", dataprocMasterCount)
				.add("dataproc_slave_count", dataprocSlaveCount)
				.add("dataproc_preemptible_count", dataprocPreemptibleCount)
				.add("dataproc_master_instance_type", dataprocMasterInstanceType)
				.add("dataproc_slave_instance_type", dataprocSlaveInstanceType)
				.add("dataproc_version", dataprocVersion)
				.toString();
	}
}
