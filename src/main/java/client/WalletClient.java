package client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

public class WalletClient {
    private Client client;
    private WebResource webResource;
    private ClientResponse clientResponse;

    public WalletClient() {
        client = Client.create();
    }

    public String doGet(String url) {
        webResource = client.resource(url);
        clientResponse = webResource
                .accept("application/json")
                .get(ClientResponse.class);
        return clientResponse.getEntity(String.class);
    }

    public String doPost(String url) {
        webResource = client.resource(url);
        clientResponse = webResource
                .accept("application/json")
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class);
        return clientResponse.getEntity(String.class);
    }

    public String doDelete(String url) {
        webResource = client.resource(url);
        clientResponse = webResource
                .accept("application/json")
                .type(MediaType.APPLICATION_JSON_TYPE)
                .delete(ClientResponse.class);
        return clientResponse.getEntity(String.class);
    }
}
