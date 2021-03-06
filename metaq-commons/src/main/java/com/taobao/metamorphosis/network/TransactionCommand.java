package com.taobao.metamorphosis.network;

import com.taobao.gecko.core.buffer.IoBuffer;
import com.taobao.gecko.core.command.CommandHeader;
import com.taobao.gecko.core.command.RequestCommand;
import com.taobao.metamorphosis.transaction.TransactionInfo;


/**
 * 事务命令,协议格式如下：</br> transaction transactionKey sessionId type [timeout]
 * opaque\r\n
 * 
 * 
 * @author boyan
 * 
 */
public class TransactionCommand implements RequestCommand, MetaEncodeCommand {
    private TransactionInfo transactionInfo;
    private final Integer opaque;
    static final long serialVersionUID = -1L;


    @Override
    public Integer getOpaque() {
        return this.opaque;
    }


    @Override
    public CommandHeader getRequestHeader() {
        return this;
    }


    public TransactionCommand(final TransactionInfo transactionInfo, final Integer opaque) {
        super();
        this.transactionInfo = transactionInfo;
        this.opaque = opaque;
    }


    public TransactionInfo getTransactionInfo() {
        return this.transactionInfo;
    }


    public void setTransactionInfo(final TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.opaque == null ? 0 : this.opaque.hashCode());
        result = prime * result + (this.transactionInfo == null ? 0 : this.transactionInfo.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final TransactionCommand other = (TransactionCommand) obj;
        if (this.opaque == null) {
            if (other.opaque != null) {
                return false;
            }
        }
        else if (!this.opaque.equals(other.opaque)) {
            return false;
        }
        if (this.transactionInfo == null) {
            if (other.transactionInfo != null) {
                return false;
            }
        }
        else if (!this.transactionInfo.equals(other.transactionInfo)) {
            return false;
        }
        return true;
    }


    @Override
    public IoBuffer encode() {
        final String transactionKey = this.transactionInfo.getTransactionId().getTransactionKey();
        final String type = this.transactionInfo.getType().name();
        int capacity =
                17 + transactionKey.length() + this.transactionInfo.getSessionId().length() + type.length()
                        + ByteUtils.stringSize(this.opaque);
        if (this.transactionInfo.getTimeout() > 0) {
            capacity += 1 + ByteUtils.stringSize(this.transactionInfo.getTimeout());
        }
        final IoBuffer buffer = IoBuffer.allocate(capacity);
        if (this.transactionInfo.getTimeout() > 0) {
            ByteUtils.setArguments(buffer, MetaEncodeCommand.TRANS_CMD, transactionKey,
                this.transactionInfo.getSessionId(), type, this.transactionInfo.getTimeout(), this.opaque);
        }
        else {
            ByteUtils.setArguments(buffer, MetaEncodeCommand.TRANS_CMD, transactionKey,
                this.transactionInfo.getSessionId(), type, this.opaque);
        }
        buffer.flip();
        return buffer;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
