#include "p5/collisions.hpp"
#include "scene/model.hpp"
#include "math/matrix.hpp"

namespace _462 {

bool collides( SphereBody& body1, SphereBody& body2, real_t collision_damping )
{
    // TODO detect collision. If there is one, update velocity
    real_t dis = distance(body1.position, body2.position);
    if(dis < body1.radius + body2.radius){
        // Collision occured
        
        // Relative velocity should be positive, else ignore
        Vector3 v1_prime = body1.velocity - body2.velocity;
        if(dot(body2.position - body1.position, v1_prime) < 0){
            return false;
        }
        
        Vector3 d = normalize(body2.position - body1.position);
        Vector3 v2_prime_prime = 2 * d * body1.mass * dot(v1_prime, d) / (body1.mass + body2.mass);
        
        Vector3 u2 = body2.velocity + v2_prime_prime;
        Vector3 u1 = (body1.mass * body1.velocity + body2.mass * body2.velocity - body2.mass * u2) / body1.mass;
        
        body1.velocity = u1 - u1 * collision_damping;
        body2.velocity = u2 - u2 * collision_damping;
        return true;
    }
    return false;
}

bool collides( SphereBody& body1, TriangleBody& body2, real_t collision_damping )
{
    // TODO detect collision. If there is one, update velocity
    if(project_onto_triangle(body1, body2.vertices[0], body2.vertices[1], body2.vertices[2])){
        //Collision occured
        if(length(body1.velocity) < 0.001)
            body1.velocity = Vector3::Zero();
        else{
            body1.velocity -= body1.velocity * collision_damping;
        }
        return true;
    }
    else{
        return false;
    }
}

bool collides( SphereBody& body1, PlaneBody& body2, real_t collision_damping )
{
    // TODO detect collision. If there is one, update velocity
    Vector3 aVector = body1.position - body2.position;
    real_t dis = dot(aVector, body2.normal);
    if(fabs(dis) < body1.radius){
        // Collision occured
        // New velocity: u = v − 2(v · n)n
        body1.velocity = body1.velocity - 2 * dot(body1.velocity, body2.normal) * body2.normal;
        return true;
    }
	return false;
}

bool collides( SphereBody& body1, ModelBody& body2, real_t collision_damping )
{
    // TODO detect collision. If there is one, update velocity
    
    
    collides(body1, *body2.model_root, collision_damping);
    
    /*
    Vector3 vertex[3];
    for(size_t i = 0; i < body2.bodies.size(); i++){
        vertex[0] = body2.bodies[i]->vertices[0];
        vertex[1] = body2.bodies[i]->vertices[1];
        vertex[2] = body2.bodies[i]->vertices[2];
        
        if(project_onto_triangle(body1, vertex[0], vertex[1], vertex[2])){
            return true;
        }
    }
    return false;
    */
    /*
    Vector3 vertex[3];
    
    const MeshTriangle* triangles = body2.model->mesh->get_triangles();
    const MeshVertex* vertices = body2.model->mesh->get_vertices();
    for(size_t i = 0; i < body2.model->mesh->num_triangles(); i++){
        // TODO Check each triangle
        vertex[0] = body2.mat.transform_point(vertices[triangles[i].vertices[0]].position);
        vertex[1] = body2.mat.transform_point(vertices[triangles[i].vertices[1]].position);
        vertex[2] = body2.mat.transform_point(vertices[triangles[i].vertices[2]].position);

        if(project_onto_triangle(body1, vertex[0], vertex[1], vertex[2])){
            std::cout<<"Velocity after: "<<body1.velocity.y<<std::endl;
            return true;
        }
    }
    return false;
    */
    
}

bool check_intersect(BoundBox bbox1, BoundBox bbox2){
    if(bbox1.upper.x < bbox2.lower.x) return false;
    if(bbox1.upper.y < bbox2.lower.y) return false;
    if(bbox1.upper.z < bbox2.lower.z) return false;
    if(bbox1.lower.x > bbox2.upper.x) return false;
    if(bbox1.lower.y > bbox2.upper.y) return false;
    if(bbox1.lower.z > bbox2.upper.z) return false;
    return true;
}

bool collides( SphereBody& body1, OctreeNodes& body2, real_t collision_damping){
    //std::cout<<"HELLO"<<std::endl;
    bool isBottom = true;
    for(size_t i=0; i<8; i++){
        if(body2.leafs[i] != NULL){
            isBottom = false;
            break;
        }
    }
    //std::cout<<isBottom<<std::endl;
    
    if(isBottom){
        //std::cout<<"isBottom!!"<<std::endl;
        for(size_t i=0; i<body2.bodies.size(); i++){
            switch(body2.bodies[i]->type){
                case TRIANGLEBODY:
                    TriangleBody* tri = (TriangleBody*)body2.bodies[i];
                    //return collides(body1, *tri, collision_damping);
                    
                    Vector3 vertex[3];
                    vertex[0] = tri->vertices[0];
                    vertex[1] = tri->vertices[1];
                    vertex[2] = tri->vertices[2];
                    
                    if(project_onto_triangle(body1, vertex[0], vertex[1], vertex[2])){
                        //std::cout<<"HIT"<<std::endl;
                        return true;
                    };
                    
                    break;
            }
        }
        return false;
    }
    else{
        for(size_t i=0; i<8; i++){
            if(body2.leafs[i] != NULL){
                if(check_intersect(body1.bound, body2.leafs[i]->bound)){
                    //std::cout<<"NEXT STAGE"<<std::endl;
                    return collides(body1, *body2.leafs[i], collision_damping);
                }
            }
        }
    }
}

    
bool project_onto_triangle(SphereBody &body1, Vector3 p0, Vector3 p1, Vector3 p2){
    // Pre calculation
    Vector3 n = normalize(cross(p1 - p0, p2 - p0));
    Vector3 aVector = body1.position - p0;
    real_t d = dot(aVector, n);
    
    // Check relative velocity
    if(dot(body1.velocity, n) > 0){
        return false;
    }
    
    // Check distance
    if(length(d * n) >= body1.radius){
        return false;
    }
    
    // Find point on the plane
    Vector3 p_prime = body1.position - d * n;
    
    // Compute vectors
    Vector3 v0 = p2- p0;
    Vector3 v1 = p1- p0;
    Vector3 v2 = aVector;
    
    // Compute dot products
    real_t dot00 = dot(v0, v0);
    real_t dot01 = dot(v0, v1);
    real_t dot02 = dot(v0, v2);
    real_t dot11 = dot(v1, v1);
    real_t dot12 = dot(v1, v2);
    
    // Compute barycentric coordinates
    real_t invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
    real_t u = (dot11 * dot02 - dot01 * dot12) * invDenom;
    real_t v = (dot00 * dot12 - dot01 * dot02) * invDenom;
    
    // Check if point is in triangle
    if(((u >= 0.0) && (v >= 0.0) && (u + v < 1.0)) || project_onto_edges(body1, p_prime, p0, p1, p2)){
        //If center is in the trianlge
        
        Vector3 newV = body1.velocity - 2 * dot(body1.velocity, n) * n;
        body1.velocity = newV;
        
        return true;
    }
    return false;
}
    

bool project_onto_edges(SphereBody sphere, Vector3 p_prime, Vector3 p0, Vector3 p1, Vector3 p2){
    // Project a point outside of triangle onto 3 edges
    // Edge 1
    real_t attr_p0 = dot(p_prime - p0, p1 - p0) / distance(p1, p0) / distance(p1, p0);
    Vector3 p_prime_prime_a = attr_p0 * (p1 - p0) + p0;
    if(attr_p0 >= 0.0 && attr_p0 <= 1.0 && distance(sphere.position, p_prime_prime_a) < sphere.radius){
        return true;
    }
    // Edge 2
    real_t attr_p1 = dot(p_prime - p1, p2 - p1) / distance(p2, p1) / distance(p2, p1);
    Vector3 p_prime_prime_b = attr_p1 * (p2 - p1)+ p1;
    if(attr_p1 >= 0.0 && attr_p1 <= 1.0 && distance(sphere.position, p_prime_prime_b) < sphere.radius){
        return true;
    }
    // Edge 3
    real_t attr_p2 = dot(p_prime - p2, p0 - p2) / distance(p0, p2) / distance(p0, p2);
    Vector3 p_prime_prime_c = attr_p2 * (p0 - p2) + p2;
    if(attr_p2 >= 0.0 && attr_p2 <= 1.0 && distance(sphere.position, p_prime_prime_c) < sphere.radius){
        return true;
    }
    return false;
}
    
}
