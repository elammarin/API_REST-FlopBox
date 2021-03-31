package fil.sr2.flopbox;

public class Server {

    private String address;
    private String name;
    private String user;
    private String pass;
    private int port;




    /**
     * Constructor
     *
     * @param address
     * @param name
     * @param user
     * @param pass
     */
    public Server(String address, String name, String user, String pass, int port) {
        this.address = address;
        this.port = port;
        this.name = name;
        this.user = user;
        this.pass = pass;
    }

    public Server(String address, String name) {
        this.address = address;
        this.name = name;
        this.user = "anonymous";
        this.pass = "anonymous";
        this.port = 21;
    }

    public Server(String address, String name, int port) {
        this.address = address;
        this.name = name;
        this.user = "anonymous";
        this.pass = "anonymous";
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
