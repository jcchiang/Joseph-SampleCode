#include "scene/mesh.hpp"

namespace _462 {
    int add_edge(Mesh::MeshVertexList &vertices, int ver1, int ver2, int other){
        for(int i=0; i<vertices[ver2].newNeightbor.size(); i++){
            if(vertices[ver2].newNeightbor[i].second == ver1){
                vertices[vertices[ver2].newNeightbor[i].ID].down = other;
                return vertices[ver2].newNeightbor[i].ID;
            }
        }
        MeshVertex newVertex;
        newVertex.ID = vertices.size();
        newVertex.first = ver1;
        newVertex.second = ver2;
        newVertex.up = other;
        vertices.push_back(newVertex);
        vertices[ver1].newNeightbor.push_back(newVertex);
        return newVertex.ID;
    }
    /*
    int update_triangle(int ver1, int ver2){
        for(int i=0; i<data[ver1].size(); i++){
            if(data[ver1][i].second == ver2){
                return data[ver1][i].mid;
            }
        }
        for(int i=0; i<data[ver2].size(); i++){
            if(data[ver2][i].second == ver1){
                return data[ver2][i].mid;
            }
        }
        return -1;
    }
    */
    
    bool Mesh::subdivide()
    {
        /*
         You should implement loop subdivision here.
         
         Triangles are stored in an std::vector<MeshTriangle> in 'triangles'.
         Vertices are stored in an std::vector<MeshVertex> in 'vertices'.
         
         Check mesh.hpp for the Mesh class definition.
         */
        
        /*
         * Initialization
         */
        //data.clear();
        int old_vertex_size = vertices.size();
        int old_triangle_size = triangles.size();
        std::vector<int> evenNeightbor[200];
        //data = new std::vector<EdgeData>[old_vertex_size];
        for(int i=0; i<old_vertex_size; i++){
            vertices[i].ID = i;
            vertices[i].N = 0;
            vertices[i].sum = Vector3(0.0, 0.0, 0.0);
            evenNeightbor[i] = std::vector<int>();
            //std::vector<EdgeData> aEdgeVector;
            //data[i] = aEdgeVector;
            //data.push_back(aEdgeVector);
        }
        //std::cout<< data.size() << std::endl;
        
       
        
        /*
         * Preparation
         */
        for(int i=0; i<triangles.size(); i++){
            // Add Edge 1
            triangles[i].mid_points[0] = add_edge(vertices, triangles[i].vertices[0], triangles[i].vertices[1], triangles[i].vertices[2]);
            vertices[triangles[i].vertices[0]].N ++;
            vertices[triangles[i].vertices[0]].sum +=  vertices[triangles[i].vertices[1]].position;
            evenNeightbor[triangles[i].vertices[0]].push_back(triangles[i].vertices[1]);
            // Add Edge 2
            triangles[i].mid_points[1] = add_edge(vertices, triangles[i].vertices[1], triangles[i].vertices[2], triangles[i].vertices[0]);
            vertices[triangles[i].vertices[1]].N ++;
            vertices[triangles[i].vertices[1]].sum +=  vertices[triangles[i].vertices[2]].position;
            evenNeightbor[triangles[i].vertices[1]].push_back(triangles[i].vertices[2]);
            // Add Edge 3
            triangles[i].mid_points[2] = add_edge(vertices, triangles[i].vertices[2], triangles[i].vertices[0], triangles[i].vertices[1]);
            vertices[triangles[i].vertices[2]].N ++;
            vertices[triangles[i].vertices[2]].sum +=  vertices[triangles[i].vertices[0]].position;
            evenNeightbor[triangles[i].vertices[2]].push_back(triangles[i].vertices[0]);
        }
        /*
         for(int i=0; i<old_vertex_size; i++){
             std::cout<< "Vertex " << i << ": ";
             for(int j=0; j<vertices[i].newNeightbor.size(); j++){
                std::cout<< vertices[i].newNeightbor[j].second << ", ";
             }
             std::cout<< std::endl;
         }
        */
        
        /*
         * Add Odd Vertex
         */
        
        for(int i=old_vertex_size; i<vertices.size(); i++){
                if (vertices[i].down >= 0){
                    // Inner edge
                    vertices[i].position =
                    vertices[vertices[i].first].position * 0.375
                    + vertices[vertices[i].second].position * 0.375
                    + vertices[vertices[i].up].position * 0.125
                    + vertices[vertices[i].down].position * 0.125;
                }
                else{
                    // Boundary edge
                    vertices[i].position = vertices[vertices[i].first].position * 0.5
                    + vertices[vertices[i].second].position * 0.5;
                }
        }
        
        /*
         * Reform Triangles
         */
        
        for (int i = 0; i < old_triangle_size; i++){
            //int mid_vertex[3] = {-1, -1, -1};
            
            // Find Edge 1
            //mid_vertex[0] = update_triangle(triangles[i].vertices[0], triangles[i].vertices[1]);
            
            // Find Edge 2
            //mid_vertex[1] = update_triangle(triangles[i].vertices[1], triangles[i].vertices[2]);
            
            // Find Edge 3
            //mid_vertex[2] = update_triangle(triangles[i].vertices[2], triangles[i].vertices[0]);
            
            MeshTriangle new_triangle[3];
            for (int j = 0; j < 3; j++){
                new_triangle[j].vertices[0] = triangles[i].vertices[j];
                new_triangle[j].vertices[1] = triangles[i].mid_points[j];
                new_triangle[j].vertices[2] = triangles[i].mid_points[(j+2)%3];
                triangles.push_back(new_triangle[j]);
            }
            
            triangles[i].vertices[0] = triangles[i].mid_points[0];
            triangles[i].vertices[1] = triangles[i].mid_points[1];
            triangles[i].vertices[2] = triangles[i].mid_points[2];
        }
        
        for(int i=0; i<old_vertex_size; i++){
            for(int j=0; j<evenNeightbor[i].size();j++){
                printf("%d, ", evenNeightbor[i][j]);
                
                
                //printf("%lf,%lf,%lf ----- %d\n",vertices[i].sum.x, vertices[i].sum.y, vertices[i].sum.z, vertices[i].N);
                
            }
            printf("\n");
            printf("Vertex %d: %lf,%lf,%lf \n", i, vertices[i].position.x, vertices[i].position.y, vertices[i].position.z);
        }
        
        
        /*
         * Move Even Vertex
         */
        
        for (int i = 0; i < old_vertex_size; i++){
            if (vertices[i].N == 2){
                // EDGE
                vertices[i].position = vertices[i].position * 0.75 + vertices[i].sum * 0.125;
            }
            else{
                
               // INTERIOR
                float N_float = (float)vertices[i].N;
                float beta = 1.0 / N_float * (0.625 - (0.375 + 0.25 * cos(2.0 * PI / N_float)) * (0.375 + 0.25 * cos(2 * PI / N_float)));
                
                vertices[i].position = (1 - beta * N_float) * vertices[i].position + beta * vertices[i].sum;
              
            }
            
            //printf("%lf,%lf,%lf\n",vertices[i].position.x, vertices[i].position.y, vertices[i].position.z);
        }
        
        /*
         * Final output
         */
        has_tcoords = false;
        has_normals = false;
        create_gl_data();
        
        
        return true;
    }
    
} /* _462 */
