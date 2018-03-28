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

package com.epam.dlab.backendapi.core.response.handlers;

import com.epam.dlab.dto.UserInstanceStatus;
import com.epam.dlab.backendapi.core.commands.DockerAction;
import com.epam.dlab.dto.base.edge.EdgeInfo;
import com.epam.dlab.dto.base.keyload.UploadFileResult;
import com.epam.dlab.exceptions.DlabException;
import com.epam.dlab.rest.client.RESTService;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class EdgeCallbackHandler<E extends EdgeInfo, T extends UploadFileResult<E>> extends ResourceCallbackHandler<T> {
    private final String callbackURI;
    private final Class<E> responseType;

    public EdgeCallbackHandler(RESTService selfService, DockerAction action, String uuid, String user,
                               String callbackURI, Class<E> responseType, Class<T> enclosingType) {

        super(selfService, user, uuid, action, enclosingType);
        this.callbackURI = callbackURI;
        this.responseType = responseType;
    }

    @Override
    protected String getCallbackURI() {
        return callbackURI;
    }

    protected T parseOutResponse(JsonNode resultNode, T baseStatus) {
        if (resultNode != null && getAction() == DockerAction.CREATE
                && UserInstanceStatus.of(baseStatus.getStatus()) != UserInstanceStatus.FAILED) {
            try {
                E credential = mapper.readValue(resultNode.toString(), responseType);
                credential.setEdgeStatus(UserInstanceStatus.RUNNING.toString());
                baseStatus.withEdgeInfo(credential);
            } catch (IOException e) {
                throw new DlabException("Cannot parse the EDGE info in JSON: " + e.getLocalizedMessage(), e);
            }
        }

        return baseStatus;
    }

    @Override
    public void handleError(String errorMessage) {
        super.handleError("Could not upload the user key: " + errorMessage);
    }
}
