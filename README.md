<h1>Your Future Needs Help</h1>
<h2>Todo</h2>
- Interleaved VBOs
- Game Content
  - Models / Textures / Assets
  - Storyline
- Faster Collision Detection
  - Each face will not have AABB
  - Each model will have AABB(s) specified by modeler / coder in another file or in the .OBJ file
  - Possibly a tool to automate the process
- Rotational Physics
  - With the AABB revision above, the only thing that would need to be rotated is/are the AABB(s), then glRotatef() could be called
- Support for animation
  - Preferably without the use of a bunch of .OBJ files, but with a seperate animation file. This would have to be manually "coded" or a tool could be made
<h2>Team</h2>
- Jared
- Ian
