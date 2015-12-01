//
//  trianglebody.hpp
//  P4
//
//  Created by Joseph Chiang on 4/22/15.
//

#ifndef _462_PHYSICS_TRIANGLE_HPP_
#define _462_PHYSICS_TRIANGLE_HPP_

#include "scene/triangle.hpp"
#include "p5/body.hpp"
#include "p5/spherebody.hpp"
#include "math/vector.hpp"

namespace _462 {

class Triangle;

class TriangleBody : public Body
{
public:
    TriangleBody( Triangle* geom );
    virtual ~TriangleBody() { }
    virtual Vector3 step_position( real_t dt, real_t motion_damping );
    virtual Vector3 step_orientation( real_t dt, real_t motion_damping );
    virtual void apply_force( const Vector3& f, const Vector3& offset );

    Vector3 vertices[3];
    
    TriangleBody( Vector3 a, Vector3 b, Vector3 c);
};

}

#endif


