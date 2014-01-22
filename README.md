# Evernote REST Webapp

"Evernote REST Webapp" provides restful APIs for evernote.

The API covers evernote OAuth and all of thrift based operations. Input and output parameters are represented
as JSON. "Evernote REST Webapp" is a java based web application that behaves like a proxy between evernote thrift
servers and your applications.
It is built on top of [Spring Boot](http://projects.spring.io/spring-boot/),
[Sprnig Social Evernote](https://github.com/ttddyy/spring-social-evernote/), and
[evernote-java-sdk](https://github.com/evernote/evernote-sdk-java).


## REST API Sample

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
  "edamWebApiUrlPrefix":"https://...evernote.com/shard/..." }
```

---

### UserStore operations

```shell
$ curl -X POST -H "Content-Type: application/json"
   -H "evernote-rest-accesstoken: ..."
   -d '{"clientName": "foo", "edamVersionMajor": 10, "edamVersionMinor": 20}'
   http://localhost:8080/userStore/checkVersion
```

---
### NoteStore operations

```shell
$ curl -X POST -H "Content-Type: application/json"
   -H "evernote-rest-accesstoken: ..."
   -d '{
          "filter":{
              "order": 2,
              "ascending": true,
              "words": "[NOTE_FILTER_WORDS]",
              "notebookGuid": "[NOTEBOOK_GUID]",
              "tagGuids": ["TAG_GUID1", "TAG_GUID2"],
              "timeZone": "",
              "inactive": true
          },
          "offset": 2,
          "maxNotes": 30
      }'
   http://localhost:8080/noteStore/findNotes
```

# Documentation

- [Evernote REST Webapp Reference](https://github.com/ttddyy/evernote-rest-webapp/wiki)
- [Quick Start Guide](https://github.com/ttddyy/evernote-rest-webapp/wiki/QuickStart)

# Development

## library versions

| evernote-rest-webapp |  spring-boot | spring-social-evernote | evernote-sdk-java |
| --------------------:| ------------:| ----------------------:| -----------------:|
|             SNAPSHOT |    1.0.0.RC1 |                  1.0.1 |            1.25.1 |


## Continuous Integration

- [BuildHive Jenkins](https://buildhive.cloudbees.com/job/ttddyy/job/evernote-rest-webapp/)
[![Build Status](https://buildhive.cloudbees.com/job/ttddyy/job/evernote-rest-webapp/badge/icon)](https://buildhive.cloudbees.com/job/ttddyy/job/evernote-rest-webapp/)

