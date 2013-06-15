varying float intensity;

void main()
{
	vec3 vertexPosition = (gl_ModelViewMatrix * gl_Vertex).xyz;
	vec3 lightDir = normalize(gl_LightSource[0].position.xyz - vertexPosition);
	intensity = dot(lightDir, gl_Normal);
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}