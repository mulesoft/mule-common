package org.mule.common.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

public class JSONSampleMetadataModelFactory  {

    public JSONSampleMetadataModelFactory() {
    }

    public MetaDataModel buildModel(URL url) throws IOException {
        String json = IOUtils.toString(url.openStream());
        return buildModel(json);
    }

    public MetaDataModel buildModel(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disableDefaultTyping();
            JsonNode root = mapper.readValue(json, JsonNode.class);
            return JSONMetaDataHelper.buildModelFromNode(root);
        } catch (IOException e) {
            throw new MetaDataGenerationException(e);
        }
    }
}
