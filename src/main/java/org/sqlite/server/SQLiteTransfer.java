/**
 * A SQLite server based on the C/S architecture, little-pan (c) 2019
 */
package org.sqlite.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.sqlite.exception.NetworkException;

/**<p>
 * The SQLite server protocol codec.
 * </p>
 * 
 * @author little-pan
 * @since 2019-3-20
 *
 */
public class SQLiteTransfer {
    
    public static final int PROTOCOL_VERSION = 1;
    public static final String ENCODING = "UTF-8";
    
    private final SQLiteServer server;
    private final SQLiteSession session;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private byte buffer[];
    
    private int protocolVersion = PROTOCOL_VERSION;
    private String encoding = ENCODING;

    public SQLiteTransfer(SQLiteSession session) throws NetworkException {
        this.server = session.getServer();
        this.session= session;
        this.socket = session.getSocket();
        this.buffer = new byte[this.server.getInitPacket()];
        try {
            this.in  = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        }catch(IOException e){
            throw new NetworkException("Access socket stream error", e);
        }
    }
    
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
    
    public String getEncoding() {
        return this.encoding;
    }

}
