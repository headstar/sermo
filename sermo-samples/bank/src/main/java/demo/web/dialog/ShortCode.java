package demo.web.dialog;

/**
 * @author Per Johansson
 */
public class ShortCode {

    private final String shortCode;
    private final String description;

    public ShortCode(String shortCode, String description) {
        this.shortCode = shortCode;
        this.description = description;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getDescription() {
        return description;
    }
}
