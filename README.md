# ROCKETBOT

Rocketbot is a Java music bot for Discord, made for my friends and it is also my pet project.

## Tech Stack

This bot uses:
* Spring Boot
* [JDA](https://github.com/discord-jda/JDA)
* [LavaPlayer](https://github.com/lavalink-devs/lavaplayer) as music player library
* [Some add-ons to LavaPlayer](https://github.com/topi314/LavaSrc?tab=readme-ov-file#lavaplayer-usage) for search engine and different sources parsing
* [Google Youtube API](https://developers.google.com/youtube/v3) for integrating own Youtube channel with playlists
* Docker
## Features

It can:
- Play music from Youtube, Spotify, Yandex Music, HTTP sources
- Connect to personal Youtube channel with playlists of music
- Play radiostations (except AAC format for now)
- Show a message with info about track that is playing now (and also next track and queue size)


## Roadmap

- Additional REST API for query history and some statistics in Postgres

- Add a simple website

