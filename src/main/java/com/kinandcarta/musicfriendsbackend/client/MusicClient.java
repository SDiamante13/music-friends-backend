package com.kinandcarta.musicfriendsbackend.client;

import com.kinandcarta.musicfriendsbackend.model.CallbackResponse;
import com.kinandcarta.musicfriendsbackend.model.MusicTokenInfo;

public interface MusicClient {
    MusicTokenInfo getMusicTokenInfo(String code);
    void redirectToAuth(CallbackResponse response);
}
