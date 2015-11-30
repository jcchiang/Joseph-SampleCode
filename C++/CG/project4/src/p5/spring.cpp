#include "math/math.hpp"
#include "math/vector.hpp"
#include "math/quaternion.hpp"
#include "math/matrix.hpp"
#include "p5/spring.hpp"
#include "p5/body.hpp"
#include "p5/spherebody.hpp"
#include <iostream>

namespace _462 {

Spring::Spring()
{
    body1_offset = Vector3::Zero();
    body2_offset = Vector3::Zero();
    damping = 0.0;
}

void Spring::step( real_t dt )
{
    // TODO apply forces to attached bodies
    
    Vector3 offset1 = body1->orientation * body1_offset;
    Vector3 offset2 = body2->orientation * body2_offset;
    
    Vector3 point1 = body1->position + offset1;
    Vector3 point2 = body2->position + offset2;
    
    Vector3 dir1 = point1 - point2;
    Vector3 dir2 = point2 - point1;
    
    //real_t dist = length(dir1);
    
    real_t v1 = dot(body1->velocity, normalize(dir1));
    real_t v2 = dot(body2->velocity, normalize(dir2));
    
    Vector3 force1 = (-constant * (length(dir1) - equilibrium) - damping * v1 ) * normalize(dir1);
    Vector3 force2 = (-constant * (length(dir2) - equilibrium) - damping * v2 ) * normalize(dir2);
    
    body1->apply_force(force1, offset1);
    body2->apply_force(force2, offset2);
}

}
