/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicevolutionvis.datamining.network;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.ArrayList;

import topicevolutionvis.data.Corpus;
import topicevolutionvis.graph.Connectivity;
import topicevolutionvis.graph.ConnectivityType;
import topicevolutionvis.graph.Vertex;
import topicevolutionvis.util.Pair;

/**
 *
 * @author Aretha
 */
public class CoreCitationConnectivity {

    TIntObjectHashMap<ArrayList<Pair>> neigh_aux;

    public CoreCitationConnectivity(Corpus corpus) {
        this.neigh_aux = corpus.getCoreReferences();
    }

    public Connectivity getCitationCore(TIntObjectHashMap<Vertex> vertex) {
        int id_vertex, id_vertex2;
        Pair[][] neighborhood = new Pair[vertex.size()][];
        ArrayList<ArrayList<Pair>> neigh_aux_vertex = new ArrayList<>();
        TIntObjectIterator<Vertex> iterator = vertex.iterator();
        for (int i = 0; i < vertex.size(); i++) {
            iterator.advance();
            neigh_aux_vertex.add(new ArrayList<Pair>());
            id_vertex = iterator.key();
            for (int j = 0; j < neigh_aux.get(id_vertex).size(); j++) {
                id_vertex2 = neigh_aux.get(id_vertex).get(j).index;
                if (vertex.containsKey(id_vertex2)) {
                    neigh_aux_vertex.get(i).add(new Pair(id_vertex2, neigh_aux.get(id_vertex).get(j).value));
                }
            }

            neighborhood[i] = new Pair[neigh_aux_vertex.get(i).size()];
            for (int j = 0; j < neighborhood[i].length; j++) {
                neighborhood[i][j] = neigh_aux_vertex.get(i).get(j);
            }
        }

        Connectivity con = new Connectivity(ConnectivityType.CORE_CITATIONS, true, false);
        con.create(vertex, neighborhood);
        return con;
    }
}
