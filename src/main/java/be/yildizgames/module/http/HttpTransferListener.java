package be.yildizgames.module.http;

import java.net.URI;

public interface HttpTransferListener {

    /**
     *
     * @param uri File transferred.
     * @param lastTransferred Size of the last received chunk of data.
     * @param totalTransferred Total size of all received data.
     */
    void received(URI uri, long lastTransferred, long totalTransferred);
}
