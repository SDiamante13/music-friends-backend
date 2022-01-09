# Music & Friends

Music Friends is a social platform for lovers of music.

When a user logs in they can see their friends and some of their favorite bands.

Like the same band as a friend, great! Show your appreciation with a microphone.

Welcome to the band!


# Spotify API

https://developer.spotify.com/documentation/web-api/quick-start/

## Authorization Flow 

The first call is the service ‘/authorize’ endpoint, passing to it the client ID, scopes, and redirect URI. 
This is the call that starts the process of authenticating to user and gets the user’s authorization to access data.

The second call is to the Spotify Accounts Service ‘/api/token’ endpoint, passing to it the authorization code returned by the first call and the client secret key. 
This call returns an access token and also a refresh token.


```
curl --request POST --url https://accounts.spotify.com/api/token --header 'Authorization: Basic ${clientId}:${clientSecret}=' --header 'Content-Type: application/x-www-form-urlencoded'
 --data code=${code} --data redirect_uri=http://localhost:8080/api/v1/callback --data grant_type=authorization_code
```