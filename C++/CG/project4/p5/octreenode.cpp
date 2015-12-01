//
//  octreenode.cpp
//  P4
//
//  Created by Joseph Chiang on 4/30/15.
//
//

#include "p5/octreenode.hpp"

namespace _462 {
    
OctreeNodes::OctreeNodes(){
    center = Vector3::Zero();
    radius = 0;
    num_geometry = 0;
    for(int i=0; i<8; i++){
        leafs[i] = NULL;
    }
    bound = BoundBox(Vector3::Zero());
}
    
OctreeNodes::~OctreeNodes(){
    for (size_t i=0; i<8; i++) {
        delete leafs[i];
    }
}

bool OctreeNodes::intersect(BoundBox bbox1, BoundBox bbox2){
    if(bbox1.upper.x < bbox2.lower.x) return false;
    if(bbox1.upper.y < bbox2.lower.y) return false;
    if(bbox1.upper.z < bbox2.lower.z) return false;
    if(bbox1.lower.x > bbox2.upper.x) return false;
    if(bbox1.lower.y > bbox2.upper.y) return false;
    if(bbox1.lower.z > bbox2.upper.z) return false;
    return true;
}
    
bool OctreeNodes::check_leaves(Body &body, int leafnum){
    Vector3 offset = boundsOffsetTable[leafnum] * radius;
    
    Vector3 _center = bound.center + offset;
    real_t _radius = radius * 0.5;
    BoundBox temp_bound = BoundBox(_center - Vector3(_radius, _radius, _radius), _center + Vector3(_radius, _radius, _radius));
    //std::cout<<_radius<<std::endl;
    //std::cout<<"BOUND " << leafnum << ": " << temp_bound<< std::endl;
    if( intersect(body.bound, temp_bound))
    {
        return true;
    }
    return false;
}
    
void OctreeNodes::initialize(std::vector<Body*> &geometryList,
                             const int count,
                             const int threshold,
                             const size_t currentDepth,
                             const size_t maxDepth)
{
    if(count <= threshold || currentDepth >= maxDepth){
        for(int i=0; i<count; i++){
            bodies.push_back(geometryList[i]);
        }
        num_geometry = count;
        return;
    }
    
    std::vector<Body*> newGeomLists[8];
    for(size_t i=0; i<8; i++){
        newGeomLists[i] = std::vector<Body*>();
    }
    
    for(size_t i=0; i<count; i++){
        
        Body &geom = *geometryList[i];
        for(size_t j=0; j<8; j++){
            if(check_leaves(geom, j)){
                newGeomLists[j].push_back(&geom);
            }
        }
    }
    for(size_t i=0; i<8; i++){
        if(newGeomLists[i].size() == 0){
            continue;
        }
        
        leafs[i] = new OctreeNodes();
        
        Vector3 offset = boundsOffsetTable[i] * radius;
        
        leafs[i]->center = center + offset;
        leafs[i]->radius = radius * 0.5;
        leafs[i]->bound = BoundBox(
            leafs[i]->center - Vector3(leafs[i]->radius, leafs[i]->radius, leafs[i]->radius),
            leafs[i]->center + Vector3(leafs[i]->radius, leafs[i]->radius, leafs[i]->radius)
        );
        
        leafs[i]->initialize(newGeomLists[i], newGeomLists[i].size(), threshold, currentDepth+1, maxDepth);
    }
    
}


    
void OctreeNodes::query(){
    //std::cout<<this->center;
}

}