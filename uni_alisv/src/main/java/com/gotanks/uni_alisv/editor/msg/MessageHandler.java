package com.gotanks.uni_alisv.editor.msg;

public interface MessageHandler {
    <T> int onHandleMessage(T message);
}
