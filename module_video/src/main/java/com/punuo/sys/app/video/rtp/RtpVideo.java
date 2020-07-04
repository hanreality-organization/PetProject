package com.punuo.sys.app.video.rtp;


import android.util.Log;

import com.punuo.sys.app.video.audio.AudioRecordManager;
import com.punuo.sys.app.video.audio.G711;

import java.net.DatagramSocket;
import java.net.SocketException;

import jlibrtp.DataFrame;
import jlibrtp.Participant;
import jlibrtp.RTPAppIntf;
import jlibrtp.RTPSession;

/**
 * Created by han.chen.
 * Date on 2020/6/15.
 **/
public class RtpVideo implements RTPAppIntf {
    public static NalBuffer[] nalBuffers = new NalBuffer[200];
    private final byte H264_STREAM_HEAD[] = {0x00, 0x00, 0x00, 0x01};
    private static int frameSizeG711 = 160;
    private RTPSession rtpSession;
    private DatagramSocket rtpSocket;
    private Participant participant;
    private StreamBuf streamBuf;
    private byte[] tempNal = new byte[200000];
    private int tempNalLen = 0;
    private int putNum;
    private int preSeq = -1;
    private boolean isPacketLost = true;
    private int status = 0;
    private int flag = 1;

    public static int isReceiveVideoData = 1;

    public RtpVideo(String networkAddress, int remoteRtpPort) throws SocketException {
        rtpSocket = new DatagramSocket();
        rtpSession = new RTPSession(rtpSocket, null);
        rtpSession.RTPSessionRegister(this, null, null);
        participant = new Participant(networkAddress, remoteRtpPort, remoteRtpPort + 1);
        rtpSession.addParticipant(participant);
        rtpSession.naivePktReception(false);
        rtpSession.frameReconstruction(false);
        streamBuf = new StreamBuf(100, 5);
        for (int i = 0; i < nalBuffers.length; i++) {
            nalBuffers[i] = new NalBuffer();
        }
        putNum = 0;
        AudioRecordManager.getInstance().play();
        RtpVideo.isReceiveVideoData = 0;
    }

    public void removeParticipant() {
        rtpSession.removeParticipant(participant);
    }

    public void endSession() {
        rtpSession.endSession();
        RtpVideo.isReceiveVideoData = 1;
    }

    @Override
    public void receiveData(DataFrame frame, Participant participant) {
        Log.i("han.chen", "receiveData: ");
        switch (frame.payloadType()) {
            case 98:
                StreamBufNode rtpFrameNode = new StreamBufNode(frame);
                streamBuf.addToBufBySeq(rtpFrameNode);
                if (streamBuf.isReady()) {
                    StreamBufNode streamBufNode = streamBuf.getFromBuf();
                    int seqNum = streamBufNode.getSeqNum();
                    byte[] data = streamBufNode.getDataFrame().getConcatenatedData();
                    int len = streamBufNode.getDataFrame().getTotalLength();
                    try {
                        if (isSPSAndPPS(data)) {
                            handleCompletePacket(data, seqNum, len);
                        }
                        if (((data[6] & 31) == 5) && ((data[2] & 0xff) == 0) && ((data[3] & 0xff) == 0) && ((data[4] & 0xff) == 0) && ((data[5] & 0xff) == 1)) {
                            flag = 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (flag == 0) {
                        getNalFrame(data, seqNum, len);
                    }
                }
                isReceiveVideoData = 2;
                break;
            case 69:
                byte[] audioBuffer = frame.getConcatenatedData();
                short[] audioData = new short[frameSizeG711];
                G711.ulaw2linear(audioBuffer, audioData, frameSizeG711);
                AudioRecordManager.getInstance().write(audioData, 0, frameSizeG711);
                break;
            default:
                break;
        }
    }

    private void getNalFrame(byte[] data, int seqNum, int len) {
        if (len > 6) {
            boolean isIFrame = ((data[6] & 31) == 5) && ((data[2] & 0xff) == 0)
                    && ((data[3] & 0xff) == 0) && ((data[4] & 0xff) == 0)
                    && ((data[5] & 0xff) == 1);
            if (isIFrame) {
                if ((data[0] & 31) == 28) {
                    if ((data[1] & 0xe0) == 0x80) {
                        tempNal = new byte[200000];
                        tempNal[0] = H264_STREAM_HEAD[0];
                        tempNal[1] = H264_STREAM_HEAD[1];
                        tempNal[2] = H264_STREAM_HEAD[2];
                        tempNal[3] = H264_STREAM_HEAD[3];
                        try {
                            System.arraycopy(data, 2, this.tempNal, 4, len - 2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        nalBuffers[putNum].setNalLen(len + 2);
                        preSeq = seqNum;
                        status = 1;
                    } else if (status == 1) {
                        if (preSeq + 1 == seqNum) {
                            if ((data[1] & 0xe0) == 0x40) {
                                try {
                                    System.arraycopy(data, 2, tempNal, nalBuffers[putNum].getNalLen(), len - 2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                nalBuffers[putNum].addNalLen(len - 2);
                                nalBuffers[putNum].isWriteable();
                                preSeq = seqNum;
                                System.arraycopy(tempNal, 0, nalBuffers[putNum].getWriteableNalBuf(),
                                        0, nalBuffers[putNum].getNalLen());
                                nalBuffers[putNum].writeLock();
                                putNum++;
                                putNum %= 200;
                                status = 0;
                                isIFrame = false;
                            } else {
                                try {
                                    System.arraycopy(data, 2, tempNal, nalBuffers[putNum].getNalLen(), len - 2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                nalBuffers[putNum].addNalLen(len - 2);
                                preSeq = seqNum;
                            }
                        } else {
                            flag = 1;
                            tempNal = new byte[200000];
                            nalBuffers[putNum].setNalLen(0);
                            preSeq = seqNum;
                            status = 0;
                        }
                    } else {
                        flag = 1;
                        tempNal = new byte[200000];
                        nalBuffers[putNum].setNalLen(0);
                        preSeq = seqNum;
                        status = 0;
                    }
                } else {
                    tempNal = new byte[200000];
                    tempNal[0] = H264_STREAM_HEAD[0];
                    tempNal[1] = H264_STREAM_HEAD[1];
                    tempNal[2] = H264_STREAM_HEAD[2];
                    tempNal[3] = H264_STREAM_HEAD[3];
                    try {
                        System.arraycopy(data, 0, tempNal, 4, len);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    nalBuffers[putNum].setNalLen(len + 4);
                    nalBuffers[putNum].isWriteable();
                    preSeq = seqNum;
                    System.arraycopy(tempNal, 0, nalBuffers[putNum].getWriteableNalBuf(),
                            0, nalBuffers[putNum].getNalLen());
                    nalBuffers[putNum].writeLock();
                    putNum++;
                    putNum %= 200;
                    status = 0;
                    isIFrame = false;
                }
            } else if ((data[0] & 31) == 28) {
                if ((data[1] & 0xe0) == 0x80) {
                    tempNal = new byte[200000];
                    tempNal[0] = H264_STREAM_HEAD[0];
                    tempNal[1] = H264_STREAM_HEAD[1];
                    tempNal[2] = H264_STREAM_HEAD[2];
                    tempNal[3] = H264_STREAM_HEAD[3];
                    try {
                        System.arraycopy(data, 2, tempNal, 4, len - 2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    nalBuffers[putNum].setNalLen(len + 2);
                    preSeq = seqNum;
                } else if (preSeq + 1 == seqNum) {
                    if ((data[1] & 0xe0) == 0x40) {
                        try {
                            System.arraycopy(data, 2, tempNal, nalBuffers[putNum].getNalLen(), len - 2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        nalBuffers[putNum].addNalLen(len - 2);
                        nalBuffers[putNum].isWriteable();
                        preSeq = seqNum;
                        System.arraycopy(tempNal, 0, nalBuffers[putNum].getWriteableNalBuf(),
                                0, nalBuffers[putNum].getNalLen());
                        nalBuffers[putNum].writeLock();
                        putNum++;
                        putNum %= 200;
                    } else {
                        try {
                            System.arraycopy(data, 2, tempNal, nalBuffers[putNum].getNalLen(), len - 2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        nalBuffers[putNum].addNalLen(len - 2);
                        preSeq = seqNum;
                    }
                } else {
                    flag = 1;
                    tempNal = new byte[200000];
                    nalBuffers[putNum].setNalLen(0);
                    preSeq = seqNum;
                }
            } else {
                if (preSeq == seqNum + 1) {
                    handleCompletePacket(data, seqNum, len);
                } else {
                    flag = 1;
                    tempNal = new byte[200000];
                    nalBuffers[putNum].setNalLen(0);
                    preSeq = seqNum;
                    status = 0;
                }
            }
        } else {
            if (preSeq == seqNum + 1) {
                if ((data[1] & 0xe0) == 0x40) {
                    handleLastPacket(data, seqNum, len);
                }
            }
        }
    }

    @Override
    public void userEvent(int type, Participant[] participant) {

    }

    @Override
    public int frameSize(int payloadType) {
        return 1;
    }

    private void handleCompletePacket(byte[] data, int seqNum, int len) {
        tempNal = new byte[100000];
        tempNal[0] = H264_STREAM_HEAD[0];
        tempNal[1] = H264_STREAM_HEAD[1];
        tempNal[2] = H264_STREAM_HEAD[2];
        tempNal[3] = H264_STREAM_HEAD[3];
        try {
            System.arraycopy(data, 0, tempNal, 4, len);
        } catch (Exception e) {
            e.printStackTrace();
        }
        nalBuffers[putNum].setNalLen(len + 4);
        nalBuffers[putNum].isWriteable();
        preSeq = seqNum;
        System.arraycopy(tempNal, 0, nalBuffers[putNum].getWriteableNalBuf(),
                0, nalBuffers[putNum].getNalLen());
        nalBuffers[putNum].writeLock();
        putNum++;
        putNum %= 200;
        status = 0;
    }

    private void handleLastPacket(byte[] data, int seqNum, int len) {
        try {
            System.arraycopy(data, 2, tempNal, nalBuffers[putNum].getNalLen(), len - 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        nalBuffers[putNum].addNalLen(len - 2);
        preSeq = seqNum;
        nalBuffers[putNum].isWriteable();
        System.arraycopy(tempNal, 0, nalBuffers[putNum].getWriteableNalBuf(),
                0, nalBuffers[putNum].getNalLen());
        nalBuffers[putNum].writeLock();
        putNum++;
        putNum %= 200;
        status = 0;
    }

    private Boolean isSPSAndPPS(byte[] data) {
        return (((data[4] & 31) == 7) && ((data[0] & 0xff) == 0) && ((data[1] & 0xff) == 0)
                && ((data[2] & 0xff) == 0) && ((data[3] & 0xff) == 1));
    }

    public void setActivePacket(byte[] data) {
        rtpSession.payloadType(0x7a);
        for (int i = 0; i < 2; i++) {
            rtpSession.sendData(data);
        }
    }
}
