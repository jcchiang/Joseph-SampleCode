//
//  trianglebody.cpp
//  P4
//
//  Created by Joseph Chiang on 4/22/15.
//
//

#include "p5/trianglebody.hpp"
#include <iostream>

namespace _462 {

TriangleBody::TriangleBody( Triangle* geom )
{
    vertices[0] = geom->vertices[0].position;
    vertices[1] = geom->vertices[1].position;
    vertices[2] = geom->vertices[2].position;
    orientation = Quaternion::Identity();
    velocity = Vector3::Zero();
    
    type = TRIANGLEBODY;
    real_t max_x = std::max(vertices[0].x, std::max(vertices[1].x, vertices[2].x));
    real_t max_y = std::max(vertices[0].y, std::max(vertices[1].y, vertices[2].y));
    real_t max_z = std::max(vertices[0].z, std::max(vertices[1].z, vertices[2].z));
    Vector3 upper(max_x, max_y, max_z);
    
    real_t min_x = std::min(vertices[0].x, std::min(vertices[1].x, vertices[2].x));
    real_t min_y = std::min(vertices[0].y, std::min(vertices[1].y, vertices[2].y));
    real_t min_z = std::min(vertices[0].z, std::min(vertices[1].z, vertices[2].z));
    Vector3 lower(min_x, min_y, min_z);
    
    bound = BoundBox(lower, upper);
}

TriangleBody::TriangleBody(Vector3 a, Vector3 b, Vector3 c){
    vertices[0] = a;
    vertices[1] = b;
    vertices[2] = c;
    orientation = Quaternion::Identity();
    velocity = Vector3::Zero();
    
    type = TRIANGLEBODY;
    real_t max_x = std::max(vertices[0].x, std::max(vertices[1].x, vertices[2].x));
    real_t max_y = std::max(vertices[0].y, std::max(vertices[1].y, vertices[2].y));
    real_t max_z = std::max(vertices[0].z, std::max(vertices[1].z, vertices[2].z));
    Vector3 upper(max_x, max_y, max_z);
    
    real_t min_x = std::min(vertices[0].x, std::min(vertices[1].x, vertices[2].x));
    real_t min_y = std::min(vertices[0].y, std::min(vertices[1].y, vertices[2].y));
    real_t min_z = std::min(vertices[0].z, std::min(vertices[1].z, vertices[2].z));
    Vector3 lower(min_x, min_y, min_z);
    
    bound = BoundBox(lower, upper);
}

Vector3 TriangleBody::step_position( real_t dt, real_t motion_damping )
{
    return Vector3::Zero();
}

Vector3 TriangleBody::step_orientation( real_t dt, real_t motion_damping )
{
    return Vector3::Zero();
}

void TriangleBody::apply_force( const Vector3& f, const Vector3& offset )
{
    return;
}

}
