//
//  planebody.cpp
//  P4
//
//  Created by Joseph Chiang on 4/22/15.
//

#include "p5/planebody.hpp"
#include <iostream>

namespace _462 {

PlaneBody::PlaneBody()
{
    type = PLANEBODY;
    orientation = Quaternion::Identity();
}

Vector3 PlaneBody::step_position( real_t dt, real_t motion_damping )
{
    return Vector3::Zero();
}

Vector3 PlaneBody::step_orientation( real_t dt, real_t motion_damping )
{
    return Vector3::Zero();
}

void PlaneBody::apply_force( const Vector3& f, const Vector3& offset )
{
    return;
}

}
