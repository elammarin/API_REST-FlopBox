package fil.sr2.flopbox;

import org.apache.commons.net.ftp.FTPClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Path("/server")
public class ServerResource {
    private static Map<String, FTPClient> serversAvailable = new HashMap<>();

    @POST
    @Path("/{name}")
    public void add(@PathParam("name") String name, @FormParam("address") String address) {
        if (serversAvailable.containsKey(name) && (serversAvailable.get(name)).isConnected()) {
            return;
        }
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(address);
            serversAvailable.put(name, ftpClient);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void connect(String name, String username, String password) {
        if (username == null || password == null) {
            username = password = "anonymous";
        }
        if (serversAvailable.containsKey(name) && serversAvailable.get(name).isConnected()){
            return;
        }
        FTPClient ftpClient = serversAvailable.get(name);
        try {
            ftpClient.login(username, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PUT
    @Path("/{name}")
    public void update(@PathParam("name") String name, @FormParam("address") String address) {
        System.out.println("@PUT");
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(address);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
        serversAvailable.put(name, ftpClient);
    }

    @DELETE
    @Path("/{name}")
    public void remove(@PathParam("name") String name) {
        serversAvailable.remove(name);
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAddress(@PathParam("name") String name) {
        return "server "+name+" correspond to the address : " + serversAvailable.get(name).getRemoteAddress() + "\n";
    }

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test(){
    	return "ok !";
    }

    private void disconnect(String name) {
        if (serversAvailable.get(name).isConnected()) {
            try {
                serversAvailable.get(name).disconnect();
            } catch (IOException e) {
                return;
            }
        }
    }


}
