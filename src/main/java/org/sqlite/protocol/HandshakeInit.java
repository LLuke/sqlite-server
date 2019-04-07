/**
 * Copyright 2019 little-pan. A SQLite server based on the C/S architecture.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sqlite.protocol;

import org.sqlite.util.SecurityUtils;

/**<p>
 * Handshake init packet format: <br/><br/>
 * header - 4 bytes(length 3 bytes, seq 1 byte) <br/>
 * protocol version - 1 byte <br/>
 * server version - utf-8 string(var-int, utf-8 bytes) <br/>
 * session id - int 4 bytes(big endian) <br/>
 * challenge seed - 20 bytes <br/>
 * </p>
 * 
 * @author little-pan
 * @since 2019-03-23
 *
 */
public class HandshakeInit extends Packet {
    
    private int protocolVersion;
    private String serverVersion;
    private int sessionId;
    private byte seed[];
    
    public HandshakeInit() {
        
    }

    /**
     * @return the protocolVersion
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * @param protocolVersion the protocolVersion to set
     */
    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    /**
     * @return the serverVersion
     */
    public String getServerVersion() {
        return serverVersion;
    }

    /**
     * @param serverVersion the serverVersion to set
     */
    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    /**
     * @return the sessionId
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the seed
     */
    public byte[] getSeed() {
        return seed;
    }

    /**
     * @param seed the seed to set
     */
    public void setSeed(byte[] seed) {
        this.seed = seed;
    }
    
    /**
     * @param t
     */
    @Override
    public void writeBody(Transfer t) {
        t.writeByte(this.protocolVersion)
        .writeString(this.serverVersion)
        .writeInt(this.sessionId)
        .writeBytes(this.seed);
    }

    /**
     * @param t
     */
    @Override
    public void readBody(Transfer t) {
        this.protocolVersion = t.readByte();
        this.serverVersion = t.readString();
        this.sessionId = t.readInt();
        this.seed = t.readBytes(SecurityUtils.SEED_LEN);
    }

}