## ReadiumAudiobook

Repo: https://github.com/roywatson/ReadiumAudiobook

*Note*: This app was successfully tested on:

- Samsung S22 Ultra with API 34 (Android 14)
- Emulators with:
  -  API 33 (Android 13)
  - API 34 (Android 14) 
- Asus tablet with Android 11
- Lenovo tablet with Android 10

This is a demo/test app for using Readium to play audiobooks. It has an audiobook bundled in the app to avoid downloading or otherwise installing content. The bundled audiobook is public domain, named "The Black Cat Vol. 03 No. 12 September 1898" and is available for download at : https://librivox.org/black-cat-03-12-sep1898-by-various/

- Opens and loads audiobook components 
- Allows the user to play and pause the playback.
- Allows the user to skip forward and backward in 30 second jumps.
- Displays the current player position timestamp and the total length of the current component.
- Allows the user to close the content and cleans up all of the player infrastructure so that the user and play the content again.
- The content playing is continued when the app is backgrounded.
- When the apps backgrounded the user can continue to control the player from the native media controls.
- I can control the playback from my Samsung Watch 5 Pro running Wear OS version 4 using the watch's native media control tile including controlling play, pause, fast forward, rewind and audio volume.