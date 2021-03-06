/**
 * @file mesh.cpp
 * @brief Mesh class and OBJ loader.
 *
 */

#include "application/opengl.hpp"
#include "scene/mesh.hpp"
#include <iostream>
#include <cstring>
#include <string>
#include <fstream>
#include <sstream>
#include <map>
#include <vector>
#include <numeric>

#define COLOR_SIZE 4

namespace _462 {

struct TriIndex
{
    int vertex;
    int normal;
    int tcoord;

    bool operator<( const TriIndex& rhs ) const {
        if ( vertex == rhs.vertex ) {
            if ( normal == rhs.normal ) {
                return tcoord < rhs.tcoord;
            } else {
                return normal < rhs.normal;
            }
        } else {
            return vertex < rhs.vertex;
        }
    }
};

struct Face
{
    TriIndex v[3];
};

enum ObjFormat
    {
	VERTEX_ONLY = 1 << 0,
	VERTEX_UV = 1 << 1,
	VERTEX_NORMAL = 1 << 2,
	VERTEX_UV_NORMAL = 1 << 3
    };

Mesh::Mesh()
{
    has_tcoords = false;
    has_normals = false;
    has_colors = 0;
}

Mesh::~Mesh() { }

typedef std::vector< Vector3 > PositionList;
typedef std::vector< Vector3 > NormalList;
typedef std::vector< Vector2 > UVList;
typedef std::vector< Face > FaceList;
//typedef std::vector< Vector4 > ColorList;

FaceList* face_listp;
PositionList* position_listp;
NormalList* normal_listp;
UVList* uv_listp;
//ColorList* color_listp;

bool Mesh::load()
{
    std::cout << "Loading mesh from '" << filename << "'..." << std::endl;

    std::string line;
    std::ifstream file( filename.c_str() );

    FaceList face_list;
    PositionList position_list;
    NormalList normal_list;
    UVList uv_list;
    //ColorList color_list;

    position_listp = &position_list;
    normal_listp = &normal_list;
    face_listp = &face_list;
    //color_listp = &color_list;
    static const char* scan_vertex = "%d";
    static const char* scan_vertex_uv = "%d/%d";
    static const char* scan_vertex_normal = "%d//%d";
    static const char* scan_vertex_uv_normal = "%d/%d/%d";

    TriIndex tri[4];

    int line_num = 0;

    std::string token;

    triangles.clear();

    ObjFormat format = VERTEX_ONLY;

    typedef std::map< TriIndex, unsigned int > VertexMap;
    VertexMap vertex_map;

    if ( !file.is_open() ) {
        std::cout << "Error opening file '" << filename << "' for mesh loading.\n";
        return false;
    }

    while ( getline( file, line ) )
    {
        std::stringstream stream( line );
        stream >> token;
        line_num++;

        if ( token == "v" ) {

            Vector3 position;
            stream >> position.x >> position.y >> position.z;

            if ( stream.fail() ) {
                std::cerr << "position syntax error on line " << line_num << std::endl;
                return false;
            }

            position_list.push_back( position );

        } else if ( token == "vn" ) {
            Vector3 normal;
            stream >> normal.x >> normal.y >> normal.z;

            if( stream.fail() ) {
                std::cerr << "normal syntax error on line " << line_num << std::endl;
                return false;
            }
            normal_list.push_back( normal );

        } else if ( token == "vt" ) {

            Vector2 uv;
            stream >> uv.x >> uv.y;

            if ( stream.fail() ) {
                std::cerr << "uv syntax error on line " << line_num << std::endl;
                return false;
            }

            uv_list.push_back( uv );

        } else if ( token == "f" ) {

            std::vector< std::string > face_tokens;
            std::string vert;

            while ( true ) {
                stream >> vert;
                if( stream.fail() )
                    break;
                face_tokens.push_back( vert );
            }

            // if it's the first time parsing a face, figure out the face format
            if ( face_list.size() == 0 ) {
                std::string token = face_tokens[0];

                if ( token.find( "//" ) != std::string::npos ) {
                    format = VERTEX_NORMAL;
                    has_normals = true;
                } else if ( token.find( '/' ) == std::string::npos ) {
                    format = VERTEX_ONLY;
                } else {
                    size_t p1 = token.find( '/' );
                    size_t p2 = token.rfind( '/' );
                    if ( p1 == p2 ) {
                        format = VERTEX_UV;
                        has_tcoords = true;
                    } else {
                        format = VERTEX_UV_NORMAL;
                        has_normals = true;
                        has_tcoords = true;
                    }
                }
            }

            size_t num_vertex;
            num_vertex = face_tokens.size();

            if ( num_vertex > 4 || num_vertex < 3 ) {
                std::cerr << "Syntax error at line " << line_num
                          << ", face has incorrect number of vertices" << std::endl;
                return false;
            }

            for ( size_t i = 0; i < num_vertex; ++i ) {
                switch ( format )
                {
                case VERTEX_ONLY:
                    sscanf( face_tokens[i].c_str(),
                            scan_vertex,
                            &tri[i].vertex );
                    tri[i].normal = 0;
                    tri[i].tcoord = 0;
                    break;

                case VERTEX_UV:
                    sscanf( face_tokens[i].c_str(),
                            scan_vertex_uv,
                            &tri[i].vertex,
                            &tri[i].tcoord );
                    tri[i].normal = 0;
                    break;

                case VERTEX_NORMAL:
                    sscanf( face_tokens[i].c_str(),
                            scan_vertex_normal,
                            &tri[i].vertex,
                            &tri[i].normal );
                    tri[i].tcoord = 0;
                    break;

                case VERTEX_UV_NORMAL:
                    sscanf( face_tokens[i].c_str(),
                            scan_vertex_uv_normal,
                            &tri[i].vertex,
                            &tri[i].tcoord,
                            &tri[i].normal );
                    break;

                default:
                    std::cerr << "Syntax error, unrecongnized face format at line "
                              << line_num << std::endl;
                    break;
                }
            }

            for ( size_t i = 0; i < num_vertex; ++i ) {
                tri[i].vertex--;
                tri[i].normal--;
                tri[i].tcoord--;
            }

            Face f1 = { { tri[0], tri[1], tri[2] } };
            face_list.push_back( f1 );

            if ( num_vertex == 4 ) {
                Face f2 = { { tri[2], tri[3], tri[0] } };
                face_list.push_back( f2 );
            }

        } else if ( token == " " ) {

        } else {
            //std::cerr << "Unknown token on line " << line_num << std::endl;
        }

        token.clear();
        line.clear();
    }

    // verify index list sanity

    int num_vertex = position_list.size();
    int num_normal = normal_list.size();
    int num_tcoord = uv_list.size();

    for ( size_t i = 0; i < face_list.size(); ++i ) {
        const Face& face = face_list[i];
        for ( size_t j = 0; j < 3; ++j ) {
            int vidx = face.v[j].vertex;
            int nidx = face.v[j].normal;
            int tidx = face.v[j].tcoord;

            if (    vidx <  0 || vidx >= num_vertex
		    || nidx < -1 || nidx >= num_normal
		    || tidx < -1 || tidx >= num_tcoord ) {
                std::cout << "Invalid index in face " << i << ".\n";
            }
        }
    }

    // build vertex list using map for shared vertices

    triangles.reserve( face_list.size() );
    vertices.reserve( face_list.size() * 2 );

    // current vertex index, for creating new vertices
    unsigned int vert_idx_counter = 0;

    for ( size_t i = 0; i < face_list.size(); ++i ) {
        const Face& face = face_list[i];
        MeshTriangle tri;
        for ( size_t j = 0; j < 3; ++j ) {
            // two vertices are only actually the same one if the vertex,
            // normal, and tcoord are all the same. use the map to check this.
            std::pair< VertexMap::iterator, bool > rv = vertex_map.insert( std::make_pair( face.v[j], vert_idx_counter ) );
            if ( rv.second ) {
                MeshVertex v;
                v.position = position_list[face.v[j].vertex];
                int nidx = face.v[j].normal;
                v.normal = nidx == -1 ? Vector3::Zero : normal_list[nidx];
                int tidx = face.v[j].tcoord;
                v.tex_coord = tidx == -1 ? Vector2::Zero : uv_list[tidx];
                vertices.push_back( v );
                vert_idx_counter++;
            }

            tri.vertices[j] = rv.first->second;
        }
        triangles.push_back( tri );
    }

    unsigned nvertices = vertices.size();

    Vector3 avePos = Vector3::Zero;
    unsigned sampleSize = 1000U;
    for (unsigned i = 0; i < sampleSize; i++)
    {
	unsigned int a = rand();
	avePos += vertices[a % nvertices].position;
    }
    avePos /= real_t(sampleSize);
    //std::cout << avePos << std::endl;
    for (size_t i = 0; i < nvertices; i++)
    {
	vertices[i].position -= avePos;
    }

    std::cout << "Successfully loaded mesh '" << filename << "'.\n";
    return true;
}

// number of floats per vertex
#define VERTEX_SIZE 8

bool Mesh::create_gl_data()
{
    // if no vertices, nothing to do
    if (vertices.empty() || triangles.empty()) {
        return false;
    }

    // compute normals if needed
    if ( !has_normals ) {
        // first zero out
        for ( size_t i = 0; i < vertices.size(); ++i ) {
            vertices[i].normal = Vector3::Zero;
        }

        // then sum in all triangle normals
        for ( size_t i = 0; i < triangles.size(); ++i ) {
            Vector3 pos[3];
            for ( size_t j = 0; j < 3; ++j ) {
                pos[j] = vertices[triangles[i].vertices[j]].position;
            }
            Vector3 normal = normalize( cross( pos[1] - pos[0], pos[2] - pos[0] ) );
            for ( size_t j = 0; j < 3; ++j ) {
                vertices[triangles[i].vertices[j]].normal += normal;
            }
        }

        // then normalize
        for ( size_t i = 0; i < vertices.size(); ++i ) {
            vertices[i].normal = normalize( vertices[i].normal );
        }

        has_normals = true;
    }

    // build vertex data
    vertex_data.resize( vertices.size() * (VERTEX_SIZE + has_colors*COLOR_SIZE) );
    float* vertex = &vertex_data[0];
    for ( size_t i = 0; i < vertices.size(); ++i )
    {
	int j = 0;
        vertices[i].tex_coord.to_array( vertex + 0 );
	if (has_colors)
	{
	    //vertices[i].color.to_array(vertex + 2);
	    j += 4;
	}
	vertices[i].normal.to_array( vertex + 2 + j );
        vertices[i].position.to_array( vertex + 5 + j);

        vertex += VERTEX_SIZE + has_colors*COLOR_SIZE;
    }
    // build index data
    index_data.resize( triangles.size() * 3 );
    unsigned int* index = &index_data[0];

    for ( size_t i = 0; i < triangles.size(); ++i ) {
        index[0] = triangles[i].vertices[0];
        index[1] = triangles[i].vertices[1];
        index[2] = triangles[i].vertices[2];
        index += 3;
    }
    return true;
}

void Mesh::render() const
{
    assert( index_data.size() > 0 );

    GLenum a = has_colors ? GL_T2F_C4F_N3F_V3F : GL_T2F_N3F_V3F;
    const unsigned int ss = VERTEX_SIZE + has_colors*COLOR_SIZE;
    glInterleavedArrays(a, ss*sizeof vertex_data[0], &vertex_data[0] );
    glDrawElements( GL_TRIANGLES, index_data.size(), GL_UNSIGNED_INT, &index_data[0] );
}

} /* _462 */
