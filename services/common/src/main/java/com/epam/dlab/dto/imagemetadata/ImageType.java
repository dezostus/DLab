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

package com.epam.dlab.dto.imagemetadata;

public enum ImageType {
    COMPUTATIONAL("computational"),
    EXPLORATORY("exploratory");

    private String type;

    ImageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
    public static ImageType of(String type) {
        if (type != null) {
            for (ImageType value : ImageType.values()) {
                if (type.equalsIgnoreCase(value.toString())) {
                    return value;
                }
            }
        }
        return null;
    }
}
