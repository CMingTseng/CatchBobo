package c.min.tseng.xmpp;

import org.jivesoftware.smack.packet.Message;

public interface OnMessageListener {
    public void processMessage(Message message);
}
