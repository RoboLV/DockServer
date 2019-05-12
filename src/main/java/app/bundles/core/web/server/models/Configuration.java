package app.bundles.core.web.server.models;

public class Configuration {
    /**
     * Thread count to process connections && data
     */
    private int threadCount;

    /**
     * Restart thread after this count of invokes
     */
    private int restartThreadAfter;

    /**
     * Amount of time after shutdown thread if it will not response
     */
    private long maxExecutionTime;

    /**
     * port to run webserver
     */
    private int port;

    /**
     * Bind address to run webserver
     */
    private String bindAddress;

    /**
     * Use ssl ?
     */
    private boolean useSSL;

    /**
     * SSL CERT file
     */
    private String sslCertFile;

    /**
     * SSL CA file
     */
    private String sslCaFile;

    public Configuration() {
        this.threadCount = 1;
        this.restartThreadAfter = 1000;
        this.maxExecutionTime = 100000000;
        this.port = 80;
        this.bindAddress = "0.0.0.0";
        this.useSSL = false;
        this.sslCertFile = "";
        this.sslCaFile = "";
    }

    public int getThreadCount() {
        return threadCount;
    }

    public Configuration setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        return this;
    }

    public int getRestartThreadAfter() {
        return restartThreadAfter;
    }

    public Configuration setRestartThreadAfter(int restartThreadAfter) {
        this.restartThreadAfter = restartThreadAfter;
        return this;
    }

    public long getMaxExecutionTime() {
        return maxExecutionTime;
    }

    public Configuration setMaxExecutionTime(long maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
        return this;
    }

    public int getPort() {
        return port;
    }

    public Configuration setPort(int port) {
        this.port = port;
        return this;
    }

    public String getBindAddress() {
        return bindAddress;
    }

    public Configuration setBindAddress(String bindAddress) {
        this.bindAddress = bindAddress;
        return this;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public Configuration setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
        return this;
    }

    public String getSslCertFile() {
        return sslCertFile;
    }

    public Configuration setSslCertFile(String sslCertFile) {
        this.sslCertFile = sslCertFile;
        return this;
    }

    public String getSslCaFile() {
        return sslCaFile;
    }

    public Configuration setSslCaFile(String sslCaFile) {
        this.sslCaFile = sslCaFile;
        return this;
    }
}
