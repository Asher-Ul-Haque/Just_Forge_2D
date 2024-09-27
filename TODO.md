# NECESSARY FEATURES FOR SNAPSHOT 2

*  [ ] Cleanup packages
    * ##  Animation System
        * [ ] Add a nice animation editor
    * ##  Asset Pool
        * [ ] Revamp the display to be able to add assets at runtime through the editor
    * ##  Audio System
        * [ ] Actually test if it works
    * ##  Editor System
        * [ ] Major cleanup, maybe make a new package and shift engine side responsibilities to it
        * [ ] Make the ability to copy-paste multiple objects at once
        * [ ] Instead of a timer, check if the pixel color has a game object to see if you can place something there or not
    * ##  Entity Component System
        * [ ] Move sprite components out of the package and into renderer
    * ##  Input System
        * [ ] Add game controller support
    * ##  Particle System
        * [ ] Try using unique id instead of name for saving game object
    * ##  Prefab System
        * [ ] Add The Ability to Generate multiple game objects like spawners in paper minecraft
    * ##  Rendering System
        * [ ] Better Shader Support
    * ##  Scene System
        * [ ] Reduce the demarcation between scene and scene initializer 
        * [ ] Move current scene away from Main Window and to Scene System Manager
        * [ ] Make Scene System Manager not completely static
    * ##  Utility
        * [ ] Ability to save default values to a json file
        * [ ] Debug Pencil at runtime
        * [ ] Rename Debug Pencil to just Pencil
        * [ ] Make Pencil width dynamic
    * ## Windowing System
        * [ ] In Main Window think if we want to resize the frame buffer or the window when we resize 


# Features that Would be very cool
* [ ] Make Lasso Select
* [ ]  Multi Thread Editor Runtime, Game Runtime and Rendering
* [ ] Add Box2D joints
* [ ] Add Liquid Fun support
* [ ]  UDP multi player, server client architecture
* [ ]  WebGL rendering to make web games
* [ ]  Lighting System