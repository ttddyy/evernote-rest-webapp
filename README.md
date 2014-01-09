# Evernote REST Webapp

## API Sample

### OAuth

Step1: Request temporal credential and redirect url for authorization
```shell
$ curl -X POST -d "callbackUrl=http://myapp/oauth-callback" http://localhost:8080/oauth/auth
```

Response:
```json
{ "authorizeUrl":"https://sandbox.evernote.com/OAuth.action?oauth_token=...",
  "requestTokenValue":"…", "requestTokenSecret":"…" }
```

Step2: Request access token
```shell
$ curl -X POST -d "oauthToken=..." -d "requestTokenSecret=..." -d "oauthVerifier=..."
    http://localhost:8080/oauth/accessToken
```

Response:
```json
{ "value":"…", "secret":"", "edamShard":"s…", "edamUserId":"...", "edamExpires":"...",
  "edamNoteStoreUrl":"https://...evernote.com/shard/...",
  "edamWebApiUrlPrefix":"https://...evernote.com/shard/..."
}
```

---

### UserStore operations

```shell
$ curl -X POST
   -H "evernote-rest-accesstoken: ..."
   -d '{"clientName": "foo", "edamVersionMajor": 10, "edamVersionMinor": 20}'
   http://localhost:8080/userStore/checkVersion
```

---
### NoteStore operations

# Documentation

- [Evernote REST Webapp Reference](https://github.com/ttddyy/evernote-rest-webapp/wiki/Reference)


# Development

## Continuous Integration

- [BuildHive Jenkins](https://buildhive.cloudbees.com/job/ttddyy/job/evernote-rest-webapp/)
[![Build Status](https://buildhive.cloudbees.com/job/ttddyy/job/evernote-rest-webapp/badge/icon)](https://buildhive.cloudbees.com/job/ttddyy/job/evernote-rest-webapp/)

