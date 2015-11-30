#ifndef _462_PHYSICS_BODY_HPP_
#define _462_PHYSICS_BODY_HPP_

#include "math/vector.hpp"
#include "math/quaternion.hpp"
#include <exception>
#include <iostream>

//Joseph Added
#include "math/matrix.hpp"

#define PLANEBODY 0
#define SPHEREBODY 1
#define TRIANGLEBODY 2
#define MODELBODY 3

namespace _462 {

class BoundBox{
public:
    //the lowest coordinates in the box
    Vector3 lower;
    //the highest coordinates in the box
    Vector3 upper;
    //the center coordinates of the box
    Vector3 center;
    
    BoundBox(Vector3 l,Vector3 u):lower(l),upper(u), center((l+u)*0.5){}
    BoundBox(Vector3 a):lower(a),upper(a), center((a+a)*0.5){}
    BoundBox(BoundBox a,BoundBox b){
        lower.x=std::min(a.lower.x,b.lower.x);
        lower.y=std::min(a.lower.y,b.lower.y);
        lower.z=std::min(a.lower.z,b.lower.z);
        
        upper.x=std::max(a.upper.x,b.upper.x);
        upper.y=std::max(a.upper.y,b.upper.y);
        upper.z=std::max(a.upper.z,b.upper.z);
        
        center = (lower + upper) * 0.5;
    }
    BoundBox(){
        lower.x=INFINITY;
        lower.y=INFINITY;
        lower.z=INFINITY;
        
        upper.x=-INFINITY;
        upper.y=-INFINITY;
        upper.z=-INFINITY;
        
        center = (lower + upper) * 0.5;
    }
    
};
static std::ostream& operator<<( std::ostream& os, const BoundBox& b )
{
    return os << "Bound(" << b.lower << ", " << b.upper << ")";
}
    
class Body
{
public:
    int id;
    int type;
    Vector3 position;
    Quaternion orientation;
    Vector3 velocity;
    Vector3 angular_velocity;

    virtual ~Body() { }
    virtual Vector3 step_position( real_t dt, real_t motion_damping ) = 0;
    virtual Vector3 step_orientation( real_t dt, real_t motion_damping ) = 0;
    virtual void apply_force( const Vector3& f, const Vector3& offset ) = 0;
    
    //Joseph Added
    size_t code;
    BoundBox bound;
    
};

}

#endif
