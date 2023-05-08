package com.omniscient.omnibalance.Core.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.omniscient.omnibalance.Core.Core;

import java.io.IOException;

public interface JSON {
    default ObjectNode toJSON() {
        return new ObjectMapper().valueToTree(this);
    }

    default void fromJSON(ObjectNode json) {
        try {
            new ObjectMapper()
                    .readerForUpdating(this)
                    .readValue(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
