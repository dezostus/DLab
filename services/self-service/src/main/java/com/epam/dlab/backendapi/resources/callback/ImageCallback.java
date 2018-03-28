package com.epam.dlab.backendapi.resources.callback;

import com.epam.dlab.backendapi.domain.RequestId;
import com.epam.dlab.backendapi.service.ImageExploratoryService;
import com.epam.dlab.dto.UserInstanceStatus;
import com.epam.dlab.dto.exploratory.ImageCreateStatusDTO;
import com.epam.dlab.dto.exploratory.ImageStatus;
import com.epam.dlab.model.exloratory.Image;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/infrastructure_provision/image")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class ImageCallback {

    @Inject
    private ImageExploratoryService imageExploratoryService;

	@Inject
	private RequestId requestId;


    @POST
    @Path("/image_status")
    public Response imageCreateStatus(ImageCreateStatusDTO dto) {
        log.debug("Updating status of image {} for user {} to {}", dto.getName(), dto.getUser(), dto);
		requestId.remove(dto.getRequestId());
        imageExploratoryService.finishImageCreate(getImage(dto), dto.getExploratoryName(), dto.getImageCreateDTO().getIp());
        return Response.status(Response.Status.CREATED).build();
    }


    private Image getImage(ImageCreateStatusDTO dto) {
        return Image.builder()
                .name(dto.getName())
                .user(dto.getUser())
                .externalName(dto.getImageCreateDTO().getExternalName())
                .fullName(dto.getImageCreateDTO().getFullName())
                .status(UserInstanceStatus.FAILED == UserInstanceStatus.of(dto.getStatus()) ?
                        ImageStatus.FAILED : dto.getImageCreateDTO().getStatus())
                .application(dto.getImageCreateDTO().getApplication()).build();
    }
}
