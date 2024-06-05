# Note app
A note app made in Kotlin with Jetpack Compose
<br></br>

## Features And Specifications
- Note Management: Create, Read, Update, Delete Notes.
- Data Persistence: Utilized Room Database for storage.
- Dependency Injection with Dagger-Hilt: To enhance maintainability.
- MVI Architecture: To improve the app's structure.
- Security and Privacy: Users can mark a note as private and access it via their fingerprint.
<br></br>

## Notes Screen
<img src="https://github.com/SerbanMarinescu/Note-App-2.0/blob/4abb188443b9c49f0a0b320edd16d693225fa221/images/MainScreen.png" width=200 align="left">

### Here, the user is able to:
<pre><ul><li>Visualize all the created notes</li><li>Sort the notes by date or by color</li><li>Search notes by title</li><li>Create new notes, or access existing ones</li><li>Get results from search query in real time, as the user types something</li><li>Upon deleting a note, the user can undo the process if he/she so whishes</li></ul></pre>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>

## Note Details Screen
<img src="https://github.com/SerbanMarinescu/Note-App-2.0/blob/a9e3d3e213b9b9c812b8fae9f204427e3aa08dff/images/Editing%20a%20note.png" width=200 align="left">

### Here, the user is able to:
<pre><ul><li>Create or edit notes</li><li>Choose the note's background color from the top menu</li><li>Use the rich text editor to change the style and color of the text</li><li>Lock or unlock a note to change its private status</li></ul></pre>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>

## Accessing Private Notes
- ### To be able to view private notes, the user needs to verify their fingerprint using the scanner that opens upon selecting a private note.
<div align="center">
  <img src="https://github.com/SerbanMarinescu/Note-App-2.0/blob/51b8a2802f0d4abf846d639f822314d3a21ab6c2/images/FingerprintPrompt.png" width=200>
</div>



## Animations
- ### Implemented Animations to further enhance the user experience.
<img src="https://github.com/SerbanMarinescu/Note-App-2.0/blob/1a40da58369580838439a53d027af33d39fb6012/images/MenuAnimation.gif" width=200> &nbsp; &nbsp; &nbsp; &nbsp; <img src="https://github.com/SerbanMarinescu/Note-App-2.0/blob/1a40da58369580838439a53d027af33d39fb6012/images/LockUnlockNoteAnimation.gif" width=200> &nbsp; &nbsp; &nbsp; &nbsp; <img src="https://github.com/SerbanMarinescu/Note-App-2.0/blob/1a40da58369580838439a53d027af33d39fb6012/images/FontSelectionAnimation.gif" width=200>











