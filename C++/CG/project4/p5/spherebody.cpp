#include "p5/spherebody.hpp"
#include "math/vector.hpp"
#include "math/matrix.hpp"
#include "scene/sphere.hpp"
#include <iostream>
#include <exception>
#include <algorithm>

namespace _462 {

SphereBody::SphereBody( Sphere* geom )
{
    sphere = geom;
    position = sphere->position;
    radius = sphere->radius;
    orientation = sphere->orientation;
    mass = 0.0;
    velocity = Vector3::Zero();
    angular_velocity = Vector3::Zero();
    force = Vector3::Zero();
    torque = Vector3::Zero();
    
    //Joseph Added
    type = SPHEREBODY;
    Vector3 upper = position + Vector3(radius, radius, radius);
    Vector3 lower = position - Vector3(radius, radius, radius);
    bound = BoundBox(lower, upper);
}

Vector3 SphereBody::step_position( real_t dt, real_t motion_damping )
{
    // Note: This function is here as a hint for an approach to take towards
    // programming RK4, you should add more functions to help you or change the
    // scheme
    // TODO return the delta in position dt in the future
    //Vector3 upper = position + Vector3(radius, radius, radius);
    //Vector3 lower = position - Vector3(radius, radius, radius);
    //bound = BoundBox(lower, upper);
    //std::cout<<bound<<std::endl;
    return velocity * dt;
}

Vector3 SphereBody::step_orientation( real_t dt, real_t motion_damping )
{
    // Note: This function is here as a hint for an approach to take towards
    // programming RK4, you should add more functions to help you or change the
    // scheme
    // TODO return the delta in orientation dt in the future
    // vec.x = rotation along x axis
    // vec.y = rotation along y axis
    // vec.z = rotation along z axis

    return angular_velocity * dt;
}

void SphereBody::apply_force( const Vector3& f, const Vector3& offset )
{
    // TODO apply force/torque to sphere
    if(length(offset) < 0.0001){
        force += f;
        torque += Vector3::Zero();
    }
    else{
        force += dot(f, offset) * offset / length(offset) / length(offset);
        torque += cross(offset, f);
    }
}
    
void SphereBody::clear_force(){
    force = Vector3::Zero();
    torque = Vector3::Zero();
}
}
