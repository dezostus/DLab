package com.epam.dlab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Alexey Suprun
 */
public class ImageMetadataDTO {
    @JsonProperty
    private String image;
    @JsonProperty(value = "template_name")
    private String templateName;
    @JsonProperty
    private String description;
    @JsonProperty(value = "request_id")
    private String requestId;

    public ImageMetadataDTO() {
    }

    public ImageMetadataDTO(String image, String templateName, String description, String requestId) {
        this.image = image;
        this.templateName = templateName;
        this.description = description;
        this.requestId = requestId;
    }

    public ImageMetadataDTO(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageMetadataDTO metadata = (ImageMetadataDTO) o;

        return image != null ? image.equals(metadata.image) : metadata.image == null;

    }

    @Override
    public int hashCode() {
        return image != null ? image.hashCode() : 0;
    }
}