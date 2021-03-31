package fil.sr2.flopbox;

//import org.apache.commons.net.ftp.FTPClient;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Path("/servers")
public class ServerResource {
    private static final Map<String, Server> serversAvailable = new HashMap<>();
    private static final String PATH_NAME= "/tmp/";
    FTPClient ftpClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getServers(){
        if (serversAvailable.isEmpty()) return "No servers added!";

        String toReturn = "";
        for (Map.Entry<String, Server> entry : serversAvailable.entrySet()) {
            toReturn += entry.getKey()+"\n";
        }
        return toReturn;
    }


    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAddress(@PathParam("name") String name) {
        return "server "+name+" correspond to the address : " + serversAvailable.get(name).getAddress() + "\n";
    }

    /*@POST
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String add(@PathParam("name") String name, @QueryParam("address") String address) {
        serversAvailable.put(name, new Server(address, name));
        return "server Added !";
    }*/

    @POST
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String add(@PathParam("name") String name, @QueryParam("address") String address,  @QueryParam("port") int port ) {
        if (port == 21)
            serversAvailable.put(name, new Server(address, name));
        else
            serversAvailable.put(name, new Server(address, name, port));
        return "server Added !";
    }

    private FTPClient connect(Server server) {
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(server.getAddress(), server.getPort());
            ftpClient.login(server.getUser(), server.getPass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ftpClient;
    }



   /* @PUT
    @Path("/{name}")
    public void update(@PathParam("name") String name, @QueryParam("address") String address) {
        System.out.println("@PUT");
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(address);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
        serversAvailable.put(name, ftpClient);
    }*/

    @DELETE
    @Path("</{name}")
    public void remove(@PathParam("name") String name) {
        serversAvailable.remove(name);
    }



    @GET
    @Path("/{name}/list/{path: .*}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response listFTP(@PathParam("name") String name, @PathParam("path") String path) {
        Response.ResponseBuilder res;
        Server server = serversAvailable.get(name);
        connect(server);
        String list = "";
        ftpClient.enterLocalPassiveMode();
        try {
            FTPFile[] files = ftpClient.listFiles(path);
            for (FTPFile file : files) {
                list += file.getRawListing() + "\n";
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot list the directory");
        }
        res = Response.status(Response.Status.OK).entity(list);
        disconnect();

        return res.build();
    }

    @GET
    @Path("/{name}/file/{path: .*}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFileFTP(@PathParam("name") String name, @PathParam("path") String path) {
            Response.ResponseBuilder res;
            connect(serversAvailable.get(name));
            File file = new File(PATH_NAME + path);
            System.out.println(file);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ftpClient.enterLocalPassiveMode();
            try {
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //ftpClient.changeWorkingDirectory(path);

                OutputStream os = new FileOutputStream(file);
                ftpClient.retrieveFile(path, os);

                res = Response.status(Response.Status.OK).entity(file);
                disconnect();
            } catch (Exception e) {
                res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to received the list of the directory\n");
            }
            return res.build();

    }

    


        private void disconnect () {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    return;
                }
            }
        }


    }
