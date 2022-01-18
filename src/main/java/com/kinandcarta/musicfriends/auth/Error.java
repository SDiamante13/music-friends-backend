package com.kinandcarta.musicfriends.auth;

import lombok.Value;

@Value
class Error {
    String type;
    String message;
}
