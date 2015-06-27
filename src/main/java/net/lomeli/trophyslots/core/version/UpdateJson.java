package net.lomeli.trophyslots.core.version;

public class UpdateJson {
    private int major;
    private int minor;
    private int revision;
    private String downloadURL;
    private boolean direct;
    private String[] changeLog;

    public UpdateJson(int major, int minor, int revision, String downloadURL, boolean direct, String... changes) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
        this.downloadURL = downloadURL;
        this.direct = direct;
        this.changeLog = changes;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getRevision() {
        return revision;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public boolean isDirect() {
        return direct;
    }

    public String[] getChangeLog() {
        return changeLog;
    }

    public String getVersion() {
        return major + "." + minor + "." + revision;
    }
}
