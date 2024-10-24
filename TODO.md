# NECESSARY FEATURES FOR SNAPSHOT 2

*  [ ] Cleanup packages
    * ##  Animation System
        * [ ] Add a nice animation editor
    * ##  Asset Pool
        * [x] Revamp the display to be able to add assets at runtime through the editor
    * ##  Audio System
        * [ ] show bad sound if not able to load 
        * [x] Actually test if it works
        * [x] Actually make a nice controller for it.
        * [ ] .wav and .mp3 support.
    * ##  Editor System
        * [ ] Major cleanup, maybe make a new package and shift engine side responsibilities to it
        * [ ] Make the ability to copy-paste multiple objects at once
        * [x] Instead of a timer, check if the pixel color has a game object to see if you can place something there or not
    * ##  Input System
        * [ ] Add game controller support
    * ##  Particle System
        * [ ] Try using unique id instead of name for saving game object
    * ##  Physics System
        * [x] Better Character Controller
        * [x] Use cylinder collider component when circles are very big compared to rectangle
    * ##  Rendering System
        * [ ] Better Shader Support
    * ##  Utility
        * [x] Ability to save default values to a json file
        * [x] Debug Pencil at runtime
        * [x] Make Pencil width dynamic
        * [ ] Remove Dependency on TinyFileDialogs for popups.
    * ## Windowing System
        * [x] In Main Window think if we want to resize the frame buffer or the window when we resize 


# Features that Would be very cool
* [ ] Make Lasso Select
* [ ]  Multi Thread Editor Runtime, Game Runtime and Rendering
* [ ] Add Liquid Fun support
* [ ]  UDP multi player, server client architecture
* [ ]  WebGL rendering to make web games
* [ ]  Lighting System