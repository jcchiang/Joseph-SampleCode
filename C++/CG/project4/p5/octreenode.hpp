//
//  octreenode.hpp
//  P4
//
//  Created by Joseph Chiang on 4/30/15.
//
//

#ifndef P4_octreenode_hpp
#define P4_octreenode_hpp

#include "p5/body.hpp"
#include "math/vector.hpp"
#include "math/quaternion.hpp"

#include <vector>

namespace _462 {
    
class OctreeNodes{
public:
    Vector3 center;
    real_t radius;
    int num_geometry;
    OctreeNodes *leafs[8];
    std::vector<Body*> bodies;
    BoundBox bound;
    
    OctreeNodes();
    ~OctreeNodes();
    
    void initialize(std::vector<Body*> &geometryList,
                    const int count,
                    const int threshold,
                    const size_t currentDepth,
                    const size_t maxDepth);
    bool check_leaves(Body &body, int leafID);
    bool intersect(BoundBox bbox1, BoundBox bbox2);
    void query();
    
private:
    Vector3 boundsOffsetTable[8] =
    {
        Vector3(-0.5, -0.5, -0.5),
        Vector3(+0.5, -0.5, -0.5),
        Vector3(-0.5, +0.5, -0.5),
        Vector3(+0.5, +0.5, -0.5),
        Vector3(-0.5, -0.5, +0.5),
        Vector3(+0.5, -0.5, +0.5),
        Vector3(-0.5, +0.5, +0.5),
        Vector3(+0.5, +0.5, +0.5)
    };
    
};
    
}

#endif
