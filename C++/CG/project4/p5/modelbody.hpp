#ifndef _462_PHYSICS_MODEL_HPP_
#define _462_PHYSICS_MODEL_HPP_

#include "p5/body.hpp"
#include "math/vector.hpp"
#include "math/quaternion.hpp"

//Joseph Added
#include "scene/model.hpp"
#include "p5/trianglebody.hpp"
#include "p5/octreenode.hpp"

namespace _462 {

class Model;

class ModelBody : public Body
{
public:
    Model* model;

    ModelBody( Model* geom );
    virtual ~ModelBody();
    virtual Vector3 step_position( real_t dt, real_t motion_damping );
    virtual Vector3 step_orientation( real_t dt, real_t motion_damping );
    virtual void apply_force( const Vector3& f, const Vector3& offset );
    
    //Joseph Added
    Matrix4 mat;
    OctreeNodes *model_root;
    std::vector<TriangleBody*> bodies;
    void initialize();
    
};

}

#endif


