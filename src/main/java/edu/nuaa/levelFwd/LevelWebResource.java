package edu.nuaa.levelFwd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onosproject.net.HostId;
import org.onosproject.rest.AbstractWebResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Sample web resource.
 */
@Path("level")
public class LevelWebResource extends AbstractWebResource {

    /**
     * Get hello world greeting.
     *
     * @return 200 OK
     */
    @GET
    @Path("test")
    public Response getGreeting() {
        ObjectNode node = mapper().createObjectNode().put("hello", "world");
        return ok(node).build();
    }

    @GET
    @Path("hosts")
    public Response getHostInfos() {
        List<HostInfo> infos = get(LevelService.class).getHostInfos();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();

        for (HostInfo info : infos) {
            ObjectNode node = mapper.createObjectNode();

            node.put("hostId", info.id().toString());
            node.put("vlanId", info.vlanId().toString());
            node.put("deviceId", info.toString());
            node.put("mac", info.srcMAC().toString());
            node.put("rule", info.rule().toString());

            arrayNode.add(node);
        }

        root.set("hosts", arrayNode);

        return Response.ok(root.toString(), MediaType.APPLICATION_JSON_TYPE).build();
    }

    /**
     * Get HostInfo By HostId.
     *
     * @param id HostId except the suffix "/None"
     * @return HostInfo in JSON.
     */
    @GET
    @Path("host/{id}")
    public Response getHostInfoById(@PathParam("id") String id) throws URISyntaxException {
        HostInfo info = get(LevelService.class).getHostInfo(HostId.hostId(id + "/None"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("hostId", info.id().toString());
        node.put("vlanId", info.vlanId().toString());
        node.put("deviceId", info.toString());
        node.put("mac", info.srcMAC().toString());
        node.setAll(ruleToJson(info.rule()));
        return Response.ok(node.toString(), MediaType.APPLICATION_JSON_TYPE).build();
    }

    /**
     * Turns the LevelRule to Json Node.
     */
    private ObjectNode ruleToJson(LevelRule rule) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("level", rule.level().toString());
        node.put("middleBox", rule.middleBox().toString());
        ArrayNode arrayNode = mapper.createArrayNode();
        for (String s : rule.service()) {
            arrayNode.add(s);
        }
        node.set("service", arrayNode);
        return node;
    }

    /**
     * Set MiddleBox MAC Address
     *
     *
     */
    @POST
    @Path("middlebox")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setMiddleBoxes(InputStream input) throws URISyntaxException {
        // TODO: Wait for middlebox defined.
        return Response.noContent().build();
    }

    /**
     * Get Middle Boxes.
     * Returns array of the middle boxes.
     *
     * @return 200 OK
     */
    @GET
    @Path("middlebox")
    public Response getMiddleBoxes() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();
        // TODO: Wait for middleBox defined.
        root.set("middleBox", arrayNode);
        return Response.ok(root, MediaType.APPLICATION_JSON_TYPE).build();
    }

    /**
     * Clear Middle Boxes.
     * @return 204 NO CONTENT
     */
    @DELETE
    @Path("middlebox")
    public Response clearMiddleBoxes() {
        // TODO: Wait for middlebox defined.
        return Response.noContent().build();
    }
}
