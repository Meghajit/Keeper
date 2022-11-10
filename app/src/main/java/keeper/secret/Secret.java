package keeper.secret;

public class Secret {
    private String username;
    private String password;
    private String metadata;

    public Secret(String username, String password, String metadata) {
        this.username = username;
        this.password = password;
        this.metadata = metadata;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMetadata() {
        return metadata;
    }
}
