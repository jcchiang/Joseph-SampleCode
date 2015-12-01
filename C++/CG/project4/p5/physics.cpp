#include "p5/physics.hpp"

namespace _462 {

Physics::Physics()
{
    reset();
}

Physics::~Physics()
{
    reset();
}

//Joseph Added
void Physics::initialize(){
    for(size_t i = 0; i < num_models(); i++){
        models[i]->initialize();
    }
    std::cout<<"Valid number of models: "<<num_models()<<std::endl;
}
    
void Physics::step( real_t dt )
{
    // TODO step the world forward by dt. Need to detect collisions, apply
    // forces, and integrate positions and orientations.
    //
    // Note: put RK4 here, not in any of the physics bodies
    //
    // Must use the functions that you implemented
    //
    // Note, when you change the position/orientation of a physics object,
    // change the position/orientation of the graphical object that represents
    // it
    
    //std::cout<<"Gravity = " << this->gravity << std::endl;
    
    
    for(size_t i=0; i<num_spheres(); i++){
        //Update Spheres boundbox
        Vector3 upper = spheres[i]->position + Vector3(spheres[i]->radius, spheres[i]->radius, spheres[i]->radius);
        Vector3 lower = spheres[i]->position - Vector3(spheres[i]->radius, spheres[i]->radius, spheres[i]->radius);
        spheres[i]->bound = BoundBox(lower, upper);
        
        for(size_t j=0; j<num_spheres(); j++){
            if(i!=j){
                collides(*spheres[i], *spheres[j], collision_damping);
            }
        }
        for(size_t j=0; j<num_planes(); j++){
            collides(*spheres[i], *planes[j], collision_damping);
        }
        for(size_t j=0; j<num_triangles(); j++){
            collides(*spheres[i], *triangles[j], collision_damping);
        }
        for(size_t j=0; j<num_models(); j++){
            collides(*spheres[i], *models[j], collision_damping);
        }
        
        //RK4 Position & Orientation
        rk4_update(*spheres[i], dt);
        
        spheres[i]->clear_force();
    }
    
    
}

Quaternion rk4_orientation_update(Quaternion ori, Vector3 k){
    Quaternion result = ori;
    result = normalize(Quaternion(result * Vector3::UnitX(), k.x) * result);
    result = normalize(Quaternion(result * Vector3::UnitY(), k.y) * result);
    result = normalize(Quaternion(result * Vector3::UnitZ(), k.z) * result);
    return result;
}
    
void Physics::rk4_update(SphereBody &s, real_t dt){
    // Store Original Status
    // -- position
    Vector3 originalPosition = s.position;
    Vector3 originalVelocity = s.velocity;
    // -- rotation
    Quaternion originalOrientation = s.orientation;
    Vector3 originalAngVelocity = s.angular_velocity;
    real_t I = 0.4 * s.mass * s.radius * s.radius;
    
    //Step 1
    // -- apply force
    s.clear_force();
    s.apply_force(gravity, Vector3::Zero());
    for(size_t j=0; j<num_springs(); j++){
        springs[j]->step(dt);
    }
    
    // -- position
    Vector3 v_v1 = Vector3::Zero();
    s.velocity = originalVelocity + v_v1;
    Vector3 v_k1 = s.step_position(dt, 0.0);
    s.position = originalPosition + v_k1 / 2.0;
    // -- rotation
    Vector3 a_v1 = Vector3::Zero();
    s.angular_velocity = originalAngVelocity + a_v1;
    Vector3 a_k1 = s.step_orientation(dt, 0.0);
    s.orientation = rk4_orientation_update(originalOrientation, a_k1/2.0);
    
    //Step 2
    // -- apply force
    s.clear_force();
    s.apply_force(gravity, Vector3::Zero());
    for(size_t j=0; j<num_springs(); j++){
        springs[j]->step(dt);
    }
    
    // -- position
    Vector3 v_v2 = (s.force / s.mass) * (dt / 2.0);
    s.velocity = originalVelocity + v_v2;
    Vector3 v_k2 = s.step_position(dt, 0.0);
    s.position = originalPosition + v_k2 / 2.0;
    // -- rotation
    Vector3 a_v2 = (s.torque / I) * (dt / 2.0);
    s.angular_velocity = originalAngVelocity + a_v2;
    Vector3 a_k2 = s.step_orientation(dt, 0.0);
    s.orientation = rk4_orientation_update(originalOrientation, a_k2/2.0);
    
    //Step 3
    // -- apply force
    s.clear_force();
    s.apply_force(gravity, Vector3::Zero());
    for(size_t j=0; j<num_springs(); j++){
        springs[j]->step(dt);
    }
    
    // -- position
    Vector3 v_v3 = (s.force / s.mass) * (dt / 2.0);
    s.velocity = originalVelocity + v_v3;
    Vector3 v_k3 = s.step_position(dt, 0.0);
    s.position = originalPosition + v_k3 / 2.0;
    // -- rotation
    Vector3 a_v3 = (s.torque / I) * (dt / 2.0);
    s.angular_velocity = originalAngVelocity + a_v3;
    Vector3 a_k3 = s.step_orientation(dt, 0.0);
    s.orientation = rk4_orientation_update(originalOrientation, a_k3/1.0);
    
    
    //Step 4
    // -- apply force
    s.clear_force();
    s.apply_force(gravity, Vector3::Zero());
    for(size_t j=0; j<num_springs(); j++){
        springs[j]->step(dt);
    }
    
    // -- position
    Vector3 v_v4 = (s.force / s.mass) * dt;
    s.velocity = originalVelocity + v_v4;
    Vector3 v_k4 = s.step_position(dt, 0.0);
    s.position = originalPosition + v_k4 / 2.0;
    // -- rotation
    Vector3 a_v4 = (s.torque / I) * dt;
    s.angular_velocity = originalAngVelocity + a_v4;
    Vector3 a_k4 = s.step_orientation(dt, 0.0);
    
    
    // Final Update
    // -- position
    Vector3 deltaPosition = v_k1 * (1.0/6.0) + v_k2 * (2.0/6.0) + v_k3 * (2.0/6.0) + a_k4 * (1.0/6.0);
    Vector3 deltaVelocity = v_v1 * (1.0/6.0) + v_v2 * (2.0/6.0) + v_v3 * (2.0/6.0) + v_v4 * (1.0/6.0);
    s.sphere->position = originalPosition + deltaPosition;
    s.velocity = originalVelocity + deltaVelocity;
    // -- rotation
    Vector3 deltaRotation = a_k1 * (1.0/6.0) + a_k2 * (2.0/6.0) + a_k3 * (2.0/6.0) + a_k4 * (1.0/6.0);
    Vector3 deltaAngularV = a_v1 * (1.0/6.0) + a_v2 * (2.0/6.0) + a_v3 * (2.0/6.0) + a_v4 * (1.0/6.0);
    s.sphere->orientation = rk4_orientation_update(originalOrientation, deltaRotation/1.0);
    s.angular_velocity = originalAngVelocity + deltaAngularV;
}

void Physics::add_sphere( SphereBody* b )
{
    spheres.push_back( b );
}

size_t Physics::num_spheres() const
{
    return spheres.size();
}

void Physics::add_plane( PlaneBody* p )
{
    planes.push_back( p );
}

size_t Physics::num_planes() const
{
    return planes.size();
}

void Physics::add_triangle( TriangleBody* t )
{
    triangles.push_back( t );
}

size_t Physics::num_triangles() const
{
    return triangles.size();
}

void Physics::add_model( ModelBody* m )
{
    models.push_back( m );
}

size_t Physics::num_models() const
{
    return models.size();
}

void Physics::add_spring( Spring* s )
{
    springs.push_back( s );
}

size_t Physics::num_springs() const
{
    return springs.size();
}

void Physics::reset()
{
    for ( SphereList::iterator i = spheres.begin(); i != spheres.end(); i++ ) {
        delete *i;
    }
    for ( PlaneList::iterator i = planes.begin(); i != planes.end(); i++ ) {
        delete *i;
    }
    for ( TriangleList::iterator i = triangles.begin(); i != triangles.end(); i++ ) {
        delete *i;
    }
    for ( ModelList::iterator i = models.begin(); i != models.end(); i++) {
        delete *i;
    }
    for ( SpringList::iterator i = springs.begin(); i != springs.end(); i++ ) {
        delete *i;
    }

    spheres.clear();
    planes.clear();
    triangles.clear();
    springs.clear();
    models.clear();

    gravity = Vector3::Zero();
	collision_damping = 0.0;
}

}
