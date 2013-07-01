<h1>Your Future Needs Help</h1>
<h2>Todo</h2>
- Interleaved VBOs
- Game Content
  - Models / Textures / Assets
  - Storyline
- <del>Faster Collision Detection</del>
  - <del>Each face will not have AABB</del> Each face still has an AABB, but it is only used if no other AABB is specified in the .OBJ file
  - <del>Each model will have AABB(s) specified by modeler / coder in another file or in the .OBJ file</del>
  - Possibly a tool to automate the process
- Rotational Physics
  - With the AABB revision above, the only thing that would need to be rotated is/are the AABB(s), then glRotatef() could be called
- <del>Support for animation</del>
  - Preferably without the use of a bunch of .OBJ files.
- Image-based GUI Support
- Add some of the memory-management that should already be in place (clean-up stuff)
<h2>Team</h2>
- Jared
- Ian
