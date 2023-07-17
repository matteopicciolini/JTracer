import trimesh
import numpy as np

file_path = "/content/ваз 2108.obj"
mesh = trimesh.load(file_path)

if isinstance(mesh, trimesh.Scene):
    # If the OBJ file contains multiple objects, take only the first one
    mesh = mesh.geometry[list(mesh.geometry.keys())[0]]

# Get the vertices and triangles
vertices = mesh.vertices
triangles = mesh.faces

# Determine the maximum and minimum values for each vertex dimension
x_min = np.min(vertices[:, 0])
x_max = np.max(vertices[:, 0])
y_min = np.min(vertices[:, 1])
y_max = np.max(vertices[:, 1])
z_min = np.min(vertices[:, 2])
z_max = np.max(vertices[:, 2])

# Normalize the vertices to the range [0, 1]
normalized_vertices = (vertices - [x_min, y_min, z_min]) / [x_max - x_min, y_max - y_min, z_max - z_min]

# Open the output file for writing
output_file = open("output.txt", "w")

# Write the normalized vertices to the file
for vertex in normalized_vertices:
    output_file.write(f"v {vertex[0]} {vertex[1]} {vertex[2]}\n")

# Write the triangles to the file
for triangle in triangles:
    output_file.write(f"f {triangle[0] + 1} {triangle[1] + 1} {triangle[2] + 1}\n")

# Close the output file
output_file.close()
