#ifndef _462_CASTER_HPP_
#define _462_CASTER_HPP_

#include "math/color.hpp"
#include "math/random462.hpp"
#include "p3/photon.hpp"
#include "p3/neighbor.hpp"
#include "application/opengl.hpp"
#include "scene/ray.hpp"
#include "scene/scene.hpp"

namespace _462 {

// max number of threads OpenMP can use. Change this if you like. (Hint: set to 1 for debugging)
#define MAX_THREADS 1
    
//maximum depth of the recursive (sampling) tracing
#define MAX_DEPTH 10
    
//increase lighting by a factor
#define WATT_BOOST 10.0
    
//maximum depth of the recursive (photon) tracing
#define MAX_PHOTON_DEPTH 10
    
//total number of photons shot from light source
#define PHOTON_COUNT 0
    
//the ``k'' in k-nearest-neighbors. The number of photons used in each radiance estimate
#define PHOTON_SAMPLE_COUNT 0


    
//the number of samples used in the direct (shadow) estimate
#define DIRECT_SAMPLE_COUNT 10

real_t computeFresnelCoefficient(Intersection &next,Ray &ray,real_t index,real_t newIndex);
Vector3 reflect(Vector3 norm,Vector3 inc);
Vector3 refract(Vector3 norm,Vector3 inc,real_t ratio);
real_t montecarlo(Color3& light);

}
#endif /*_462_CASTER_HPP_*/