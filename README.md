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
- Support for GLSL Post-Processing effects like motion blur, and depth of field
- Shadows 
  - http://www.youtube.com/watch?v=IiqiCVs3R4Q
  - http://www.opengl.org/discussion_boards/showthread.php/181334-LWJGL-Shadow-Mapping
  - http://en.wikipedia.org/wiki/Shadow_mapping
  - http://en.wikipedia.org/wiki/Shadow_volume
  - http://www.dreamincode.net/forums/topic/149241-shadow-mapping/
  - http://www.flyingkakapo.co.nz/Code/Lesson27Fixed.java
  - http://flyingkakapo.blogspot.com/2012/10/shadow-volumes-with-lwjgl.html
- Binary Space Partitioning or chunks to be used in collision detection (grouping of nearby vertices)
  - Collision detection will not be done per object, but per chunk or per patrition. A list of all Faces in game would need to be added to World
<h2>Team</h2>
- Jared (fiendfan1@yahoo.com)
- Ian (Put email here if you want)
