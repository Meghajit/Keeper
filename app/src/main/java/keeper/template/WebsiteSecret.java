package keeper.template;

import java.nio.charset.StandardCharsets;

public class WebsiteSecret implements Template {
    private final byte[] username;
    private final byte[] password;

    public byte[] getMetadata() {
        return metadata;
    }

    private final byte[] metadata;

    public WebsiteSecret(byte[] username, byte[] password, byte[] metadata) {
        this.username = username;
        this.password = password;
        this.metadata = metadata;
    }

    @Override
    public String print() {
        return String.join(
                " <> ",
                (new String(username, StandardCharsets.UTF_8)),
                (new String(password, StandardCharsets.UTF_8)),
                (new String(metadata, StandardCharsets.UTF_8)));
    }
}
