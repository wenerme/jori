package me.wener.jori.grammar.dot;

import java.util.List;
import lombok.Data;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/10/15
 * @see <a href=https://www.graphviz.org/doc/info/lang.html>The DOT Language</a>
 */
public interface Dot {

  interface Element{}
  abstract class AbstractElement implements Element{}
  @Data
  class Node extends AbstractElement{}
  @Data
  class NodeReference extends AbstractElement{}
  @Data
  class Edge extends AbstractElement{
    private NodeReference source;
    private NodeReference target;
  }
  @Data
  class Graph extends AbstractElement{}

  interface Stmt{};
  class GraphStmt{private boolean strict;private String type;String id;private List<Stmt> statements;}
  class NodeStmt{private NodeId nodeId;private AttrStmt attribute;}
  class EdgeStmt{private EdgeSource source;private EdgeTarget target;private AttrStmt attribute;}
  class EdgeSource{private NodeId nodeId;private SubGraph subGraph;}
  class EdgeTarget{private NodeId nodeId;private SubGraph subGraph;private String operation;private AttrStmt attribute;private EdgeTarget target;}
  class AttrStmt{private String type;private List<Attr>attributes;}
  class Attr{private String key;private String value;}
  class NodeId{private String id;private String portId;private String portCompassPt;}
  class SubGraph{private String id;private List<Stmt> statements;}
  /*
graph	:	[ strict ] (graph | digraph) [ ID ] '{' stmt_list '}'
stmt_list	:	[ stmt [ ';' ] stmt_list ]
stmt	:	node_stmt
|	edge_stmt
|	attr_stmt
|	ID '=' ID
|	subgraph
attr_stmt	:	(graph | node | edge) attr_list
attr_list	:	'[' [ a_list ] ']' [ attr_list ]
a_list	:	ID '=' ID [ (';' | ',') ] [ a_list ]
edge_stmt	:	(node_id | subgraph) edgeRHS [ attr_list ]
edgeRHS	:	edgeop (node_id | subgraph) [ edgeRHS ]
node_stmt	:	node_id [ attr_list ]
node_id	:	ID [ port ]
port	:	':' ID [ ':' compass_pt ]
|	':' compass_pt
subgraph	:	[ subgraph [ ID ] ] '{' stmt_list '}'
compass_pt	:	(n | ne | e | se | s | sw | w | nw | c | _)
   */
}
