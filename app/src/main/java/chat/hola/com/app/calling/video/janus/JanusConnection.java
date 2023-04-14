package chat.hola.com.app.calling.video.janus;

import org.webrtc.PeerConnection;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

import java.math.BigInteger;


public class JanusConnection {
    public BigInteger handleId;
    public PeerConnection peerConnection;
    public PeerConnectionClient.SDPObserver sdpObserver;
    public VideoTrack videoTrack;
    public SurfaceViewRenderer videoRender;
    public boolean type;
}
