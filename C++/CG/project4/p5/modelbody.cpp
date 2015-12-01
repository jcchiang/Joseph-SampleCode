#include "p5/modelbody.hpp"
#include <iostream>

namespace _462 {

ModelBody::ModelBody( Model* geom )
{
    model = geom;
    
    // Joseph Added
    type = MODELBODY;
    
    mat = Matrix4::Zero();
    model_root = new OctreeNodes();
}
    
ModelBody::~ModelBody(){
    for(size_t i=0; i<bodies.size(); i++){
        delete bodies[i];
    }
    delete model_root;
    model_root = NULL;
    
}
    
//Joseph Added
void ModelBody::initialize(){
    make_transformation_matrix(&mat, model->position, model->orientation, model->scale);
    
    const MeshTriangle* triangles = model->mesh->get_triangles();
    const MeshVertex* vertices = model->mesh->get_vertices();
    for(size_t i = 0; i < model->mesh->num_triangles(); i++){
        Vector3 a = mat.transform_point(vertices[triangles[i].vertices[0]].position);
        Vector3 b = mat.transform_point(vertices[triangles[i].vertices[1]].position);
        Vector3 c = mat.transform_point(vertices[triangles[i].vertices[2]].position);
        
        TriangleBody *temp = new TriangleBody(a, b, c);
        bodies.push_back(temp);
        //std::cout<<bodies.size()<<std::endl;
    }
    // Create Boundbox for the model
    bound = bodies[0]->bound;
    for(size_t i=1; i<bodies.size(); i++){
        bound = BoundBox(bound, bodies[i]->bound);
    }
    
    // Create root node for Octree
    model_root = new OctreeNodes();
    std::vector<Body*> aList;
    for(size_t i=0; i<bodies.size(); i++){
        aList.push_back((Body*) bodies[i]);
    }
    
    // Boundbox setting for Octree's root node
    model_root->center = bound.center;
    
    real_t b_radius = std::max(fabs(bound.upper.x - model_root->center.x), std::max(fabs(bound.upper.y - model_root->center.y), fabs(bound.upper.z - model_root->center.z)));
    model_root->radius = b_radius;
    model_root->bound = bound;
    
    // Build the tree
    std::cout<<"Initializing Model "<<id<<", Number of triangle:"<<model->mesh->num_triangles()<<std::endl;
    model_root->initialize(aList, bodies.size(), 15, 0, 15);
    
    //model_root->query();
}
    
Vector3 ModelBody::step_position( real_t dt, real_t motion_damping )
{
    return Vector3::Zero();
}

Vector3 ModelBody::step_orientation( real_t dt, real_t motion_damping )
{
    return Vector3::Zero();
}

void ModelBody::apply_force( const Vector3& f, const Vector3& offset )
{
    return;
}

}
