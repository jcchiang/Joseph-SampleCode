#ifndef _462_COLLISIONS_HPP_
#define _462_COLLISIONS_HPP_

#include "scene/sphere.hpp"
#include "p5/spherebody.hpp"
#include "p5/trianglebody.hpp"
#include "p5/planebody.hpp"
#include "p5/modelbody.hpp"
//Joseph Added
#include "p5/octreenode.hpp"

namespace _462 {

bool collides( SphereBody& body1, SphereBody& body2, real_t collision_damping );
bool collides( SphereBody& body1, TriangleBody& body2, real_t collision_damping );
bool collides( SphereBody& body1, PlaneBody& body2, real_t collision_damping );
bool collides( SphereBody& body1, ModelBody& body2, real_t collision_damping );
//Joseph Added
bool collides( SphereBody& body1, OctreeNodes& body2, real_t collision_damping );

bool project_onto_triangle(SphereBody &body1, Vector3 p0, Vector3 p1, Vector3 p2);
bool project_onto_edges(SphereBody sphere, Vector3 p_prime, Vector3 p0, Vector3 p1, Vector3 p2);

}

#endif
